package com.github.strindberg.emacsjplus.selection

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.actionSystem.get
import com.intellij.openapi.editor.ex.EditorEx
import org.intellij.lang.annotations.Language

@Language("devkit-action-id")
internal const val ACTION_SELECT_NEXT_OCCURRENCE = "com.github.strindberg.emacsjplus.actions.selection.selectnextoccurrence"

@Language("devkit-action-id")
internal const val ACTION_SELECT_ALL_OCCURRENCES = "com.github.strindberg.emacsjplus.actions.selection.selectalloccurrences"

enum class SelectionType(internal val platformActionId: String) {
    NEXT(IdeActions.ACTION_SELECT_NEXT_OCCURENCE),
    ALL(IdeActions.ACTION_SELECT_ALL_OCCURRENCES),
}

/**
 * Wraps the platform's occurrence actions, which leave sticky selection switched on. Every caret move then re-extends the
 * selection from the sticky start, so the occurrences the platform just selected are immediately overwritten.
 *
 * Sticky selection is therefore switched off before delegating. Note that turning it off clears the selection of the current
 * caret - see `EditorImpl.isStickySelectionChanged` - so the selection is restored afterward: the platform actions search
 * for the selected text and would otherwise fall back to selecting the word under the caret.
 */
class SelectionHandler(private val type: SelectionType) {

    fun update(e: AnActionEvent) {
        val delegate = delegate()
        if (delegate == null) {
            e.presentation.isEnabled = false
        } else {
            delegate.update(e)
        }
    }

    fun doExecute(e: AnActionEvent) {
        (e.dataContext[EDITOR] as? EditorEx)?.dropStickySelection()
        delegate()?.actionPerformed(e)
    }

    private fun delegate(): AnAction? = ActionManager.getInstance().getAction(type.platformActionId)
}

private fun EditorEx.dropStickySelection() {
    if (isStickySelection) {
        val hadSelection = selectionModel.hasSelection()
        val selectionStart = selectionModel.selectionStart
        val selectionEnd = selectionModel.selectionEnd

        isStickySelection = false

        if (hadSelection) {
            selectionModel.setSelection(selectionStart, selectionEnd)
        }
    }
}
