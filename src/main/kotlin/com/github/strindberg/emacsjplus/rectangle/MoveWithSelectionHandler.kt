package com.github.strindberg.emacsjplus.rectangle

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorAction
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.ex.EditorEx
import org.intellij.lang.annotations.Language

enum class MovementType { DOWN, UP, RIGHT, LEFT }

@Language("devkit-action-id")
internal const val ACTION_SELECTION_MOVE_DOWN = "com.github.strindberg.emacsjplus.actions.rectangle.movecaretdown"

@Language("devkit-action-id")
internal const val ACTION_SELECTION_MOVE_UP = "com.github.strindberg.emacsjplus.actions.rectangle.movecaretup"

@Language("devkit-action-id")
internal const val ACTION_SELECTION_MOVE_RIGHT = "com.github.strindberg.emacsjplus.actions.rectangle.movecaretright"

@Language("devkit-action-id")
internal const val ACTION_SELECTION_MOVE_LEFT = "com.github.strindberg.emacsjplus.actions.rectangle.movecaretleft"

class MoveWithSelectionHandler(val type: MovementType) : EditorActionHandler() {

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext) {
        (editor as? EditorEx)?.let { ex ->
            val action = when (type) {
                MovementType.DOWN -> getAction("EditorDownWithSelection", "EditorDown")
                MovementType.UP -> getAction("EditorUpWithSelection", "EditorUp")
                MovementType.RIGHT -> getAction("EditorRightWithSelection", "EditorRight")
                MovementType.LEFT -> getAction("EditorLeftWithSelection", "EditorLeft")
            }
            action.handler.execute(editor, caret, dataContext)
        }
    }

    private fun getAction(withSelection: String, withoutSelection: String): EditorAction {
        val actionManager = ActionManager.getInstance()
        val action = if (RectangleSelectHandler.isRectangleSelection) {
            actionManager.getAction(withSelection)
        } else {
            actionManager.getAction(withoutSelection)
        }
        return (action as EditorAction)
    }
}
