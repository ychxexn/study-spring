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
			Boolean changed = null;
			
			if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
				user.setLevel(Level.SILVER);
				changed = true;
			}else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
				user.setLevel(Level.GOLD);
				changed = true;
			}else if(user.getLevel() == Level.GOLD) {
				changed = false;
			}else {
				changed = false;
			}
			
			if(changed) {
				userDAO.update(user);
			}
		}
	}

	public void add(User user) {
		if(user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}
		userDAO.add(user);
	}
}
