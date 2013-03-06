/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import kent.requestprocess.BetProccess;
import kent.requestprocess.RandomNumberGenerator;
import kent.requestprocess.User;
import org.json.simple.JSONObject;

/**
 *
 * @author Kent
 */
@WebServlet(name = "Portal", urlPatterns = {"/Portal"})
public class Portal extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        /*
         * Get type of request         
         */
        String req = "";
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        HttpSession se = request.getSession();
        se.setMaxInactiveInterval(1800);
        User user = new User();

        req = request.getParameter("type_of_request");

        /* =====================================================================
         * Switch to corespondent request
         * =====================================================================
         */

        //<editor-fold defaultstate="collapsed" desc="User request">
        if ("signup".equals(req)) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            long date_create = System.currentTimeMillis() / 1000;

            jsonResponse = user.signUp(username, password, email, date_create);

        } else if ("signin".equals(req)) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            try {
                Object signInResult = user.signIn(username, password);


                JSONObject jsonTask = new JSONObject();
                JSONObject jsonData = new JSONObject();

                if (signInResult instanceof User) {
                    User u = (User) signInResult;

                    // If success, create and mantain session
                    

                    if (se.isNew()) {//dont have session yet
                        // Set session
                        se.setAttribute("user", signInResult);

                        jsonData.put("signin_success", true);
                        jsonData.put("email", u.getEmail());
                        jsonData.put("current_balance", u.getCurrentBalance());
                        
                        //put for task
                        jsonTask.put("taskID", "res_sigin");
                        jsonTask.put("data", jsonData);
                        jsonResponse.put("request", jsonTask);
                    } else {//have session  -dont need to login
                        // Set session
                        
                         
                        //put for task
                        jsonTask.put("taskID", "res_sigin");
                        jsonTask.put("data", jsonData);
                        jsonResponse.put("request", jsonTask);
                         
                    }
                } else {
                    jsonData.put("signin_success", false);

                    jsonTask.put("taskID", "res_sigin");
                    jsonTask.put("data", jsonData);
                    jsonResponse.put("request", jsonTask);
                }

            } catch (SQLException ex) {
                Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
            }



        } else if ("signout".equals(req)) {
            if (se != null) {
                se.invalidate();
            }

            JSONObject jsonTask = new JSONObject();
            JSONObject jsonData = new JSONObject();

            jsonData.put("message", "Sign out successfully");

            jsonTask.put("taskID", "res_sigout");
            jsonTask.put("data", jsonData);
            jsonResponse.put("request", jsonTask);

        } //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Bet requests">
        else if ("play_bet".equals(req)) {

            String[] betSpots = request.getParameterValues("betspots");
            String[] betAmounts = request.getParameterValues("betamounts");
            int numOfSpots = betSpots.length;

            int[] spots = new int[numOfSpots];
            float[] amounts = new float[numOfSpots];

            // Transfer params in type of String into type in int, Float
            for (int ibet = 0; ibet < numOfSpots; ibet++) {
                spots[ibet] = Integer.parseInt(betSpots[ibet]);
                amounts[ibet] = Float.parseFloat(betAmounts[ibet]);
            }
            //HttpSession se = request.getSession();
            User curUser = (User) se.getAttribute("user");

            BetProccess betProc = new BetProccess(curUser);
            try {
                jsonResponse = betProc.play(spots, amounts);
            } catch (SQLException ex) {
                Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
            }

        } //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Random Number Generator">
        else if ("three_dice_random".equals(req)) {

            HttpSession s = request.getSession();
            if (null != s.getAttribute("n")) {
                s.setAttribute("n", 1);
            } else {
                //int i = Integer.parseInt(s.getAttribute("n"));
                //int i = 
                //s.setAttribute("n", i + 1);
            }

            out.println(s.getId());
            out.println("");
            RandomNumberGenerator rng = new RandomNumberGenerator();
            int[] randomDice = rng.getRandom();
            out.println(randomDice[0] + " " + randomDice[1] + " " + randomDice[2] + rng.isBig() + rng.isSmall());



            jsonResponse = RandomNumberGenerator.getThreeDiceRandom();

        }
        //</editor-fold>


        /*
         * Response back result in JSON format
         */
        try {
            System.out.println(req);
            System.out.println(jsonResponse.toJSONString());
            out.println(jsonResponse.toJSONString());
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
