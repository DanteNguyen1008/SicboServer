/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent.requestprocess;

import java.sql.SQLException;
import org.json.simple.JSONObject;

/**
 *
 * @author Kent
 */
public class BetProccess {

    static String[] patterns = {
        "Big",
        "Small",
        "Specific 'Triples' or 'Alls'",
        "Specific Doubles",
        "Any Triple or All 'Alls'",
        "Three Dice Total 4 or 17",
        "Three Dice Total 6 or 15",
        "Three Dice Total 5 or 16",
        "Three Dice Total 7 or 14",
        "Three Dice Total 8 or 13",
        "Three Dice Total 9 or 12",
        "Single Dice Bet 1",
        "Single Dice Bet 2",
        "Single Dice Bet 3",
        "Three Dice total 10 or 11"
    };
    User user = null;
    RandomNumberGenerator rng = null;
    Pattern ptn = null;
    private float currentBalance;
    private float amountBet = 0;

    public BetProccess(User user) {
        this.user = user;
        this.rng = new RandomNumberGenerator();
        this.ptn = new Pattern();
        this.currentBalance = this.user.getCurrentBalance();
        
        
    }

    public JSONObject play(int[] patterns, float[] amounts) throws SQLException {

        /*
         * Initital JSON object
         */
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonTask = new JSONObject();
        JSONObject jsonData = new JSONObject();
        jsonTask.put("taskID", "res_play");

        int numOfBet = patterns.length;

        float totalAmount = this.getTotalAmount(amounts);

        // Check valid balance
        // If Balance is less than Bet amount. Cancel and return invalid message
        if (this.user.getCurrentBalance() < totalAmount) {

            jsonData.put("is_success", false);
            jsonData.put("message", "Your balance is not enough!");
            //put for task
            jsonTask.put("data", jsonData);
            jsonResult.put("request", jsonTask);
            return jsonResult;
        } else { // If balance is valid
            // For each Bet. Do Bet
            for (int iBet = 0; iBet < numOfBet; iBet++) {
                this.doBet(
                        this.ptn.getPattern(patterns[iBet]),
                        (float) amounts[iBet],
                        rng.getRandom());
            }

            // Update to databse
            if (this.user.updateBalance(this.currentBalance)) {
                jsonData.put("is_success", true);
                if (this.amountBet > 0) {
                    jsonData.put("iswin", true);
                } else if (this.amountBet <= 0) {
                    jsonData.put("iswin", false);
                }
                jsonData.put("dice1",this.rng.getDice1());
                jsonData.put("dice2",this.rng.getDice2());
                jsonData.put("dice3",this.rng.getDice3());
                jsonData.put("current_balance",this.currentBalance);
                //put for task
                jsonTask.put("data", jsonData);
                jsonResult.put("request", jsonTask);
                return jsonResult;
            }

        }

        return jsonResult;
    }

    public float getTotalAmount(float[] amounts) {
        int numOfBet = amounts.length;
        float totalAmount = 0;
        for (int iBet = 0; iBet < numOfBet; iBet++) {
            totalAmount = totalAmount + amounts[iBet];
        }

        return totalAmount;
    }

    /*
     * Do bets.
     * After do bets . update current_balance property
     */
    public void doBet(Pattern ptn, Float amount, int[] rnd) {

        if (1 == ptn.getPatternId()) { // Big
            if (this.rng.isBig()) {
                this.amountBet = this.amountBet + amount;
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        if (2 == ptn.getPatternId()) { // Small
            if (this.rng.isSmall()) {
                this.amountBet = this.amountBet + amount;
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        this.currentBalance = this.currentBalance + this.amountBet;
    }
}
