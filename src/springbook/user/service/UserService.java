package springbook.user.service;

import java.util.List;

import springbook.user.dao.UserDAO;
import springbook.user.domain.Level;
import springbook.user.domain.User;


public class UserService {
	UserDAO userDAO;
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void upgradeLevels() {
		List<User> users = userDAO.getAll();
		
		for(User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}

	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		
		switch(currentLevel) {
			case BASIC:
				return (user.getLogin() >= 50);
			case SILVER:
				return (user.getRecommend() >= 30);
			case GOLD:
				return false;
			default:
				throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}
	
	private void upgradeLevel(User user) {
		user.upgradeLevel();
		userDAO.update(user);
	}

	public void add(User user) {
		if(user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}
		userDAO.add(user);
	}
}
