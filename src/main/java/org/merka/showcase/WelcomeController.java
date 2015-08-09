package org.merka.showcase;

import java.util.Locale;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController
{
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String signIn(Locale locale, Model model)
	{
		model.addAttribute("message", "Welcome back!");
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("usernameLinkText", username);
		return "welcome";
	}
}
