package org.zoltor.model.entities;

import org.zoltor.common.HelperUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zoltor on 26.10.14.
 */
public class RoomEntity {
    private long id;
    private UserEntity hostUser;
    private String name;
    private String password;
    private String encryptedPassword;
    private Date created;
    private boolean isPrivate;
    private boolean isActive;
    private List<UserEntity> joinedUsers = new ArrayList<UserEntity>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getHostUser() {
        return hostUser;
    }

    public void setHostUser(UserEntity hostUser) {
        this.hostUser = hostUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        if (encryptedPassword == null && password != null) {
            encryptedPassword = HelperUtils.getMd5Digest(password);
        }
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = HelperUtils.getDateFromDb(created);
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        if (password == null && encryptedPassword == null) {
            this.isPrivate = false;
        } else {
            this.isPrivate = isPrivate;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<UserEntity> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(List<UserEntity> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }
}
