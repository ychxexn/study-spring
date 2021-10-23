package springbook.user.dao;

public class DaoFactory {

	public UserDAO userDAO() {
		return new UserDAO(connectionMaker());
	}
	
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}

}
