package com.github.strindberg.emacsjplus.actions.rectangle

import com.github.strindberg.emacsjplus.rectangle.MoveWithSelectionHandler
import com.github.strindberg.emacsjplus.rectangle.MovementType
import com.intellij.openapi.editor.actions.TextComponentEditorAction

class MoveCaretLeftAction : TextComponentEditorAction(MoveWithSelectionHandler(MovementType.LEFT))
