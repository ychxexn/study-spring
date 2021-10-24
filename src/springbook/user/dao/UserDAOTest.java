package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

/***
 * 
 * [JUnit이 하나의 테스트 클래스를 가져와 테스트를 수행하는 방식]
 * 1. 테스트 클래스에서 @Test가 붙은 public && void && 파라미터 없는 테스트 메소드를 모두 찾는다.
 * 2. 테스트 클래스의 오브젝트를 하나 만든다.
 * 3. @Before가 붙은 메소드가 있으면 실행한다.
 * 4. @Test가 붙은 메소드를 하나 호출하고 테스트 결과를 저장해둔다.
 * 5. @After가 붙은 메소드가 있으면 실행한다.
 * 6. 나머지 테스트 메소드에 대해 2~5 과정을 반복한다.
 * 7. 모든 테스트의 결과를 종합해서 돌려준다. 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)						// 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations="/applicationContext.xml")	// 테스트 컨텍스트가 자동으로 만들어 줄 애플리케이션 컨텍스트의 위치 지정
public class UserDAOTest {
//	@Autowired
//	private ApplicationContext context;
	
	// 픽스처 : 테스트를 수행하는 데 필요한 정보나 오브젝트
	// 반복적으로 사용되기 때문에 @Before 메소드를 이용해 생성해둔다.
	@Autowired
	private UserDAO dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		//System.out.println(this.context);	// 항상 같음
		//System.out.println(this);			// 매번 다름
		
		//this.dao = context.getBean("userDAO", UserDAO.class);
		this.user1 = new User("chaeeun1", "윤채은1", "dbscodms1");
		this.user2 = new User("chaeeun2", "윤채은2", "dbscodms2");
		this.user3 = new User("chaeeun3", "윤채은3", "dbscodms3");
	}
	
	// JUnit에게 테스트 메소드임을 알림
	@Test
	public void addAndGet() throws SQLException{	// JUnit 메소드는 반드시 public으로 선언
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
