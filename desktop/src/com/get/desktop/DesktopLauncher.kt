package com.get.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import GetGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setTitle("Kamchatcka!");
        config.setWindowedMode(529, 662);
        Lwjgl3Application(GetGame(), config)
    }
}
