package com.github.valv.directorium.app

import com.github.valv.directorium.view.MainView
import tornadofx.*

class Directorium : App(MainView::class, Styles::class) {
    init {
        FX.layoutDebuggerShortcut = null
    }
}
