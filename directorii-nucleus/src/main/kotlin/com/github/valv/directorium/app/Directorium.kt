package com.github.valv.directorium.app

import com.github.valv.directorium.view.ViewMain
import kotlinx.serialization.ImplicitReflectionSerializer
import tornadofx.*

@ImplicitReflectionSerializer
class Directorium : App(ViewMain::class, Styles::class) {
    init {
        FX.layoutDebuggerShortcut = null
    }
}
