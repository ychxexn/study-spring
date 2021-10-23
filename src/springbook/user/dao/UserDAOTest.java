package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDAOTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		// @Bean �� ���� �޼ҵ� �̸��� ���� �̸��� �ȴ�.
		// getBean�� Object Ÿ������ �����ϰ� ���־ ���׸� �޼ҵ� ����� ����� ���� Ÿ���� �־� �������� ĳ���� �ڵ带 ������� �ʾҴ�.
		UserDAO da = context.getBean("userDAO", UserDAO.class);
	}

}
