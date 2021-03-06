package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDAO;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.proxy.TransactionHandler;
import springbook.user.service.TestUserService.TestUserServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	MailSender mailSender;
	
	@Autowired
	ApplicationContext context;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("chaeeun", "??????", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("chaeeun2", "??????2", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("chaeeun3", "??????3", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
			new User("chaeeun4", "??????4", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
			new User("chaeeun5", "??????5", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
		);
	}
	
	@Test
	@DirtiesContext	// ?????????? DI ?????? ???????? ?????????? ????
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDAO mockUserDAO = new MockUserDAO(this.users);
		userServiceImpl.setUserDAO(mockUserDAO);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userService.upgradeLevels();
		
		List<User> updated = mockUserDAO.getUpdated();
		
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "chaeeun2", Level.SILVER);
		checkUserAndLevel(updated.get(1), "chaeeun4", Level.GOLD);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}
	
	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDAO mockUserDAO = mock(UserDAO.class);
		when(mockUserDAO.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDAO(mockUserDAO);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDAO, times(2)).update(any(User.class));
		verify(mockUserDAO, times(2)).update(any(User.class));
		verify(mockUserDAO).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDAO).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
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
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDAO.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	@DirtiesContext
	public void upgradeAllOrNothing() throws Exception {
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDAO(this.userDAO);
		testUserService.setMailSender(mailSender);
		
		ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService) txProxyFactoryBean.getObject();
		
		userDAO.deleteAll();
		
		for(User user : users) {
			userDAO.add(user);
		}
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e) {
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	static class MockMailSender implements MailSender{
		private List<String> requests = new ArrayList<String>();
		
		public List<String> getRequests(){
			return requests;
		}

		@Override
		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);
		}

		@Override
		public void send(SimpleMailMessage[] mailMessage) throws MailException {
		}
		
	}
	
	static class MockUserDAO implements UserDAO {

		private List<User> users;
		private List<User> updated = new ArrayList<User>();
		
		private MockUserDAO(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated(){
			return this.updated;
		}
		
		@Override
		public List<User> getAll() {
			return this.users;
		}
		
		@Override
		public void update(User user) {
			updated.add(user);
		}
		
		@Override
		public void add(User user) { throw new UnsupportedOperationException(); }
		@Override
		public User get(String id) { throw new UnsupportedOperationException(); }
		@Override
		public void deleteAll() { throw new UnsupportedOperationException(); }
		@Override
		public int getCount() { throw new UnsupportedOperationException(); }
	}
}
