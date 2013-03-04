/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent.database;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kent
 */
public class DatabaseHandler {

    private Connection connection = null;//The database connection.
    private String strError;
    private CallableStatement proc;

    //<editor-fold defaultstate="collapsed" desc="Connect Mysql">
    /**
     *
     * @param strServerName
     * @param strUser
     * @param strPass
     * @param strDatabase
     * @return
     * @throws SQLException
     */
    public Connection connectMySql(
            String strServerName,
            String strUser,
            String strPass,
            String strDatabase) throws SQLException {

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(
                    "jdbc:mysql://" + strServerName + "/" + strDatabase,
                    strUser,
                    strPass);
            System.out.println("Connected");

        } catch (ClassNotFoundException e) {
            this.strError = "Error:" + e.getMessage();
        } catch (InstantiationException e) {
            this.strError = "Error:" + e.getMessage();
        } catch (IllegalAccessException e) {
            this.strError = "Error:" + e.getMessage();
        }
        return con;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Execute SQL statement">
    /**
     * Execute SQL statement
     *
     * @param strProcedureName
     * @param parameterNames
     * @param parameterValues
     * @return
     */
    public int executeSQL(
            String strProcedureName,
            String[] parameterNames,
            Object[] parameterValues) throws SQLException {
        int j = 0;


        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = this.connectMySql("10.0.1.15:3306", "kibowvn", "123456", "kb_sicbo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String parameterCount = "";
            for (int i = 0; i < parameterNames.length; i++) {
                if (i == (parameterNames.length - 1)) {
                    parameterCount += "?";
                } else {
                    parameterCount += "?,";
                }
            }

            this.proc = this.connection.prepareCall("{call " + strProcedureName + " (" + parameterCount + ")}");                
            for (int i = 0; i < parameterNames.length; i++) {
                this.proc.setObject(i + 1, parameterValues[i]);
            }
            j = this.proc.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return j;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Execute SQL query. Return ResultSet">
    /**
     *
     * @param procedureName
     * @param parameterNames
     * @param parameterValues
     * @return
     */
    public ResultSet executeQuery(
            String procedureName,
            String[] parameterNames,
            Object[] parameterValues) {
        
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = connectMySql("10.0.1.15:3306", "kibowvn", "123456", "kb_sicbo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        ResultSet resultSet = null;
        try {
            String parameterCount = "";
            for (int i = 0; i < parameterNames.length; i++) {
                if (i == (parameterNames.length - 1)) {
                    parameterCount += "?";
                } else {
                    parameterCount += "?,";
                }
            }
            this.proc = this.connection.prepareCall("{call " + procedureName + " (" + parameterCount + ")}");
            //Ex: this.proc = connection.prepareCall("{call SELECT_LOGIN('a1provip002@mail.com','123456')}");
            for (int i = 0; i < parameterNames.length; i++) {
                this.proc.setObject(i + 1, parameterValues[i]);
            }
            
            resultSet = this.proc.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }
    //</editor-fold>
}
