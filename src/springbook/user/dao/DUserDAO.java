package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import springbook.user.domain.User;

public class DUserDAO extends UserDAO {

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		// D�� DB �����ڵ�
		return null;
	}

}
