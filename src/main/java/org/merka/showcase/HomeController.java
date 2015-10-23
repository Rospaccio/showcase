package org.merka.showcase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import static org.merka.showcase.utils.ShowcaseUtils.*;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BufferedHeader;
import org.merka.showcase.listener.StartupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@RequestMapping("/pentaho-autologin")
	public View pentahoAutologin() throws ClientProtocolException, IOException
	{
		return pentahoAutologin("http://localhost:8080/pentaho/Home?dummy=true");
	}
	
	@RequestMapping("/pentaho-autologin-plugin")
	public View pentahoAutologinPlugin() throws ClientProtocolException, IOException
	{
		return pentahoAutologin("http://localhost:8080/pentaho/plugin/jpivot/Pivot"
				+ "?solution=&path=/public/Steel%20Wheels/Analysis/2005%20Q1%20Product%20Analysis.xjpivot.xjpivot&action=2005%20Q1%20Product%20Analysis.xjpivot.xjpivot");
		// Gets a ticket from Pentaho
		
//		HttpClient client = HttpClientBuilder.create().build();
//		HttpGet get = new HttpGet("http://localhost:8080/pentaho/Login?generate-ticket=1&app=showcase&username=user0.2");
//		HttpResponse response = client.execute(get);
//		InputStream responseStream = response.getEntity().getContent();
//		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
//		String firstLine = reader.readLine();
//		
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode node = mapper.readTree(firstLine);
//		String ticketId = node.get("ticketId").asText();
//		
////		 RedirectView redirectVSiew = new RedirectView("http://localhost:8080/pentaho/Home?autologin=true&ticket=" + ticketId);
//		RedirectView redirectView = new RedirectView("http://localhost:8080/pentaho/plugin/jpivot/Pivot"
//				+ "?autologin=true&ticket=" + ticketId
//				+ "&solution=&path=/public/Steel%20Wheels/Analysis/2005%20Q1%20Product%20Analysis.xjpivot.xjpivot&action=2005%20Q1%20Product%20Analysis.xjpivot.xjpivot");
//		return redirectView;
	}
	
	@RequestMapping("/pentaho-autologin-api")
	public View pentahoAutologinApi() throws ClientProtocolException, IOException
	{
		return pentahoAutologin("http://localhost:8080/pentaho/api/repo/files/%3A/tree?depth=-1&showHidden=false&filter=*|FOLDERS&_=1443273419486");
	}
	
	
	private View pentahoAutologin(String targetUrl) throws ClientProtocolException, IOException
	{
		// Gets a ticket from Pentaho
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet("http://localhost:8080/pentaho/Login?generate-ticket=1&app=showcase&username=user0.2&userid=admin&password=password");
		HttpResponse response = client.execute(get);
		InputStream responseStream = response.getEntity().getContent();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
		String firstLine = reader.readLine();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(firstLine);
		String ticketId = node.get("ticketId").asText();
		
//		 RedirectView redirectVSiew = new RedirectView("http://localhost:8080/pentaho/Home?autologin=true&ticket=" + ticketId);
		RedirectView redirectView = new RedirectView(targetUrl + "&autologin=true&ticket=" + ticketId);
		return redirectView;
	}
	
	@Override
	public String getPageTitle() {
		return "Home Page";
	}
}
