package com.aquib.cy5assignment.web.entities;

import java.util.List;

public class UserGroupData {
    private String groupName;
    private String groupArn;

    private List<UserData> userDataList;

    public UserGroupData(String groupName, String groupArn) {
        this.groupName = groupName;
        this.groupArn = groupArn;
    }

    public UserGroupData() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupArn() {
        return groupArn;
    }

    public void setGroupArn(String groupArn) {
        this.groupArn = groupArn;
    }


    public List<UserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }
}