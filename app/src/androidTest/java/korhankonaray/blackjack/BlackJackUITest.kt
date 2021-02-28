package korhankonaray.blackjack

import android.view.View
import android.widget.TextView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import korhankonaray.blackjack.activities.BlackJackActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException
@RunWith(AndroidJUnit4::class)
@LargeTest
class BlackJackUITest {
   val delay : Long = 2500
    @get:Rule
    val activityRule = ActivityScenarioRule(BlackJackActivity::class.java)
    @Test
    fun playTheGame() {
        //Bet for $200
        clickBet100Button()
        clickBet100Button()
        while(getPlayerBalanceValue()>100) {  //The bot plays the game until the balance is lower than $200
            clickDealButton()
            Thread.sleep(delay)
            if(getPlayerHandScore()==21)
            {
                Thread.sleep(delay)
                checkBlackJackWonMessageShown()
                clickAlertBoxOKButton()
                continue
            }
            var playerHandScore = getPlayerHandScore()
            while(playerHandScore<=16) //The bot clicks the hit button until the hand value is greater than 16
            {
                clickHitButton()
                Thread.sleep(500)
                playerHandScore = getPlayerHandScore()
                if(playerHandScore>21) //The bot has busted
                {
                    Thread.sleep(delay)
                    checkIfBustedMessageShown()
                    clickAlertBoxOKButton()
                }
            }
            if(playerHandScore>21)
            {
                continue
            }
            clickStandButton()
            var dealerHandScore: Int
            do {
               dealerHandScore = getDealerHandScore()

           }while(dealerHandScore<17)
            Thread.sleep(delay)
           if(dealerHandScore>21) //Dealer has busted
           {
                checkIfDealerBustedMessageShown()
               clickAlertBoxOKButton()
           }
           else
           {
               if(dealerHandScore== playerHandScore) //The bot and the dealer have same score
               {
                   checkIfPushMessageShown()
               }
               else if(dealerHandScore < playerHandScore)  //The bot has won
               {
                   checkIfPlayerWonMessageShown()
               }
               else   //Dealer has won
               {
                   checkIfDealerWonMessageShown()
               }
               clickAlertBoxOKButton()
           }
        }
    }

    fun checkIfBustedMessageShown() : ViewInteraction
    {
        return onView(withText(R.string.busted)).check(ViewAssertions.matches(isDisplayed()))
    }
    fun checkIfDealerWonMessageShown() : ViewInteraction
    {
        return onView(withText(R.string.dealerWin)).check(ViewAssertions.matches(isDisplayed()))
    }
    fun checkIfDealerBustedMessageShown() : ViewInteraction
    {
        return onView(withText(R.string.dealerBusted)).check(ViewAssertions.matches(isDisplayed()))
    }
    fun checkIfPlayerWonMessageShown() : ViewInteraction
    {
        return onView(withText(R.string.playerWin)).check(ViewAssertions.matches(isDisplayed()))
    }
    fun checkIfPushMessageShown() : ViewInteraction
    {
        return onView(withText(R.string.push)).check(ViewAssertions.matches(isDisplayed()))
    }
    fun checkBlackJackWonMessageShown() : ViewInteraction
    {
        return onView(withText(R.string.blackJackWin)).check(ViewAssertions.matches(isDisplayed()))
    }
    fun getPlayerBalanceValue() : Int
    {
        val balanceValueView =  onView(withId(R.id.balanceValue))
        val balanceValue = getText(balanceValueView)
        return balanceValue.toInt()
    }
    fun getPlayerHandScore() : Int
    {
        val playerHandScoreView =  onView(withId(R.id.playerHandScore))
        val playerHandScore = getText(playerHandScoreView)
       return playerHandScore.toInt()
    }
    fun getDealerHandScore() : Int
    {
        val dealerHandScoreView =  onView(withId(R.id.dealerHandScore))
        val dealerHandScore = getText(dealerHandScoreView)
        return dealerHandScore.toInt()
    }
    fun clickBet100Button()
    {
         onView(withId(R.id.bet100)).perform(click())
    }
    fun clickDealButton()
    {
       onView(withId(R.id.buttonDeal)).perform(click())
    }
    fun clickClearButton()
    {
        onView(withId(R.id.buttonClear)).perform(click())
    }
    fun clickHitButton()
    {
        onView(withId(R.id.buttonHit)).perform(click())
    }
    fun clickStandButton()
    {
        onView(withId(R.id.buttonStand)).perform(click())
    }
    fun clickAlertBoxOKButton()
    {
        onView(withText("OK")).perform(click())
    }
    fun waitUntilAlertBoxShown() : ViewInteraction
    {
        return onView(isRoot()).perform(waitForView(android.R.string.ok, 5000))
    }

    fun getText(matcher: ViewInteraction): String {
        var text = String()
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }

    fun waitForView(viewId: Int, timeout: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with id $viewId; during $timeout millis."
            }

            override fun perform(uiController: UiController, rootView: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout
                val viewMatcher = withId(viewId)

                do {
                    // Iterate through all views on the screen and see if the view we are looking for is there already
                    for (child in TreeIterables.breadthFirstViewTraversal(rootView)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }
                    // Loops the main thread for a specified period of time.
                    // Control may not return immediately, instead it'll return after the provided delay has passed and the queue is in an idle state again.
                    uiController.loopMainThreadForAtLeast(100)
                } while (System.currentTimeMillis() < endTime) // in case of a timeout we throw an exception -&gt; test fails
                throw PerformException.Builder()
                    .withCause(TimeoutException())
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(rootView))
                    .build()
            }
        }
    }
}