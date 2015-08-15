package org.merka.showcase.service;

import org.merka.showcase.entity.User;

public class UserServiceUtils
{
	private static UserService service;
	
	private void setStaticServiceInstance(UserService service){
		UserServiceUtils.service = service;
	}
	
	public UserServiceUtils(UserService service)
	{
		setStaticServiceInstance(service);
	}
	
	public static void save(User user)
	{
		service.save(user);
	}
}
