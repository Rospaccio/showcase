package org.merka.showcase.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.merka.showcase.utils.ShowcaseUtils.*;

@Controller
public class TestPageController 
{
	@RequestMapping(path = "/probe")
	public String getTestPage()
	{
		return thymeleafViewName("probe");
	}
}
