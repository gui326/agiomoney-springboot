package gg.agiomoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import gg.agiomoney.model.Company;
import gg.agiomoney.repository.CompanyRepository;
import gg.agiomoney.service.CompanyService;

@Controller
public class CompanyController {
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private CompanyService companyService;
	
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
		mv.addObject("company", new Company());
		return mv;
	}
	
	@PostMapping("/company/register")
	public ModelAndView registeredCompany(Company company) {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("mensagem");
		mv.addObject("mensagem", "Cadastro realizado com sucesso!, bem-vindo " + company.getName());
		
		companyService.saveCompany(company);
		
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
