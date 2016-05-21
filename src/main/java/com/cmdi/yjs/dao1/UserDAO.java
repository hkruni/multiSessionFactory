package com.cmdi.yjs.dao1;



import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cmdi.yjs.po.User;

public interface UserDAO {
	
	public List<User> getUser(@Param(value="username") String username);
	
	public List<User> getUserByName(@Param(value="username") String username);
	
	public int addUser(User user);
}
