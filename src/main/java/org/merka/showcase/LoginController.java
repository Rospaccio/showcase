package org.merka.showcase;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController
{
	private static final Logger	logger	= LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/loginPage")
	public String loginPage(Model model, @RequestParam(value = "fail", required = false) String fail)
	{
		if (fail != null)
		{
			logger.info("Login failed");
			model.addAttribute("errorMessage", "Authentication failed: please check username and password");
		}
		return "loginPage";
	}

//	@RequestMapping(value = "/loginPage?fail")
//	public String failedLogin(Locale locale, Model model)
//	{
//
//		return "loginPage";
//	}
}
