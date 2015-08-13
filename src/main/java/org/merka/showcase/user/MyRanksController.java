package org.merka.showcase.user;

import static org.merka.showcase.utils.ShowcaseUtils.thymeleafViewName;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.merka.showcase.BasePageController;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/user/ranks/**")
public class MyRanksController extends BasePageController
{
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MyRanksController.class);
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@ModelAttribute(value = "newRank")
	public Rank newRank(@ModelAttribute("user") User user)
	{
		Rank rank = Rank.create("", "");
		return rank;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String putNewRank(
			Model model, 
			@ModelAttribute("newRank") Rank newRank,
			@ModelAttribute("user") User user)
	{
		logger.info("Rank: " + newRank.getName() + ", " + newRank.getDescription());
		
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		User freshUser = manager.find(User.class, user.getId());
		
		freshUser.getRanks().add(newRank);
		newRank.setOwner(freshUser);
		manager.getTransaction().begin();
		manager.persist(freshUser);
		manager.persist(newRank);
		manager.getTransaction().commit();
		
		List<Rank> updatedRanks = ranks(user);
		model.addAttribute("ranks", updatedRanks);
		
		return thymeleafViewName("user/ranks");
	}
	
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public String deleteRank(
			Model model,
			@RequestParam(name = "id") String rankId,
			@ModelAttribute("user") User user)
	{
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		Long id = Long.parseLong(rankId);
		manager.getTransaction().begin();
		Rank toDelete = manager.find(Rank.class, id);
		manager.remove(toDelete);
		manager.getTransaction().commit();
		
		List<Rank> updatedRanks = ranks(user);
		model.addAttribute("ranks", updatedRanks);
		
		return thymeleafViewName("user/ranks");
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getRanks(Model model){
		
		String view = thymeleafViewName("user/ranks");
		return new ModelAndView(view, "command", model);
	}
	
	@ModelAttribute("user")
	private User user()
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		EntityManager manager = entityManagerFactory.createEntityManager();
		User user = manager.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username).getResultList().get(0);
		manager.close();
		return user;
	}
	
	@ModelAttribute(value = "ranks")
	private List<Rank> ranks(@ModelAttribute User user)
	{
		// retrieves all the ranks of the user
		// all in line, I know it's ugly and evil but this is just a trial
		
		EntityManager manager = entityManagerFactory.createEntityManager();
		List<Rank> results = manager.createQuery("SELECT r FROM Rank r where r.owner = :user", Rank.class).setParameter("user", user) .getResultList();
		manager.close();
		return results;
	}
}
