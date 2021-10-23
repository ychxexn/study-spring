package springbook.user.dao;

import java.sql.SQLException;

public class UserDAOTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		UserDAO dao = new DaoFactory().userDAO();
	}

}
