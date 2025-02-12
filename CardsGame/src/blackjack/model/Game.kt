package blackjack.model

class Game : Model{

    companion object {
        const val MAX = 21
        const val MIN_IA = 17
    }

    private val deck = Deck() //déclaration et instanciation
    override lateinit var player : Player
    override var bank = Bank()
    override val winner: Player
        get() = if (player.isLost(bank)) bank else player

    init {
        deck.shuffle() // mélange des cartes
    }

    override fun addName(name : String){
        if(name.isBlank()){
            throw IllegalArgumentException("le nom ne peut pas être vide")
        }
        player = Player(name)
    }

    override fun playerCanHit(): Boolean {
        return player.score < MAX
    }

    override fun playerHit() {
        if (playerCanHit()) {
            player.add(deck.hit())
        }
    }

    fun bankCanHit(): Boolean {
        return bank.score < MIN_IA
    }

    fun bankHit() {
        if (bankCanHit()) {
            bank.add(deck.hit())
        }
    }
}