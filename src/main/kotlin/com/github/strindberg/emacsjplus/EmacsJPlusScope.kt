package com.github.strindberg.emacsjplus

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import kotlinx.coroutines.CoroutineScope

/** The plugin's coroutine scope, for work that outlives a keystroke but has no service of its own to belong to. */
@Service
internal class EmacsJPlusScope(val scope: CoroutineScope) {

    companion object {
        internal val instance: EmacsJPlusScope
            get() = ApplicationManager.getApplication().getService(EmacsJPlusScope::class.java)
    }
}
