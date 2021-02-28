package korhankonaray.blackjack.activities

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import korhankonaray.blackjack.R
import korhankonaray.blackjack.helpers.Globals
import korhankonaray.blackjack.helpers.ImageFactory
import korhankonaray.blackjack.helpers.MetricsUtil
import korhankonaray.blackjack.modals.Deck
import korhankonaray.blackjack.modals.Hand
import kotlinx.android.synthetic.main.activity_black_jack.*

/** Activity class of the BlackJack game.
 */
class BlackJackActivity : AppCompatActivity() {
    lateinit var deck : Deck //Deck object which holds the deck of cards
    lateinit var dealerHand : Hand //Hand object for the dealer
    lateinit var playerHand :Hand // Hand object for the player
    var currentBetValue = 0  //Current bet value for the player
    var currentBalanceValue = 0  //Current balance value for the player
    lateinit var bitmapOptions : BitmapFactory.Options
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)
        val initialBalanceValue = 1000
        balanceValue.setText(initialBalanceValue.toString())
        deck = Deck()
        betValue.setText("0")
        //Initialize bitmap option for cards
         bitmapOptions = BitmapFactory.Options().apply {
            // inJustDecodeBounds = true
            inSampleSize = 2
        }
        initializeGame()
        //Triggered when the player click's deal button.
        buttonDeal.setOnClickListener {
            layout_deal.isVisible =  false   //Hides the bet layout
            layout_gameCommand.isVisible = true //Shows hit and stand button
            dealerHandLayout.removeAllViews()  //Resets dealer hand layout
            playerHandLayout.removeAllViews()  //Resets player hand layout
            currentBetValue = betValue.text.toString().toInt()
            currentBalanceValue = balanceValue.text.toString().toInt()

            //Checking if balance is enough for betting
            val newBalanceValue = currentBalanceValue - currentBetValue
            if(newBalanceValue < 0)
            {
                showAlertDialog(getString(R.string.notEnoughBalance))
                return@setOnClickListener
            }
            balanceValue.text  = newBalanceValue.toString()
            Thread {
                playerHand.insert(deck.drawCard())
                runOnUiThread {
                    playerHandScore.text = playerHand.value.toString()
                    paintCardToScreen(playerHand, playerHandLayout)
                }
                Thread.sleep(Globals.dealSpeed)
                dealerHand.insert(deck.drawCard())
                runOnUiThread {
                    dealerHandScore.text = dealerHand.value.toString()
                    paintCardToScreen(dealerHand, dealerHandLayout)
                }
                Thread.sleep(Globals.dealSpeed)
                playerHand.insert(deck.drawCard())
                runOnUiThread {
                    playerHandScore.text = playerHand.value.toString()
                    paintCardToScreen(playerHand, playerHandLayout)
                }
                Thread.sleep(Globals.dealSpeed)
                runOnUiThread {
                    paintClosedCardToScreen()
                }
                Thread.sleep(Globals.dealSpeed)
                //Checking if player has score 21 in first round. If first round value is 21, player automatically wins the game
               if(playerHand.value == 21 && playerHand.cardList.count() == 2)
               {
                   runOnUiThread {
                       showAlertDialog(getString(R.string.blackJackWin))
                       balanceValue.text  = (currentBalanceValue + currentBetValue).toString()
                   }
               }
            }.start()

        }
        //Resets the bet value.
        buttonClear.setOnClickListener {
            betValue.text = "0"
        }
        //Triggered when the player clicks hit button.
        buttonHit.setOnClickListener{
            playerHand.insert(deck.drawCard())
            playerHandScore.text = playerHand.value.toString()
            paintCardToScreen(playerHand, playerHandLayout)
            Thread{

                if (playerHand.value > 21) {

                    Thread.sleep(Globals.dealSpeed)
                    runOnUiThread {
                        showAlertDialog(getString(R.string.busted))
                    }
                }
            }.start()
        }
        //Triggered when the player clicks stand button.
        buttonStand.setOnClickListener{
            Thread {
                //Dealer draw cards until his score is greater than player's score.
            while(dealerHand.value <17) {
                dealerHand.insert(deck.drawCard())
                runOnUiThread {
                    paintCardToScreen(dealerHand, dealerHandLayout)
                    dealerHandScore.text = dealerHand.value.toString()
                }
                Thread.sleep(Globals.dealSpeed)
            }
                runOnUiThread {
                    if (dealerHand.value > 21) {
                        showAlertDialog(getString(R.string.dealerBusted))
                        balanceValue.text  = (currentBalanceValue + currentBetValue).toString()
                    }
                    else
                    {
                        if(dealerHand.value== playerHand.value)
                        {
                            showAlertDialog(getString(R.string.push))
                            balanceValue.text  = (currentBalanceValue).toString()
                        }
                        else if(dealerHand.value < playerHand.value)
                        {
                            showAlertDialog(getString(R.string.playerWin))
                            balanceValue.text  = (currentBalanceValue + currentBetValue).toString()
                        }
                        else {
                            showAlertDialog(getString(R.string.dealerWin))
                        }
                    }
                }
            }.start()
        }
        bet5.setOnClickListener {
            val value = 5 + betValue.text.toString().toInt()
            betValue.setText(value.toString())
        }
        bet25.setOnClickListener {
            val value = 25 + betValue.text.toString().toInt()
            betValue.setText(value.toString())
        }
        bet50.setOnClickListener {
            val value = 50 + betValue.text.toString().toInt()
            betValue.setText(value.toString())
        }
        bet100.setOnClickListener {
            val value = 100 + betValue.text.toString().toInt()
            betValue.setText(value.toString())
        }
    }
    //Creates necessary objects and initializes the game view.
    fun initializeGame()
    {

        dealerHand = Hand()
        playerHand = Hand()
        layout_deal.isVisible = true       // Clear and Deal button visibility
        layout_gameCommand.isVisible = false   //Hit and stand button visibility
       // betValue.setText("0")
    }
    //Calls the paint method for the right top card.
    fun paintClosedCardToScreen()
    {
        paintCard( BitmapFactory.decodeResource(resources,  R.drawable.blue_back), dealerHandLayout)
    }
    fun paintCardToScreen(hand: Hand, layout: LinearLayout)
    {
        layout.removeAllViews()
        hand.cardList.forEach()
        {

            paintCard( BitmapFactory.decodeResource(resources, ImageFactory.getImage(it), bitmapOptions), layout)
        }
    }
    private fun paintCard(cardImage : Bitmap, layout: LinearLayout) {
        val newView = ImageView(this)
        newView.setImageBitmap(cardImage)
        newView.adjustViewBounds = true
        val cardWidth = 80
        val cardHeight = 120

        val params = LinearLayout.LayoutParams(layout.getLayoutParams())
        if (layout.size == 0) {
            params.setMargins(0, 0, 0, 0)
        } else {
            params.setMargins(
                    MetricsUtil.convertDpToPixel(-cardWidth.toFloat() / 2, this).toInt(), 0, 0, 0
            )
        }
        newView.setLayoutParams(params)
        newView.maxWidth = MetricsUtil.convertDpToPixel(cardWidth.toFloat(), this).toInt()
        newView.maxHeight = MetricsUtil.convertDpToPixel(cardHeight.toFloat(), this).toInt()
        layout.addView(newView)
    }
    fun showAlertDialog(message: String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            initializeGame()
        }
        builder.show()
    }
}