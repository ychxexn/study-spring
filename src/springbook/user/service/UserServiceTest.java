package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDAO;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDAO userDAO;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("chaeeun", "À±Ã¤Àº", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("chaeeun2", "À±Ã¤Àº2", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("chaeeun3", "À±Ã¤Àº3", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
			new User("chaeeun4", "À±Ã¤Àº4", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
			new User("chaeeun5", "À±Ã¤Àº5", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
		);
	}
	
	@Test
	public void upgradeLevels() {
		userDAO.deleteAll();
		
		for(User user : users) {
			userDAO.add(user);
		}
		
		userService.upgradeLevels();
		
		checkLevlUpgraded(users.get(0), false);
		checkLevlUpgraded(users.get(1), true);
		checkLevlUpgraded(users.get(2), false);
		checkLevlUpgraded(users.get(3), true);
		checkLevlUpgraded(users.get(4), false);
	}
	
	@Test
	public void add() {
		userDAO.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDAO.get(userWithLevel.getId());
		User userWithoutLevelRead = userDAO.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	private void checkLevlUpgraded(User user, boolean upgraded) {
		User userUpdate = userDAO.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
}
