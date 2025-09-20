package com.github.strindberg.emacsjplus.actions.rectangle

import com.github.strindberg.emacsjplus.rectangle.RectangleSelectHandler
import com.intellij.openapi.editor.actions.TextComponentEditorAction

class RectangleSelectAction : TextComponentEditorAction(RectangleSelectHandler())
