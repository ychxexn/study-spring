package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDAOTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		// @Bean 이 붙은 메소드 이름이 빈의 이름이 된다.
		// getBean은 Object 타입으로 리턴하게 돼있어서 제네릭 메소드 방식을 사용해 리턴 타입을 주어 지저분한 캐스팅 코드를 사용하지 않았다.
		UserDAO da = context.getBean("userDAO", UserDAO.class);
	}

}
