package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDAOTest {
	
	// JUnit에게 테스트 메소드임을 알림
	@Test
	public void addAndGet() throws SQLException{	// JUnit 메소드는 반드시 public으로 선언
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
		User user1 = new User("chaeeun1", "윤채은1", "dbscodms1");
		User user2 = new User("chaeeun2", "윤채은2", "dbscodms2");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}
	
	@Test
	public void count() throws SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
		User user1 = new User("chaeeun1", "윤채은1", "dbscodms1");
		User user2 = new User("chaeeun2", "윤채은2", "dbscodms2");
		User user3 = new User("chaeeun3", "윤채은3", "dbscodms3");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException{
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
	}
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		/*
		// 자바 코드 사용
		//ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		// XML 설정파일 사용
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		// @Bean 이 붙은 메소드 이름이 빈의 이름이 된다.
		// getBean은 Object 타입으로 리턴하게 돼있어서 제네릭 메소드 방식을 사용해 리턴 타입을 주어 지저분한 캐스팅 코드를 사용하지 않았다.
		UserDAO da = context.getBean("userDAO", UserDAO.class);
		*/
		
		// eclipse IDE 에서 제공하는 JUnit 테스트를 사용하면 main 메소드를 호출하지 않아도 된다.
		//JUnitCore.main("springbook.user.dao.UserDAOTest");
	}

}
