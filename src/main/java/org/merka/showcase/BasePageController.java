package org.merka.showcase;

import javax.servlet.http.HttpServletRequest;

import org.merka.showcase.entity.User;
import org.merka.showcase.filter.SessionUserInjectorFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public abstract class BasePageController 
{

	@ModelAttribute(value = "sessionUser")
	public User sessionUser(HttpServletRequest request)
	{
		User sessionUser = (User)request.getSession().getAttribute(SessionUserInjectorFilter.KEY_CURRENT_USER);
		return sessionUser;
	}
	
	@ModelAttribute("pageTitle")
	public abstract String getPageTitle();
}
