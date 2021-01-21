package com.get.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import Kamchatka

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setTitle("Kamchatcka!");
        config.setWindowedMode(1536,810 );
        Lwjgl3Application(Kamchatka(), config)
    }
}
