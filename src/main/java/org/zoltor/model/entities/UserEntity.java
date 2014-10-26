package org.zoltor.model.entities;

import org.zoltor.common.HelperUtils;

import java.util.Date;

/**
 * Created by zoltor on 26.10.14.
 */
public class UserEntity {
    private long id;
    private String nick;
    private String password;
    private String encryptedPassword;
    private String email;
    private Date registered;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        if (encryptedPassword == null) {
            this.encryptedPassword = HelperUtils.getMd5Digest(nick, password, email);
        }
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = HelperUtils.getDateFromDb(registered);
    }
}
