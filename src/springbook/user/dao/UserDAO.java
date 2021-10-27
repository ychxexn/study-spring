package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mysql.jdbc.MysqlErrorNumbers;

import springbook.user.domain.User;
import springbook.user.exception.DuplicateUserIdException;

public class UserDAO {

	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				
				return user;
			}
		};
		
	private JdbcTemplate jdbcTemplate;
	
	public UserDAO() {
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	public void add(User user) throws DuplicateUserIdException {
		try {
			this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)"
					, user.getId(), user.getName(), user.getPassword());
			// 컴파일 에러를 피하기 위해 넣음
			throw new SQLException();
		}catch(SQLException e) {
			if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
				throw new DuplicateUserIdException(e);	// 예외 전환
			}else {
				throw new RuntimeException(e);			// 예외 포장
			}
		}
	}
	
	public User get(String id) throws SQLException {
		return this.jdbcTemplate.queryForObject("select * from users where id=?", new Object[] {id}, this.userMapper);
	}
	
	public void deleteAll() throws SQLException{
		this.jdbcTemplate.update("delete from users");
	}
	
	public int getCount() throws SQLException{
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
	}
}
