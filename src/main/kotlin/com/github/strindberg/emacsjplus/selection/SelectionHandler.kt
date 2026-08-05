package com.github.strindberg.emacsjplus.selection

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.actionSystem.get
import com.intellij.openapi.editor.ex.EditorEx
import org.intellij.lang.annotations.Language

@Language("devkit-action-id")
internal const val ACTION_SELECT_NEXT_OCCURRENCE = "com.github.strindberg.emacsjplus.actions.selection.selectnextoccurrence"

@Language("devkit-action-id")
internal const val ACTION_SELECT_ALL_OCCURRENCES = "com.github.strindberg.emacsjplus.actions.selection.selectalloccurrences"

enum class SelectionType { NEXT, ALL }

class SelectionHandler(private val type: SelectionType) {

    fun doExecute(e: AnActionEvent) {
        val editor = e.dataContext[EDITOR]
        (editor as? EditorEx)?.let { ex ->
            if (ex.isStickySelection) {
                val selectionStart = ex.selectionModel.selectionStart
                val selectionEnd = ex.selectionModel.selectionEnd
                ex.isStickySelection = false
                ex.selectionModel.setSelection(selectionStart, selectionEnd)
            }
            val action = when (type) {
                SelectionType.NEXT -> ActionManager.getInstance().getAction("SelectNextOccurrence")
                SelectionType.ALL -> ActionManager.getInstance().getAction("SelectAllOccurrences")
            }
            action.actionPerformed(e)
        }
    }
}
