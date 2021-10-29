package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;

/***
 * 
 * [JUnit�� �ϳ��� �׽�Ʈ Ŭ������ ������ �׽�Ʈ�� �����ϴ� ���]
 * 1. �׽�Ʈ Ŭ�������� @Test�� ���� public && void && �Ķ���� ���� �׽�Ʈ �޼ҵ带 ��� ã�´�.
 * 2. �׽�Ʈ Ŭ������ ������Ʈ�� �ϳ� �����.
 * 3. @Before�� ���� �޼ҵ尡 ������ �����Ѵ�.
 * 4. @Test�� ���� �޼ҵ带 �ϳ� ȣ���ϰ� �׽�Ʈ ����� �����صд�.
 * 5. @After�� ���� �޼ҵ尡 ������ �����Ѵ�.
 * 6. ������ �׽�Ʈ �޼ҵ忡 ���� 2~5 ������ �ݺ��Ѵ�.
 * 7. ��� �׽�Ʈ�� ����� �����ؼ� �����ش�. 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)						// �������� �׽�Ʈ ���ؽ�Ʈ �����ӿ�ũ�� JUnit Ȯ���� ����
@ContextConfiguration(locations="/applicationContext.xml")	// �׽�Ʈ ���ؽ�Ʈ�� �ڵ����� ����� �� ���ø����̼� ���ؽ�Ʈ�� ��ġ ����
public class UserDAOTest {
//	@Autowired
//	private ApplicationContext context;
	
	// �Ƚ�ó : �׽�Ʈ�� �����ϴ� �� �ʿ��� ������ ������Ʈ
	// �ݺ������� ���Ǳ� ������ @Before �޼ҵ带 �̿��� �����صд�.
	@Autowired
	private UserDAO dao;
	
	@Autowired
	DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		//System.out.println(this.context);	// �׻� ����
		//System.out.println(this);			// �Ź� �ٸ�
		
		//this.dao = context.getBean("userDAO", UserDAO.class);
		this.user1 = new User("chaeeun1", "��ä��1", "dbscodms1", Level.BASIC, 1, 0);
		this.user2 = new User("chaeeun2", "��ä��2", "dbscodms2", Level.SILVER, 55, 10);
		this.user3 = new User("chaeeun3", "��ä��3", "dbscodms3", Level.GOLD, 100, 40);
	}
	
	// JUnit���� �׽�Ʈ �޼ҵ����� �˸�
	@Test
	public void addAndGet() throws SQLException{	// JUnit �޼ҵ�� �ݵ�� public���� ����
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
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
	
	@Test
	public void getAll() throws SQLException {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));

		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0));
		checkSameUser(user2, users3.get(1));
		checkSameUser(user3, users3.get(2));
	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void duplicateKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}
	
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		}catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)ex.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			
			assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
		}
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
