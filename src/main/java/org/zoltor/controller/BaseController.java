package org.zoltor.controller;

import org.zoltor.model.User;
import org.zoltor.model.entities.UserEntity;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoltor on 05.11.14.
 */
public class BaseController extends HttpServlet {

    protected enum COOKIE_TYPE {
        LOGIN,
        PASSWORD
    }

    protected boolean isUserAuthorized(HttpServletRequest request) {
        UserEntity user = new UserEntity();
        Map<COOKIE_TYPE, String> cookiesUserInfo = getUserInfoFromCookies(request.getCookies());
        user.setNick(cookiesUserInfo.get(COOKIE_TYPE.LOGIN));
        user.setEncryptedPassword(cookiesUserInfo.get(COOKIE_TYPE.PASSWORD));
        if (user.getNick().isEmpty() || user.getEncryptedPassword().isEmpty()) {
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

    protected long getRoomIdForUser(String nick) {
        try {
            return User.getRoomIdForUser(nick);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1L;
        }
    }

    protected Map<COOKIE_TYPE, String> getUserInfoFromCookies(Cookie... cookies) {
        Map<COOKIE_TYPE, String> result = new HashMap<COOKIE_TYPE, String>();
        result.put(COOKIE_TYPE.LOGIN, "");
        result.put(COOKIE_TYPE.PASSWORD, "");
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("z_c_uname")) {
                result.put(COOKIE_TYPE.LOGIN, cookie.getValue());
            }
            if (cookie.getName().equals("z_c_pwd")) {
                result.put(COOKIE_TYPE.PASSWORD, cookie.getValue());
            }
        }
        return result;
    }

    protected String getRootUrl(HttpServletRequest request) {
        String[] urlParts = request.getRequestURL().toString().split("/");
        return  urlParts[0] + "//" + urlParts[2] + "/" + urlParts[3];
    }

    protected void smartRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isAuthorized = isUserAuthorized(request);
        RequestDispatcher requestDispatcher;
        String to;
        if (isAuthorized) {
            long roomId = getRoomIdForUser(getUserInfoFromCookies(request.getCookies()).get(COOKIE_TYPE.LOGIN));
            if (roomId != -1) {
                to = "/game.jsp?roomId=" + String.valueOf(roomId);
            } else {
                to = "/lobby.jsp";
            }
        } else {
            to = "/login.jsp";
        }
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", getRootUrl(request) + to);
        requestDispatcher = request.getRequestDispatcher(to);
        requestDispatcher.forward(request, response);
    }
}
