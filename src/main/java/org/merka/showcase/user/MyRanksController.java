package org.merka.showcase.user;

import org.merka.showcase.BasePageController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.merka.showcase.utils.ShowcaseUtils.*;

@Controller
@RequestMapping(path = "/user/ranks/**")
public class MyRanksController extends BasePageController
{
	@RequestMapping(value = "/")
	public String ranks(){
		return jspViewName("user/ranks");
	}
}
