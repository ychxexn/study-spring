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
	
	// JUnit���� �׽�Ʈ �޼ҵ����� �˸�
	@Test
	public void addAndGet() throws SQLException{	// JUnit �޼ҵ�� �ݵ�� public���� ����
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
		User user1 = new User("chaeeun1", "��ä��1", "dbscodms1");
		User user2 = new User("chaeeun2", "��ä��2", "dbscodms2");
		
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
		
		User user1 = new User("chaeeun1", "��ä��1", "dbscodms1");
		User user2 = new User("chaeeun2", "��ä��2", "dbscodms2");
		User user3 = new User("chaeeun3", "��ä��3", "dbscodms3");
		
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
		// �ڹ� �ڵ� ���
		//ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		// XML �������� ���
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		// @Bean �� ���� �޼ҵ� �̸��� ���� �̸��� �ȴ�.
		// getBean�� Object Ÿ������ �����ϰ� ���־ ���׸� �޼ҵ� ����� ����� ���� Ÿ���� �־� �������� ĳ���� �ڵ带 ������� �ʾҴ�.
		UserDAO da = context.getBean("userDAO", UserDAO.class);
		*/
		
		// eclipse IDE ���� �����ϴ� JUnit �׽�Ʈ�� ����ϸ� main �޼ҵ带 ȣ������ �ʾƵ� �ȴ�.
		//JUnitCore.main("springbook.user.dao.UserDAOTest");
	}

}
