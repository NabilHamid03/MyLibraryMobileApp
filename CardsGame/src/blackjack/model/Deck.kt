package blackjack.model

internal class Deck{

    private val cards = mutableListOf<Card>()

    init {
        println("DEBUG − Deck init() − Instanciation des cartes")
        for (color in Color.entries) {
            for (value in Value.entries) {
                val card = Card(value, color)
                cards.add(card)
            }
        }
    }

    init {
        println("DEBUG − Deck init() − 2")
    }

    internal fun shuffle() {
        cards.shuffle()
    }

    internal fun hit():Card {
        return cards.removeFirst()
    }

}