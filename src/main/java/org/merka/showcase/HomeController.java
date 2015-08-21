package org.merka.showcase;

import java.text.DateFormat;
import static org.merka.showcase.utils.ShowcaseUtils.*;
import java.util.Date;
import java.util.Locale;

import org.merka.showcase.listener.StartupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BasePageController{
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	StartupManager startupManager;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return thymeleafViewName("home");
	}

	@RequestMapping("/learn-more")
	public String learnMore()
	{
		return thymeleafViewName("learn-more");
	}
	
	@RequestMapping("/init")
	public String init()
	{
		startupManager.insertDefaultData();
		return thymeleafViewName("home");
	}
	
	@Override
	public String getPageTitle() {
		return "Home Page";
	}
}
