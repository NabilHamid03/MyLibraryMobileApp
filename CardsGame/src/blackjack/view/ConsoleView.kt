package blackjack.view

import blackjack.model.Card
import blackjack.model.Player

fun initialize() {
    println("Bienvenue au Blackjack !")
}

fun displayOver() {
    println("La partie est terminée !")
}

fun displayCards(cards:List<Card>) {
    println("Voici vos cartes actuelles :" +
            cards.toString())

}

fun displayWinLose(winner : Player) {
    println("""Le gagnant est : ${winner.name}""")
}

fun askName():String {
    println("Comment vous appelez vous ?")
    val name = readln()
    return name
}

fun askToHit() : Boolean {
    println("Voulez vous tirer une carte ?")
    var answer = readln()
    while (true) {
        when (answer) {
            "Oui","oui","Y" -> return true
            "Non","non","N" -> return false
            else -> {
                println("Réponse non reconnue.")
                println("Voulez vous tirer une carte ?")
                answer = readln()
            }
        }
    }
}

fun askBoolean():Boolean {
    println("Voulez vous rejouez ?")
    var answer = readln()
    while (true) {
        when (answer) {
            "Oui","oui","Y" -> return true
            "Non","non","N" -> return false
            else -> {
                println("Réponse non reconnue.")
                println("Voulez vous rejouez ?")
                answer = readln()
            }
        }
    }
}