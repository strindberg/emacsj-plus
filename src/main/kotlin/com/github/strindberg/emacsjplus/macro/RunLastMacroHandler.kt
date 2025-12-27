package com.github.strindberg.emacsjplus.macro

import kotlin.concurrent.thread
import com.github.strindberg.emacsj.EmacsJService
import com.intellij.ide.actionMacro.ActionMacroManager
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler
import org.intellij.lang.annotations.Language

@Language("devkit-action-id")
internal const val ACTION_RUN_LAST_MACRO = "com.github.strindberg.emacsjplus.actions.macro.runlastmacro"

class RunLastMacroHandler : EditorWriteActionHandler() {

    companion object {
        init {
            EmacsJService.instance.registerSingleAction(ACTION_RUN_LAST_MACRO)
        }
    }

    override fun executeWriteAction(editor: Editor, caret: Caret?, dataContext: DataContext) {
        val macroManager = ActionMacroManager.getInstance()
        if (macroManager.hasRecentMacro()) {
            val times = EmacsJService.instance.universalArgument()
            WriteAction.run<Throwable> {
                thread {
                    EmacsJService.instance.setRepeating(true)
                    try {
                        repeat(times) {
                            if (EmacsJService.instance.isRepeating()) {
                                macroManager.playbackLastMacro()
                                do {
                                    Thread.sleep(10)
                                } while (macroManager.isPlaying)
                            }
                        }
                    } finally {
                        EmacsJService.instance.setRepeating(false)
                    }
                }
            }
        }
    }
}
