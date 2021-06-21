package com.temzu.datamapper.data;

import com.temzu.datamapper.model.User;

import java.util.HashMap;
import java.util.Map;

public class IdentityUserMap {

    private Map<Long, User> userMap = new HashMap();

    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }

    public User getUser(Long key) {
        return userMap.get(key);
    }

    public void removeUser(Long key) {
        userMap.remove(key);
    }
}
