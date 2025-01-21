package ispan.user.service;

import java.util.List;

import ispan.user.model.UserBean;

public interface UserService {
	
	UserBean queryOne(String userID);

	List<UserBean> queryAll();

	UserBean insertAndUpate(UserBean user);

	void delete(String userID);


}
