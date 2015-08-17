package org.merka.showcase.session;

import org.merka.showcase.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

//@Component
//@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
public class CurrentUserComponent extends User 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6517769777303613013L;

	public CurrentUserComponent()
	{
		setUsername("anonymousUser");
		setPassword("");
		setEnabled(true);
		setId(-1L);
	}
}
