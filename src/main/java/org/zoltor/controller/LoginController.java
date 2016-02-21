package org.zoltor.controller;

import org.zoltor.model.User;
import org.zoltor.model.entities.UserEntity;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by zoltor on 05.11.14.
 */
public class LoginController extends BaseController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isAuthorized = isUserAuthorized(req);
        String uri = req.getRequestURI();
        if (isAuthorized) {
            if (req.getParameter("username") != null) {
                UserEntity user;
                try {
                    user = User.getUserInfo(req.getParameter("username"));
                    Cookie userNameCookie = new Cookie("z_c_uname", user.getNick());
                    Cookie userPwdCookie = new Cookie("z_c_pwd", user.getEncryptedPassword());
                    userNameCookie.setDomain(uri);
                    userPwdCookie.setDomain(uri);
                    if (req.getParameter("remember") != null) {
                        userNameCookie.setMaxAge(Integer.MAX_VALUE);
                        userPwdCookie.setMaxAge(Integer.MAX_VALUE);
                    }
                    resp.addCookie(userNameCookie);
                    resp.addCookie(userPwdCookie);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    smartRedirect(req, resp);
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
