package org.merka.showcase.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/user/ranks/**")
public class MyRanksController extends BasePageController
{
	@RequestMapping(value = "/")
	public String ranks(){
		return "user/ranks";
	}
}
