package com.aquib.cy5assignment.web.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserData {
    public UserData() {
    }
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

