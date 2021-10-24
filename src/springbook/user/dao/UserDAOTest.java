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

import springbook.user.domain.User;

public class UserDAOTest {
	
	// JUnit에게 테스트 메소드임을 알림
	@Test
	public void addAndGet() throws SQLException{	// JUnit 메소드는 반드시 public으로 선언
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		User user = new User();
		user.setId("chaeeun");
		user.setName("윤채은");
		user.setPassword("dbscodms");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
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
