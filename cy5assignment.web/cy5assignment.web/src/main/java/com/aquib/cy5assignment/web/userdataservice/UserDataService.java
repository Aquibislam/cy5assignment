package com.aquib.cy5assignment.web.userdataservice;

import com.aquib.cy5assignment.web.entities.UserData;
import com.aquib.cy5assignment.web.entities.UserGroupData;

import java.io.IOException;
import java.util.List;

public interface UserDataService {
    public List<UserData> getUserData() throws IOException;

    List<UserGroupData> getUserDataGroupedByGroup() throws IOException;
}
