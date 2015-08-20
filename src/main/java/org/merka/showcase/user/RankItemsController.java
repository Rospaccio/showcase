package org.merka.showcase.user;

import static org.merka.showcase.utils.ShowcaseUtils.thymeleafViewName;

import java.util.Locale;

import org.merka.showcase.BasePageController;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.RankItem;
import org.merka.showcase.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = "/user/rank-items")
public class RankItemsController extends BasePageController{

	@Autowired
	RankService rankService;
	
	@Autowired
	MessageSource messageSource;
	
	@ModelAttribute("newRankItem")
	public RankItem newRankItem()
	{
		return RankItem.create("", "");
	}
	
	@RequestMapping(path = "/{rankId}")
	public ModelAndView getRankItems(
			@PathVariable(value = "rankId") String rankId,
			Model model,
			Locale locale)
	{
		if(rankId.equals("favicon"))
		{
			RedirectView redirection = new RedirectView("/favicon.ico");
			return new ModelAndView(redirection);
		}
		if(model.asMap().get("rank") == null)
		{
			try
			{
				Long id = Long.parseLong(rankId, 10);
				setCurrentRankInModel(id, model);
			}
			catch(NumberFormatException ne)
			{
				model.addAttribute("rankNotFoundError", 
						messageSource.getMessage("rank.not.found.message", null, "Rank not found", locale));
			}
		}
		return new ModelAndView(thymeleafViewName("/user/rank-items"), "command", model);
	}
	
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public View appendNewRankItem(@ModelAttribute("newRankItem") RankItem rankItem
			, @ModelAttribute("rank") Rank rank)
	{
		rank.appendRankItem(rankItem);
		rankService.save(rank);
		return new RedirectView("/user/rank-items" + rank.getId());
	}

	private void setCurrentRankInModel(Long rankId, Model model)
	{
		Rank currentRank = rankService.findById(rankId);
		model.addAttribute("rank", currentRank);
	}
	
	@Override
	public String getPageTitle() {
		return "Rank Items";
	}
	
}
