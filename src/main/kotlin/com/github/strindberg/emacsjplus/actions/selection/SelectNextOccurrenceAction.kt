package com.github.strindberg.emacsjplus.actions.selection

import com.github.strindberg.emacsjplus.selection.SelectionHandler
import com.github.strindberg.emacsjplus.selection.SelectionType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class SelectNextOccurrenceAction : DumbAwareAction() {

    private val handler = SelectionHandler(SelectionType.NEXT)

    // Matches the update thread of the platform action the handler delegates to.
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        handler.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        handler.doExecute(this, e)
    }
}
