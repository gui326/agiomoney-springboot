package gg.agiomoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import gg.agiomoney.model.Client;
import gg.agiomoney.repository.ClientRepository;
import gg.agiomoney.service.ClientService;

@Controller
public class ClientController {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientService clientService;
	
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	@GetMapping("/client/login")
	public ModelAndView loginClient() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/client/loginClient");
		logger.trace("Encaminhando para a view index");
		return mv;
	}
	
	@GetMapping("/client/register")
	public ModelAndView registerClient() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/client/registerClient");
		mv.addObject("client", new Client());
		return mv;
	}
	
	@PostMapping("/client/register")
	public ModelAndView registeredClient(Client client) {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("mensagem");
		
		clientService.saveClient(client);
		
		mv.addObject("mensagem", "Cadastro Realizado com sucesso!, bem-vindo " + client.getName());
		
		return mv;
	}
	
	@GetMapping("/client/home")
	public ModelAndView homeClient() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/client/areaClient");
		logger.trace("Encaminhando para a view index");
		return mv;
	}

}
