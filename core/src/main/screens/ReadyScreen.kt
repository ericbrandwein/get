package screens

import Kamchatka
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import gamelogic.*
import screens.running.RunningScreen
import screens.running.parseMapInfoFromJsonFile

class ReadyScreen(private val game: Kamchatka) : KamchatkaScreen(game) {
    private val message = BitmapFont()
    override val viewport: Viewport = FitViewport(800F, 480F, game.camera)
    override val inputProcessor: InputProcessor = object: InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            when (keycode) {
                Input.Keys.Q -> Gdx.app.exit()
                Input.Keys.S -> switchToRunningScreen()
            }
            return true
        }
    }

    private fun switchToRunningScreen() {
        val (countryColors, politicalMap) =
            parseMapInfoFromJsonFile(MAP_INFO_FILE_NAME)
        val goal =
            Goal(listOf(OccupyContinent(politicalMap.continents.first())))
        val players = mapOf("Eric" to Color.White, "Nico" to Color.Black)
        val referee = Referee.forGame(players, politicalMap, listOf(goal, goal))
        game.setKamchatkaScreen(RunningScreen(game, referee, countryColors))
    }

    override fun render(delta: Float) {
        super.render(delta)
        game.batch.begin()
        message.draw(
            game.batch, "Press s to start, q to quit",
            viewport.worldWidth / 2, viewport.worldHeight / 2
        )
        game.batch.end()
    }

    companion object {
        private const val MAP_INFO_FILE_NAME = "mapa.json"
    }
}
