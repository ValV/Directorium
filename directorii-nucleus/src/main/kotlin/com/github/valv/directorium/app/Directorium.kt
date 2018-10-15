package com.github.valv.directorium.app

import com.github.valv.directorium.view.ViewMain
import tornadofx.*

class Directorium : App(ViewMain::class, Styles::class) {
    init {
        FX.layoutDebuggerShortcut = null
    }
}
