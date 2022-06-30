package gg.agiomoney.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import gg.agiomoney.model.Client;
import gg.agiomoney.model.Company;

@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@GetMapping(value = {"/", "/index.html"} )
	public ModelAndView index(HttpSession session) {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("index");
		
		Client client = (Client) session.getAttribute("client");
		if(client != null) {
			mv.addObject("client", client);
		}
		
		Company company = (Company) session.getAttribute("company");
		if(client != null) {
			mv.addObject("company", company);
		}
		
		logger.trace("Encaminhando para a view index");
		return mv;
	}
	
}