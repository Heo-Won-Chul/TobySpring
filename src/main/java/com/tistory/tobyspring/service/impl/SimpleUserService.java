package com.tistory.tobyspring.service.impl;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.service.UserLevelUpgradePolicy;
import com.tistory.tobyspring.service.UserService;

import java.util.List;

/**
 * Service <BR>
 * 일반적인 유저 비즈니스 로직 처리 클래스 <BR>
 */
public class SimpleUserService implements UserService {

    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    @Override
    public void upgradeLevels() {
        List<User> userList = userDao.getAll();

        for (User user : userList) {
            if (userLevelUpgradePolicy.isUpgradeLevel(user)) {
                userLevelUpgradePolicy.upgradeLevel(user);
            }
        }
    }

    @Override
    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}