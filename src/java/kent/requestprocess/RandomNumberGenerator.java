/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent.requestprocess;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.json.simple.JSONObject;
/**
 *
 * @author Kent
 */
public class RandomNumberGenerator {

    public RandomNumberGenerator() {
    }
    
    public static JSONObject getThreeDiceRandom() {
        
        RandomData randomData = new RandomDataImpl();        
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonDices = new JSONObject();
        
        jsonDices.put("dice1", randomData.nextInt(1, 6));
        jsonDices.put("dice2", randomData.nextInt(1, 6));
        jsonDices.put("dice3", randomData.nextInt(1, 6));
        
        jsonResult.put("three_dice_random", jsonDices);
        return jsonResult;
    }
    
}
