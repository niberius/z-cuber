package org.zoltor.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by p.ordenko on 20.11.2014, 11:30.
 */
public class IndexController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isAuthorized = isUserAuthorized(req);
        RequestDispatcher requestDispatcher;
        String to;
        if (isAuthorized) {
            long roomId = getRoomIdForUser(getUserInfoFromCookies(req.getCookies()).get(COOKIE_TYPE.LOGIN));
            if (roomId != -1) {
                to = "/game.jsp?roomId=" + String.valueOf(roomId);
            } else {
                to = "/lobby.jsp";
            }
        } else {
            to = "/login.jsp";
        }
        resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        resp.setHeader("Location", getRootUrl(req) + to);
        requestDispatcher = req.getRequestDispatcher(to);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
