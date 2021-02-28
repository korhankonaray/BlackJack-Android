package korhankonaray.blackjack.modals

import korhankonaray.blackjack.CardType


class Card(number: Int, type: CardType) {
    var number : Int   //Card number
    var type: CardType  //Enum Card type. Club, Heart, Spade, Diamond

    // initializer block
    init {
        this.number = number
        this.type = type
    }
}