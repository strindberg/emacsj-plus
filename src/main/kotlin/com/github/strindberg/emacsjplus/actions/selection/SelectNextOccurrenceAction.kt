package com.github.strindberg.emacsjplus.actions.selection

import com.github.strindberg.emacsjplus.selection.SelectionHandler
import com.github.strindberg.emacsjplus.selection.SelectionType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class SelectNextOccurrenceAction : DumbAwareAction() {

    private val handler = SelectionHandler(SelectionType.NEXT)

    override fun actionPerformed(e: AnActionEvent) {
        handler.doExecute(e)
    }
}
