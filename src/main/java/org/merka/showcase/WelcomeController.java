package org.merka.showcase;

import static org.merka.showcase.utils.ShowcaseUtils.jspViewName;

import java.util.Locale;

import javax.inject.Inject;

import org.merka.showcase.entity.User;
import org.merka.showcase.service.UserService;
import org.merka.showcase.session.CurrentUserComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = {"sessionUser"})
public class WelcomeController
{
	private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String signIn(Locale locale, Model model)
	{			
		if(SecurityContextHolder.getContext() == null ||
				SecurityContextHolder.getContext().getAuthentication() == null)
		{
			return "redirect:/loginPage?fail";
		}

		model.addAttribute("message", "Welcome back!");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("usernameLinkText", username);
		return jspViewName("welcome");
	}
}
