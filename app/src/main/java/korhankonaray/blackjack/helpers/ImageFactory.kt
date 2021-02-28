package korhankonaray.blackjack.helpers

import korhankonaray.blackjack.CardType
import korhankonaray.blackjack.R
import korhankonaray.blackjack.modals.Card

object ImageFactory {

    lateinit var clubCardsImages :ArrayList<Int>
    lateinit var diamondCardsImages : ArrayList<Int>
    lateinit var heartCardsImages : ArrayList<Int>
    lateinit var  spadeCardsImages : ArrayList<Int>

    init {
        initializeCardImages()
    }
    fun initializeCardImages()
    {
        clubCardsImages =  arrayListOf<Int>(
            R.drawable.c_1,
            R.drawable.c_2,
            R.drawable.c_3,
            R.drawable.c_4,
            R.drawable.c_5,
            R.drawable.c_6,
            R.drawable.c_7,
            R.drawable.c_8,
            R.drawable.c_9,
            R.drawable.c_10,
            R.drawable.c_11,
            R.drawable.c_12,
            R.drawable.c_13
        )
        diamondCardsImages  =  arrayListOf<Int>(
            R.drawable.d_1,
            R.drawable.d_2,
            R.drawable.d_3,
            R.drawable.d_4,
            R.drawable.d_5,
            R.drawable.d_6,
            R.drawable.d_7,
            R.drawable.d_8,
            R.drawable.d_9,
            R.drawable.d_10,
            R.drawable.d_11,
            R.drawable.d_12,
            R.drawable.d_13
        )
        heartCardsImages=  arrayListOf<Int>(
            R.drawable.h_1,
            R.drawable.h_2,
            R.drawable.h_3,
            R.drawable.h_4,
            R.drawable.h_5,
            R.drawable.h_6,
            R.drawable.h_7,
            R.drawable.h_8,
            R.drawable.h_9,
            R.drawable.h_10,
            R.drawable.h_11,
            R.drawable.h_12,
            R.drawable.h_13
        )
        spadeCardsImages =  arrayListOf<Int>(
            R.drawable.s_1,
            R.drawable.s_2,
            R.drawable.s_3,
            R.drawable.s_4,
            R.drawable.s_5,
            R.drawable.s_6,
            R.drawable.s_7,
            R.drawable.s_8,
            R.drawable.s_9,
            R.drawable.s_10,
            R.drawable.s_11,
            R.drawable.s_12,
            R.drawable.s_13
        )
    }
    fun getImage(card: Card) : Int
    {
        val cardNumber = card.number -1
        if(card.type == CardType.Club)
        {
            return clubCardsImages[cardNumber]
        }
        else if(card.type == CardType.Diamond)
        {
            return diamondCardsImages[cardNumber]
        }
        else if(card.type == CardType.Heart)
        {
            return heartCardsImages[cardNumber]
        }
        else if(card.type == CardType.Spade)
        {
            return spadeCardsImages[cardNumber]
        }
        return 0
    }

}