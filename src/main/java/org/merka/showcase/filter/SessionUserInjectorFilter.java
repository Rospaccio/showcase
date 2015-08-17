package org.merka.showcase.filter;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.merka.showcase.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SessionUserInjectorFilter extends OncePerRequestFilter implements
		Filter {

	public static final String KEY_CURRENT_USER = "CURRENT_USER";

	private static final Logger logger = LoggerFactory.getLogger(SessionUserInjectorFilter.class);
	
	@Autowired
	EntityManagerFactory entityManagerFactory;

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try
		{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(request instanceof HttpServletRequest && auth != null
					&& !auth.getName().equals("anonymousUser"))
			{
				EntityManager manager = entityManagerFactory.createEntityManager();
				User user = null;
				try
				{
					user = manager.createQuery("select u from User u where username = :username", User.class)
							.setParameter("username", auth.getName()).getSingleResult();
				}
				catch(NoResultException ne)
				{
					logger.warn("User '" + auth.getName() + "' not found in application database", ne);
				}
				if(user != null)
				{
					((HttpServletRequest) request).getSession().setAttribute(KEY_CURRENT_USER, user);
					
//					// add the user in the spring scoped attributes
//					RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
//					attributes.setAttribute("user", user, RequestAttributes.SCOPE_REQUEST);
				}
			}
		}
		catch(Exception e)
		{
			logger.error("Error executin doFilterInternal", e);
		}
		finally
		{
			filterChain.doFilter(request, response);
		}
		
	}

}
