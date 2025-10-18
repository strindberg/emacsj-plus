package com.github.strindberg.emacsjplus.actions.macro

import com.github.strindberg.emacsjplus.macro.RunLastMacroHandler
import com.intellij.openapi.editor.actionSystem.EditorAction

class RunLastMacroAction : EditorAction(RunLastMacroHandler())
