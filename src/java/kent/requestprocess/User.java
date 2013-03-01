/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent.requestprocess;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import kent.database.DatabaseHandler;
import org.json.simple.JSONObject;

/**
 *
 * @author Kent
 */
public class User {

    private DatabaseHandler databaseHandler;

    public User() {
        this.databaseHandler = new DatabaseHandler();
    }

    public JSONObject signUp(String username, String password, String email, long date_create)  {

        /*
         * Initital JSON object
         */
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonMessage = new JSONObject();

        /*
         * Execute SQL
         */
        int rowAffected = 0;
        try {
            rowAffected = this.databaseHandler.executeSQL(
                    "USER_INSERT",
                    new String[]{"username", "password", "email", "date_create"},
                    new Object[]{username, password, email, date_create});
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }



        if (rowAffected > 0) {
            // If success
            jsonMessage.put("message", "Sign up success.");
            jsonMessage.put("is_success", true);
        } else {
            // If fail
            jsonMessage.put("message", "Sign up fail for some reason.");
            jsonMessage.put("is_success", false);
        }

        jsonResult.put(
                "res_signup", jsonMessage);
        return jsonResult;
    }

    public JSONObject signIn() {
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("request", "signin");
        return jsonResult;
    }

    public JSONObject signOut() {
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("request", "signout");
        return jsonResult;
    }
}