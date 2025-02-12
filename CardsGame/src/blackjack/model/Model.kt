package blackjack.model

interface Model {
    val bank: Player
    var player : Player
    // return the object bank or player
    val winner: Player
    // create a new Player with name and assign it to the player property
    fun addName(name: String)
    // take a card in the deck and add it to player’s hand
    fun playerHit()
    // return true if player.score is <= 21 (Game.MAX)
    fun playerCanHit(): Boolean
}