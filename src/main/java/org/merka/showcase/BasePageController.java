package org.merka.showcase;

import javax.servlet.http.HttpServletRequest;

import org.merka.showcase.entity.User;
import org.merka.showcase.filter.SessionUserInjectorFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public abstract class BasePageController 
{

	protected User sessionUser;
	
	protected User getSessionUser() {
		return sessionUser;
	}

	@ModelAttribute(value = "sessionUser")
	public User initSessionUser(HttpServletRequest request, Model model)
	{
		User sessionUser = (User)request.getSession().getAttribute(SessionUserInjectorFilter.KEY_CURRENT_USER);
		this.sessionUser = sessionUser; 
		return sessionUser;
	}
	
	@ModelAttribute("pageTitle")
	public abstract String getPageTitle();
}
