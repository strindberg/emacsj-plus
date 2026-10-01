package com.github.strindberg.emacsjplus.macro

import kotlin.time.Duration.Companion.milliseconds
import com.github.strindberg.emacsj.EmacsJService
import com.github.strindberg.emacsjplus.EmacsJPlusScope
import com.github.strindberg.emacsjplus.isEnabledIn
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionUiKind
import com.intellij.openapi.actionSystem.ActionWrapperUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.actionSystem.get
import com.intellij.openapi.application.EDT
import com.intellij.openapi.editor.Editor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language

@Language("devkit-action-id")
internal const val ACTION_RUN_LAST_MACRO = "com.github.strindberg.emacsjplus.actions.macro.runlastmacro"

/**
 * The platform's own 'play last macro' action. Everything this handler needs from the macro subsystem is reached through it,
 * because `com.intellij.ide.actionMacro` - `ActionMacroManager` included - is marked `@ApiStatus.Internal` as a package.
 */
@Language("devkit-action-id")
private const val ACTION_PLAYBACK_LAST_MACRO = "PlaybackLastMacro"

/** How often the delegate is re-checked while waiting for a macro run to finish. */
private val PLAYBACK_POLL = 10.milliseconds

/**
 * Runs the last recorded macro, [EmacsJService.universalArgument] times.
 *
 * Playback is asynchronous, so the runs have to be sequenced. The platform tracks this in `ActionMacroManager.isPlaying`,
 * which is internal, but `PlaybackLastMacro` reports it: that action is enabled exactly when a macro exists and none is
 * playing. Polling its presentation is therefore an internal-API-free way of waiting for a run to complete.
 *
 * Speed of macro execution can be configured through the registry: registry -> actionSystem.playback.typecommand.delay
 */
class RunLastMacroHandler {

    companion object {
        init {
            EmacsJService.instance.registerSingleAction(ACTION_RUN_LAST_MACRO)
        }
    }

    fun update(e: AnActionEvent) {
        val delegate = delegate()
        e.presentation.isEnabled = delegate != null && delegate.isEnabledIn(e)
    }

    fun doExecute(wrapper: AnAction, e: AnActionEvent) {
        delegate()?.let { delegate ->
            e.dataContext[EDITOR]?.let { editor ->
                val times = EmacsJService.instance.universalArgument()

                // Playback returns before the macro has run, so the repetitions are driven from a coroutine rather than by blocking
                // the caller. Dispatchers.EDT keeps every action invocation on the thread the action system expects.
                EmacsJPlusScope.instance.scope.launch(Dispatchers.EDT) {
                    EmacsJService.instance.setRepeating(true)
                    try {
                        var remaining = times
                        while (remaining > 0 && EmacsJService.instance.isRepeating() && !editor.isDisposed) {
                            ActionWrapperUtil.actionPerformed(delegate.event(editor), wrapper, delegate)
                            delegate.awaitPlaybackFinished(editor)
                            remaining--
                        }
                    } finally {
                        EmacsJService.instance.setRepeating(false)
                    }
                }
            }
        }
    }

    private fun delegate(): AnAction? = ActionManager.getInstance().getAction(ACTION_PLAYBACK_LAST_MACRO)
}

private suspend fun AnAction.awaitPlaybackFinished(editor: Editor) {
    // isRepeating is checked as well, so that cancelling a repeat also breaks a wait that would otherwise never end -
    // the delegate stays disabled for as long as a macro is playing, but also if the last macro is removed meanwhile.
    while (!isEnabledIn(event(editor)) && EmacsJService.instance.isRepeating() && !editor.isDisposed) {
        delay(PLAYBACK_POLL)
    }
}

private fun AnAction.event(editor: Editor): AnActionEvent =
    AnActionEvent.createEvent(
        this,
        DataManager.getInstance().getDataContext(editor.contentComponent),
        null,
        ActionPlaces.KEYBOARD_SHORTCUT,
        ActionUiKind.NONE,
        null,
    )
