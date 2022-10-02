package com.aquib.miniproject.cy5assignment;

public class UserGroupData {
    private String groupName;
    private String groupArn;

    public UserGroupData(String groupName, String groupArn) {
        this.groupName = groupName;
        this.groupArn = groupArn;
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
}
