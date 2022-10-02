package com.aquib.cy5assignment.web.controller;

import com.aquib.cy5assignment.web.entities.UserData;
import com.aquib.cy5assignment.web.entities.UserGroupData;
import com.aquib.cy5assignment.web.userdataservice.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Mycontroller {
    @Autowired
    private UserDataService userDataService;

    @GetMapping("/users")
    public List<UserData> fetchUserData() throws IOException {
       return userDataService.getUserData();
    }

    @GetMapping("/groups")
    public List<UserGroupData> fetchUserDataByGroups() throws IOException {
        return userDataService.getUserDataGroupedByGroup();
    }

}
