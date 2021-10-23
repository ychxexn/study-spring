package springbook.user.dao;

import java.sql.SQLException;

public class UserDAOTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		UserDAO dao = new UserDAO(connectionMaker);
	}

}
