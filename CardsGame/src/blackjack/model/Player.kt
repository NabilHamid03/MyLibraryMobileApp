package blackjack.model

open class Player(open val name:String = "UNKNOWN") {
    private val _hand = mutableListOf<Card>()

    val hand: List<Card>
        get() = _hand

    val score : Int
        get() = _hand.sumOf { it.value.score }

    fun add(card: Card) {
        _hand.add(card)
    }

    fun clear() {
        _hand.clear()
    }

    fun isLost(bank : Player):Boolean {
        return when {
            score > Game.MAX -> true
            bank.score > Game.MAX -> false
            else -> score <= bank.score
        }
    }
}