package com.github.strindberg.emacsjplus.actions.macro

import com.github.strindberg.emacsjplus.macro.RunLastMacroHandler
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class RunLastMacroAction : DumbAwareAction() {

    private val handler = RunLastMacroHandler()

    // Matches the update thread of the platform action the handler delegates to.
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

    override fun update(e: AnActionEvent) {
        handler.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        handler.doExecute(e)
    }
}
