package org.zoltor.controller;

import org.zoltor.model.User;
import org.zoltor.model.entities.UserEntity;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by zoltor on 05.11.14.
 */
public abstract class BaseController extends HttpServlet {

    protected abstract void processRequest(HttpServletRequest req, HttpServletResponse resp);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isUserAuthorized(req)) {
            BaseController controller = getController(getAction(req));
            controller.processRequest(req, resp);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    ////////////////////
    // Private methods
    ////////////////////

    private boolean isUserAuthorized(HttpServletRequest request) {
        UserEntity user = new UserEntity();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("z_c_uname")) {
                user.setNick(cookie.getValue());
            }
            if (cookie.getName().equals("z_c_pwd")) {
                user.setEncryptedPassword(cookie.getValue());
            }
        }
        if (user.getNick() == null || user.getEncryptedPassword() == null) {
            user.setNick(request.getParameter("login"));
            user.setPassword(request.getParameter("password"));
        }
        try {
            return User.isAuthorized(user);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String getAction(HttpServletRequest request) {
        return (request.getParameter("action") == null) ? "" : request.getParameter("action");
    }

    private BaseController getController(String action) {
        action = action.trim();
        if (action.isEmpty()) {
            return null;
        }
        if (action.equals("login")) {
            return new LoginController();
        }
        // and so on ...
        return null;
    }
}
