package org.merka.showcase.user;

import static org.merka.showcase.utils.ShowcaseUtils.thymeleafViewName;

import java.util.List;

import javax.persistence.EntityManager;

import org.merka.showcase.BasePageController;
import org.merka.showcase.StartupManager;
import org.merka.showcase.entity.Rank;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/user/ranks/**")
public class MyRanksController extends BasePageController
{
	@SuppressWarnings("unused")
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MyRanksController.class);
	
	@ModelAttribute(value = "newRank")
	public Rank newRank()
	{
		return Rank.create("", "");
	}
	
	@RequestMapping(value = "/")
	public String ranks(Model model){
		
		// get the current user
		@SuppressWarnings("unused")
		org.springframework.security.core.userdetails.User user = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// retrieves all the ranks of the user
		// all in line, I know it's ugly and evil but this is just a trial
		EntityManager manager = StartupManager.getEntityManagerFactory().createEntityManager();
		
		List<Rank> results = manager.createQuery("SELECT r FROM Rank r", Rank.class).getResultList();
		
		model.addAttribute("ranks", results);
		
		return thymeleafViewName("user/ranks");
	}
}
