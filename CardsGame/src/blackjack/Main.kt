package blackjack

import blackjack.controller.Controller
import blackjack.model.Card
import blackjack.model.Color
import blackjack.model.Deck
import blackjack.model.Value


fun main() {
    val controller = Controller()
    controller.play()
}