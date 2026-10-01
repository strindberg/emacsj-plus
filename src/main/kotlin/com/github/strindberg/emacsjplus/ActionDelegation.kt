package com.github.strindberg.emacsjplus

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.UpdateSession
import com.intellij.openapi.actionSystem.ex.ActionUtil

/**
 * Whether the action reports itself as enabled for [e].
 *
 * [AnAction.update] is `@ApiStatus.OverrideOnly` - the platform reserves the call for itself - so it is never invoked
 * directly here. Inside an update session the platform computes the delegate's presentation for us, on the update thread
 * the delegate asks for; outside one - the events this plugin synthesizes carry no session - [ActionUtil] makes the call.
 *
 * Only the enabled flag is read. `ActionWrapperUtil.update` would be the off-the-shelf alternative, but it copies the
 * whole presentation, which would replace the wrapping action's own text and description with the delegate's.
 */
internal fun AnAction.isEnabledIn(e: AnActionEvent): Boolean {
    val session = e.updateSession
    return if (session == UpdateSession.EMPTY) {
        val probe = AnActionEvent.createEvent(this, e.dataContext, null, e.place, e.uiKind, e.inputEvent)
        ActionUtil.updateAction(this, probe)
        probe.presentation.isEnabled
    } else {
        session.presentation(this).isEnabled
    }
}
