package gg.agiomoney.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import gg.agiomoney.model.Client;
import gg.agiomoney.model.Company;
import gg.agiomoney.model.Loan;
import gg.agiomoney.repository.ClientRepository;
import gg.agiomoney.repository.CompanyRepository;
import gg.agiomoney.repository.LoanRepository;
import gg.agiomoney.service.ClientService;
import gg.agiomoney.service.LoanService;

@Controller
public class ClientController {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private LoanService loanService;
	
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
	
	@GetMapping("/client/loan")
	public ModelAndView clientLoan() {
		ModelAndView mv = new ModelAndView("/client/loanClient");
		return mv;
	}
	
	@PostMapping("/client/loan")
	public ModelAndView clientLoans(Double value, int installments) {
		ModelAndView mv = new ModelAndView("/client/loanClient");
		
		List<Company> companies = companyRepository.findAll();
		List<Loan> loans = new ArrayList<Loan>();
		
		Long clientId = (long) 1; 
		
		Optional<Client> client = clientRepository.findById(clientId);
		
		for(Company company : companies) {
			Loan loan = new Loan();
			loan.setClient(client.get());
			loan.setCompany(company);
			loan.setInstallments(installments);
			loan.setState("Pendente");
			loan.setTotal(company.getTax() * value);
			loans.add(loan);
		}
		
		mv.addObject("loans", loans);
		mv.addObject("value", value);
		mv.addObject("installments", installments);
		
		return mv;
	}
	
	@PostMapping("/client/loan/contract")
	public ModelAndView contractLoan(String companyId, String installments, String total) {
		ModelAndView mv = new ModelAndView("mensagem");
		
		Loan loan = new Loan();
		
		logger.trace("inicio 1111");
		
		loan.setCompany(companyRepository.findById((long) Integer.parseInt(companyId)).get());
		loan.setInstallments(Integer.parseInt(installments));
		loan.setTotal(Double.parseDouble(total));
		loan.setState("Pendente");
		loan.setClient(clientRepository.findById((long) 1).get());
		
		logger.trace("final 1111");
		
		loanService.saveLoan(loan);
		
		mv.addObject("mensagem", "Criado loan");
		
		return mv;
	}
	

}
