package org.zoltor.controller;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zoltor on 21.11.14.
 */
public class LogoutController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie userNameCookie = new Cookie("z_c_uname", "");
        Cookie userPwdCookie = new Cookie("z_c_pwd", "");
        userNameCookie.setMaxAge(0);
        userPwdCookie.setMaxAge(0);
        resp.addCookie(userNameCookie);
        resp.addCookie(userPwdCookie);
        smartRedirect(req, resp);
    }
}
