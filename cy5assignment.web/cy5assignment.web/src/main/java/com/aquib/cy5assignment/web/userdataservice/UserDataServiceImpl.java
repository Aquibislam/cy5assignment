package com.aquib.cy5assignment.web.userdataservice;

import com.aquib.cy5assignment.web.dao.UserDataDao;
import com.aquib.cy5assignment.web.entities.UserData;
import com.aquib.cy5assignment.web.entities.UserGroupData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserDataServiceImpl implements UserDataService {
    @Autowired
    private UserDataDao userDataDao;

    public List<UserData> getUserData() throws IOException {
        return userDataDao.getUserData();
    }

    @Override
    public List<UserGroupData> getUserDataGroupedByGroup() throws IOException {
        List<UserData> userDataList = userDataDao.getUserData();
        Map<String, UserGroupData> userGroupDataMap = new HashMap<>();
        for (UserData userData : userDataList) {
            if (!CollectionUtils.isEmpty(userData.getGroups())) {
                for (UserGroupData userGroupData : userData.getGroups()) {
                    UserGroupData userGroupDataInMap = userGroupDataMap.get(userGroupData.getGroupArn());
                    if (userGroupDataInMap == null) {
                        userGroupDataInMap = userGroupData;
                        userGroupDataMap.put(userGroupData.getGroupArn(), userGroupData);
                    }
                    List<UserData> userDataListInGroup = userGroupDataInMap.getUserDataList();
                    if (userDataListInGroup == null) {
                        userDataListInGroup = new ArrayList<>();
                        userGroupDataInMap.setUserDataList(userDataListInGroup);
                    }
                    userDataListInGroup.add(userData);
                }
                userData.setGroups(null);
            }
        }

        return new ArrayList<>(userGroupDataMap.values());
    }
}
