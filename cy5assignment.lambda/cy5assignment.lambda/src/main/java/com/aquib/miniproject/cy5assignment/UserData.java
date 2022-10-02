package com.aquib.miniproject.cy5assignment;

import com.amazonaws.services.identitymanagement.model.Group;

import java.util.List;

public class UserData {
    private String userName;
    private String userArn;
    private List<UserGroupData> groups;



    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserArn() {
        return userArn;
    }

    public void setUserArn(String userArn) {
        this.userArn = userArn;
    }

    public List<UserGroupData> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroupData> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userName='" + userName + '\'' +
                ", groups=" + groups +
                '}';
    }
}

