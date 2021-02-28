package korhankonaray.blackjack.modals

import korhankonaray.blackjack.CardType

/**This is class is responsible for creating deck of cards,shuffle the deck
 * and draw a card from the deck. In this class hearts,diamonds,spades,clubs types
 * are created and added them into the card list.The card list holds the whole deck. Size of the card list
 * is 52.
 */
 class Deck {
    /** Holds a card object in each element */
    var cardList = arrayListOf<Card>()
    /**The class's initializer method initalizes the card List.  13 heart objects,13 diamond objects,13 spade
    objects and 13 club objects are added to the cardList */
    init {
        initializeDeck()
    }
    //The method gets the first card of the deck,removes the card from the deck and returns it.
    fun drawCard(): Card {
        val card = cardList.elementAt(0)
        cardList.removeAt(0)
        if(cardList.count() == 0)  //Initializes the deck if no cards remained.
        {
           initializeDeck()
        }
        return card
    }
    fun initializeDeck()
    {
        for (i in 1..13) {
            cardList.add( Card(i, CardType.Diamond))
            cardList.add( Card(i, CardType.Club))
            cardList.add( Card(i, CardType.Heart))
            cardList.add( Card(i, CardType.Spade))

            cardList.shuffle() //Shuffles the deck
        }
    }
}