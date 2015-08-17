package org.merka.showcase;

import static org.merka.showcase.utils.ShowcaseUtils.thymeleafViewName;

import java.util.Locale;

import org.merka.showcase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController extends BasePageController
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

		return thymeleafViewName("user/welcome");
	}

	@Override
	public String getPageTitle() {
		return "Welcome Page";
	}
}
