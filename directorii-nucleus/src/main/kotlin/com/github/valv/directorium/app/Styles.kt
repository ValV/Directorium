package com.github.valv.directorium.app

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val basis by cssclass()

        // Cool Blues

        val clDeepsAqua = c("#003B46")
        val clOcean = c("#07575B")
        val clWave = c("#66A5Ad")
        val clSeafoam = c("#C4DFE6")

        // Aqua blues

        val clPeaccockBlue = c("#004D47")
        val clDeepAqua = c("#128277")
        val clLichen = c("#52958B")
        val clMist = c("#B9C4C9")

        // Icy Blues & Grays

        val clOvercast = c("#F1F1F2")
        val clWarnGray = c("#BCBABE")
        val clIce = c("#A1D6E2")
        val clGlacierBlue = c("#1995AD")

        // Fresh & Energetic

        val clBlueSky = c("#4CB5F5")
        val clGranite = c("#B7B8B6")
        val clPine = c("#34675C")
        val clFields = c("#B3C100")

        // Modern & Crisp

        val clDeepGreen = c("#0F1B07")
        val clWhite = c("#FFFFFF")
        val clPlants = c("#5C821A")
        val clNewGrowth = c("#C6D166")

        // Fresh Greens

        val clEmerald = c("#265C00")
        val clGreenBean = c("#68A225")
        val clLightGreen = c("#B3DE81")
        val clCotton = c("#FDFFFF")

        // Misty Greens

        val clForest = c("#04202C")
        val clEvergreen = c("#304040")
        val clPines = c("5B7065")
        val clFog = c("#C9D1C8")

        // Hazy Grays

        val clBlueGreen = c("#2C4A52")
        val clWaterway = c("#537072")
        val clHaze = c("#8E9B97")
        val clSmog = c("#F4EBDB")

        // Berry Blues

        val clMidnightBlue = c("#1E1F26")
        val clIndigoInk = c("#283655")
        val clBlueberry = c("#4D648D")
        val clPeriwinkle = c("#D0E1F9")

        // Wintery Reds

        val clBerry = c("#A10115")
        val clCherry = c("#D72C16")
        val clChiffon = c("#F0EFEA")
        val clSmoke = c("#C0B2B5")

        // Chocolaty Browns

        val clCocoa = c("#301B28")
        val clChocolate = c("#523634")
        val clToffee = c("#B6452C")
        val clFrosting = c("#DDC5A2")

        // Spicy Neutrals

        val clCayenne = c("#AF4425")
        val clCinnamon = c("#662E1C")
        val clCream = c("#EBDCB2")
        val clCaramel = c("#C9A66B")

        //Polished & Inviting

        val clLinen = c("#EAE2D6")
        val clOyster = c("#D5C3AA")
        val clPewterLight = c("#867666")
        val clLemonTea = c("#E1B80D")

        // Naturally Elegant

        val clYellowPear = c("#EBDF00")
        val clMossGreen = c("#7E7B15")
        val clUmber = c("#563E20")
        val clGold = c("#B38540")

        // Trendy & Metropolitan

        val clDarkAqua = c("#488A99")
        val clGoldLight = c("#DBAE58")
        val clNoCharocal = c("#FBE9E7") // Wrong color
        val clGray = c("#B4B4B4")

        // Modern Urban

        val clSunshine = c("#F9BA32")
        val clSteelBlue = c("#426E86")
        val clBone = c("#F8F1E5")
        val clCoal = c("2F3131")

        // Urban Living

        val clSoftGray = c("#DDDEDE")
        val clBlackish = c("#232122")
        val clHouseplant = c("#A5C05B")
        val clBlueGray = c("7BA4A8")

        // Classic Metallics

        val clBlackSteel = c("#080706")
        val clPaper = c("#EFEFEF")
        val clGoldLeaf = c("#D1B280")
        val clSilver = c("#594D46")

        // Orange Accent

        val clWoodVeneer = c("#756867")
        val clSandDollar = c("#D5D6D2")
        val clCharocal = c("#353C3F")
        val clOrange = c("#FF8D3F")

        // Shabby Chic Neutrals

        val clMetal = c("#6C5F5B")
        val clKraftPaper = c("#CDAB81")
        val clNewsprint = c("#DAC3B3")
        val clPewter = c("#4F4A45")

        // Neutral & Versatile

        val clSlate = c("#626D71")
        val clCeramic = c("#CDCDC0")
        val clLatte = c("#DDBC95")
        val clCoffee = c("#B38867")

    }

    init {
        basis {
            prefWidth = 640.px
            prefHeight = 360.px
        }
        button {
            padding = box(5.px, 20.px)
        }
        label and heading {
            padding = box(8.px)
            fontSize = 16.px
            fontWeight = FontWeight.BOLD
        }
    }
}