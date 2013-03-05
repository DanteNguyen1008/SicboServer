/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent.requestprocess;

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
    
    public BetProccess() {
        
        
    }
    
    public void play(int[] patterns, int[] amounts) {
        
    }
    
    public void doBet(Pattern ptn, Float amount, int[] rnd) {
        /*
         * Check balance
         */
        
        
        if (1 == ptn.getPatternId()) { // Big
            
        }
        
    }
    
}
