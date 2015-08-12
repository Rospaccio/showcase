package org.merka.showcase.user;

import static org.merka.showcase.utils.ShowcaseUtils.thymeleafViewName;

import java.util.List;

import javax.persistence.EntityManager;

import org.merka.showcase.BasePageController;
import org.merka.showcase.StartupManager;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/user/ranks/**")
public class MyRanksController extends BasePageController
{
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MyRanksController.class);
	
	@ModelAttribute(value = "newRank")
	public Rank newRank()
	{
		return Rank.create("", "");
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String putNewRank(
			Model model, 
			@ModelAttribute("newRank") Rank newRank)
	{
		logger.info("Rank: " + newRank.getName() + ", " + newRank.getDescription());
		return thymeleafViewName("user/ranks");
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView ranks(Model model){
		
		String view = thymeleafViewName("user/ranks");
		return new ModelAndView(view, "command", model);
	}
	
	@ModelAttribute("user")
	private User user()
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		EntityManager manager = StartupManager.getEntityManagerFactory().createEntityManager();
		User user = manager.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username).getResultList().get(0);
		return user;
	}
	
	@ModelAttribute(value = "ranks")
	private List<Rank> ranks(@ModelAttribute User user)
	{
		// retrieves all the ranks of the user
		// all in line, I know it's ugly and evil but this is just a trial
		
		EntityManager manager = StartupManager.getEntityManagerFactory().createEntityManager();
		List<Rank> results = manager.createQuery("SELECT r FROM Rank r where r.owner = :user", Rank.class).setParameter("user", user) .getResultList();
		return results;
	}
}
