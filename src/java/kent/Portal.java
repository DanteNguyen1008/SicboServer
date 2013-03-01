/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kent;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            jsonResponse = user.signIn();

        } else if ("signout".equals(req)) {
            jsonResponse = user.signOut();

        } //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Random Number Generator">
        else if ("three_dice_random".equals(req)) {
            
            HttpSession s = request.getSession();           
            if (null != s.getAttribute("n")) {
                s.setAttribute("n", 1);
            }
            else {
                //int i = Integer.parseInt(s.getAttribute("n"));
                //int i = 
                //s.setAttribute("n", i + 1);
            }
            
            out.println(s.getId());
            
            
            
            jsonResponse = RandomNumberGenerator.getThreeDiceRandom();

        }
        //</editor-fold>


        /*
         * Response back result in JSON format
         */
        try {
            System.out.println(req);
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