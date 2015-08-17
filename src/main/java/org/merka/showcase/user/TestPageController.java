package org.merka.showcase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.merka.showcase.utils.ShowcaseUtils.*;

import java.util.Locale;

import org.merka.showcase.BasePageController;

@Controller
public class TestPageController extends BasePageController
{
	@Autowired
	MessageSource messageSource;
	
	@ModelAttribute("testMessage")
	public String getTestMessage(Locale locale)
	{
		return messageSource.getMessage("test.message", null, locale);
	}
	
	@RequestMapping(path = "/probe")
	public String getTestPage()
	{
		return thymeleafViewName("probe");
	}

	@Override
	public String getPageTitle() {
		return "Test Page";
	}
}
