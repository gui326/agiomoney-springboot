package gg.agiomoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CompanyController {
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	
	@GetMapping("/company/login")
	public ModelAndView loginCompany() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/company/loginCompany");
		logger.trace("Encaminhando para a view index");
		return mv;
	}
	
	@GetMapping("/company/register")
	public ModelAndView registerCompany() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/company/registerCompany");
		logger.trace("Encaminhando para a view index");
		return mv;
	}
	
	@GetMapping("/company/home")
	public ModelAndView homeCompany() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/company/areaCompany");
		logger.trace("Encaminhando para a view index");
		return mv;
	}

}
