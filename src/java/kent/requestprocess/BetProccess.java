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
    Spots spot = null;
    private float currentBalance;
    private float amountBet;

    public BetProccess(User user) {
        this.user = user;
        this.rng = new RandomNumberGenerator();
        this.ptn = new Pattern();
        this.spot = new Spots();

    }

    public JSONObject play(int[] spots, float[] amounts) throws SQLException {

        this.currentBalance = this.user.getCurrentBalance();
        this.amountBet = 0;

        /*
         * Initital JSON object
         */
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonTask = new JSONObject();
        JSONObject jsonData = new JSONObject();
        jsonTask.put("taskID", "res_play");

        int numOfBet = spots.length;

        float totalAmount = this.getTotalAmount(amounts);

        // Check valid balance
        // If Balance is less than Bet amount. Cancel and return invalid message
        if (this.user.getCurrentBalance() < totalAmount || totalAmount > 100) {

            jsonData.put("is_success", false);
            jsonData.put("message", "Your balance is not enough or Total Amount greater than 100!");
            //put for task
            jsonTask.put("data", jsonData);
            jsonResult.put("request", jsonTask);
            return jsonResult;
        } else { // If balance is valid
            // For each Bet. Do Bet
            for (int iBet = 0; iBet < numOfBet; iBet++) {
                this.doBet(
                        this.spot.getSpot(spots[iBet]),
                        (float) amounts[iBet],
                        rng.getRandom());
            }

            // Update to databse
            if (this.user.updateBalance(this.currentBalance)) {
                this.currentBalance = this.user.getCurrentBalance();
                jsonData.put("is_success", true);
                if (this.amountBet > 0) {
                    jsonData.put("iswin", true);
                } else if (this.amountBet <= 0) {
                    jsonData.put("iswin", false);
                }
                jsonData.put("dice1", this.rng.getDice1());
                jsonData.put("dice2", this.rng.getDice2());
                jsonData.put("dice3", this.rng.getDice3());
                jsonData.put("current_balance", this.currentBalance);
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
    public void doBet(Spots spot, Float amount, int[] rnd) throws SQLException {

        Pattern ptnOfSpot = this.ptn.getPattern(spot.getPatternId());

        if (1 == spot.getSpotId()) { // Big
            if (this.rng.isBig()) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        if (2 == spot.getSpotId()) { // Small
            if (this.rng.isSmall()) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        if (3 == spot.getSpotId()) { // Triple 1
            if (this.rng.isSpecificTriple(1)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (4 == spot.getSpotId()) { // Triple 2
            if (this.rng.isSpecificTriple(2)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (5 == spot.getSpotId()) { // Triple 3
            if (this.rng.isSpecificTriple(3)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (6 == spot.getSpotId()) { // Triple 4
            if (this.rng.isSpecificTriple(4)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (7 == spot.getSpotId()) { // Triple 5
            if (this.rng.isSpecificTriple(5)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (8 == spot.getSpotId()) { // Triple 6
            if (this.rng.isSpecificTriple(6)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        if (9 == spot.getSpotId()) { // Double 1
            if (this.rng.isSpecificDouble(1)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (10 == spot.getSpotId()) { // Double 2
            if (this.rng.isSpecificDouble(2)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (11 == spot.getSpotId()) { // Double 3
            if (this.rng.isSpecificDouble(3)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (12 == spot.getSpotId()) { // Double 4
            if (this.rng.isSpecificDouble(4)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (13 == spot.getSpotId()) { // Double 5
            if (this.rng.isSpecificDouble(5)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (14 == spot.getSpotId()) { // Double 6
            if (this.rng.isSpecificDouble(6)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        if (16 == spot.getSpotId()) { // Three Dice = 4
            if (this.rng.isTotalEqualTo(4)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (20 == spot.getSpotId()) { // Three Dice = 5
            if (this.rng.isTotalEqualTo(5)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (18 == spot.getSpotId()) { // Three Dice = 6
            if (this.rng.isTotalEqualTo(6)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (22 == spot.getSpotId()) { // Three Dice = 7
            if (this.rng.isTotalEqualTo(7)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (24 == spot.getSpotId()) { // Three Dice = 8
            if (this.rng.isTotalEqualTo(8)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (26 == spot.getSpotId()) { // Three Dice = 9
            if (this.rng.isTotalEqualTo(9)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (34 == spot.getSpotId()) { // Three Dice = 10
            if (this.rng.isTotalEqualTo(10)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (35 == spot.getSpotId()) { // Three Dice = 11
            if (this.rng.isTotalEqualTo(11)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (27 == spot.getSpotId()) { // Three Dice = 12
            if (this.rng.isTotalEqualTo(12)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (25 == spot.getSpotId()) { // Three Dice = 13
            if (this.rng.isTotalEqualTo(13)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (23 == spot.getSpotId()) { // Three Dice = 14
            if (this.rng.isTotalEqualTo(14)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (19 == spot.getSpotId()) { // Three Dice = 15
            if (this.rng.isTotalEqualTo(15)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (21 == spot.getSpotId()) { // Three Dice = 16
            if (this.rng.isTotalEqualTo(16)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (17 == spot.getSpotId()) { // Three Dice = 17
            if (this.rng.isTotalEqualTo(17)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        if (28 == spot.getSpotId()) { // Three Dice = 1
            if (this.rng.isSingle(1)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (29 == spot.getSpotId()) { // Three Dice = 2
            if (this.rng.isSingle(2)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (30 == spot.getSpotId()) { // Three Dice = 3
            if (this.rng.isSingle(3)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (31 == spot.getSpotId()) { // Three Dice = 4
            if (this.rng.isSingle(4)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (32 == spot.getSpotId()) { // Three Dice = 5
            if (this.rng.isSingle(5)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }
        if (33 == spot.getSpotId()) { // Three Dice = 6
            if (this.rng.isSingle(6)) {
                this.amountBet = this.amountBet + amount * ptnOfSpot.getOdds();
            } else {
                this.amountBet = this.amountBet - amount;
            }
        }

        this.currentBalance = this.currentBalance + this.amountBet;
    }
}
