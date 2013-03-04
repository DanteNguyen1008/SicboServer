/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent.requestprocess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kent.database.DatabaseHandler;
import org.json.simple.JSONObject;

/**
 *
 * @author Kent
 */
public class User {

    private int userId;
    private String username;
    private String email;
    private String dataCreate;
    private String currentBalance;
    
    private DatabaseHandler databaseHandler;

    public User() {
        this.databaseHandler = new DatabaseHandler();
    }

    public JSONObject signUp(String username, String password, String email, long date_create) {

        /*
         * Initital JSON object
         */
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonTask = new JSONObject();
        JSONObject jsonData = new JSONObject();

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
            jsonData.put("message", "Sign up success.");
            jsonData.put("is_success", true);
        } else {
            // If fail
            jsonData.put("message", "Sign up fail for some reason.");
            jsonData.put("is_success", false);
        }
        
        //put for task
        jsonTask.put("taskID", "res_signup");
        jsonTask.put("data", jsonData);
        jsonResult.put("request", jsonTask);  
        return jsonResult;
    }

    public Object signIn(String username, String password) throws SQLException {

        int countResult = 0;
        ResultSet rs = this.databaseHandler.executeQuery(
                "USER_SELECT_LOGIN",
                new String[]{"username", "pass"},
                new Object[]{username, password});
        rs.next();
        countResult = rs.getRow();
        if (countResult == 1) {
            // Sign in success
            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setUsername(rs.getString("username"));
            u.setEmail(rs.getString("email"));
            u.setDataCreate(rs.getString("date_create"));
            u.setCurrentBalance(rs.getString("current_balance"));
            return u;
        } else {
            // Sign in fail
            return false;
        }
    }

    public JSONObject signOut() {
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("request", "signout");
        return jsonResult;
    }

    //<editor-fold defaultstate="collapsed" desc="Ecapsulate private fields">
    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the dataCreate
     */
    public String getDataCreate() {
        return dataCreate;
    }

    /**
     * @param dataCreate the dataCreate to set
     */
    public void setDataCreate(String dataCreate) {
        this.dataCreate = dataCreate;
    }
    
    /**
     * @return the dataCreate
     */
    public String getCurrentBalance() {
        return this.currentBalance;
    }

    /**
     * @param dataCreate the dataCreate to set
     */
    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }
    //</editor-fold>
}