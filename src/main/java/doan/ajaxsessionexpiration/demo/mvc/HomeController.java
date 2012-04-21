package doan.ajaxsessionexpiration.demo.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import doan.ajaxsessionexpiration.demo.domain.User;

@Controller
public class HomeController
{
	private User ddoan = new User("ddoan", "DuyHai", "DOAN");

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model)
	{
		return "pages/login";
	}

	@RequestMapping(value =
	{
			"/",
			"/home"
	}, method = RequestMethod.GET)
	public String home()
	{
		return "pages/home";
	}

	@RequestMapping(value = "/rest/check", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public User rest()
	{
		return ddoan;
	}
}
