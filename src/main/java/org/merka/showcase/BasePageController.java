package org.merka.showcase;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.merka.showcase.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public abstract class BasePageController {
	private static final Logger logger = LoggerFactory.getLogger(BasePageController.class);

	@Autowired
	EntityManagerFactory entityManagerFactory;

	protected User sessionUser;

	protected User getSessionUser() {
		return sessionUser;
	}

	@ModelAttribute(value = "sessionUser")
	public User initSessionUser(HttpServletRequest request, Model model) 
	{
		User user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (request instanceof HttpServletRequest && auth != null && !auth.getName().equals("anonymousUser")
				&& !request.getRequestURI().equals("/logout")) 
		{
			EntityManager manager = entityManagerFactory.createEntityManager();
			try 
			{
				user = manager.createQuery("select u from User u where username = :username", User.class)
						.setParameter("username", auth.getName()).getSingleResult();
			} 
			catch (NoResultException ne) 
			{
				logger.warn("User '" + auth.getName() + "' not found in application database", ne);
			}
		}

		return user;
	}

	@ModelAttribute("pageTitle")
	public abstract String getPageTitle();
}
