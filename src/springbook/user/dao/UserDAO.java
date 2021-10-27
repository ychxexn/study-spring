package springbook.user.dao;

import java.util.List;
import springbook.user.domain.User;

public interface UserDAO {
	public void add(User user);
	public User get(String id);
	public List<User> getAll();
	public void deleteAll();
	public int getCount();
}
