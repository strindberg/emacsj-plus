package com.github.strindberg.emacsjplus.rectangle

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.ex.EditorEx
import org.intellij.lang.annotations.Language

@Language("devkit-action-id")
internal const val ACTION_RECTANGLE_SELECT = "com.github.strindberg.emacsjplus.actions.rectangle.rectangleselect"

class RectangleSelectHandler : EditorActionHandler() {

    companion object {
        var isRectangleSelection = false
    }

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext) {
        (editor as? EditorEx)?.let { ex ->
            isRectangleSelection = !isRectangleSelection
            if (isRectangleSelection) {
                ex.caretModel.removeSecondaryCarets()

                val selectionModel = editor.selectionModel

                val selStart = selectionModel.selectionStart
                val logicalSelStart = editor.offsetToLogicalPosition(selectionModel.selectionStart)
                val logicalSelEnd = editor.offsetToLogicalPosition(selectionModel.selectionEnd)

                val caretOffset = ex.caretModel.offset
                val blockStart = if (selStart == caretOffset) logicalSelEnd else logicalSelStart
                val blockEnd = if (selStart == caretOffset) logicalSelStart else logicalSelEnd

                ex.isStickySelection = false
                ex.isColumnMode = true
                selectionModel.setBlockSelection(blockStart, blockEnd)
            } else {
                ex.isColumnMode = true
            }
        }
    }
}
