package blackjack.controller

import blackjack.model.Game
import blackjack.view.*

class Controller {
    fun play() {
        val game = Game()
        initialize()
        game.addName(askName())
        var playing = true
        while(playing) {
            game.playerHit()
            game.playerHit()
            displayCards(game.player.hand)
            var hitting = askToHit()
            while (hitting) {
                game.playerHit()
                displayCards(game.player.hand)
                if (!game.playerCanHit()) {
                    displayOver()
                    break
                }
                hitting = askToHit()
            }
            while (game.bankCanHit()) {
                game.bankHit()
                displayCards(game.bank.hand)
            }
            displayWinLose(game.winner)
            playing = askBoolean()
        }
        displayOver()
    }
}