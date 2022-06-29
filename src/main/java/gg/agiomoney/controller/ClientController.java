package gg.agiomoney.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public ModelAndView loginClientView() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/client/loginClient");
		logger.trace("Encaminhando para a view index");
		return mv;
	}
	
	@PostMapping("/client/login")
	public String loginClient(Model model, HttpSession session, String email, String password) {
		logger.trace("Entrou em index");
		
		
		Client client = clientRepository.findByEmail(email);
		if(client == null) {
			model.addAttribute("mensagem", "Usuário não encontrado");
			return "/client/loginClient";
		}
		
		logger.trace("senha: {}", client.getPassword());
		logger.trace("senha: {}", password);
		
		if(!client.getPassword().equals(password)) {
			model.addAttribute("mensagem", "Senha incorreta");
			return "/client/loginClient";
		}
		
		session.setAttribute("client", client);
		logger.trace("Encaminhando para a view index");
		return "redirect:/client/home";
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
	public ModelAndView homeClient(HttpSession session) {
		logger.trace("Entrou em homeClient");
		ModelAndView mv = new ModelAndView("/client/areaClient");
		
		Client client = (Client) session.getAttribute("client");
		
		logger.trace("client: {}", client.getCode());
		
		List<Loan> loans = loanRepository.findByClientCode(client.getCode());
		
		mv.addObject("loans", loans);
		
		mv.addObject("client", client);
		logger.trace("Encaminhando para a view index");
		return mv;
	}
	
	@GetMapping("/client/loan")
	public ModelAndView clientLoan() {
		ModelAndView mv = new ModelAndView("/client/loanClient");
		return mv;
	}
	
	@PostMapping("/client/loan")
	public ModelAndView clientLoans(HttpSession session, Double value, int installments) {
		ModelAndView mv = new ModelAndView("/client/loanClient");
		
		List<Company> companies = companyRepository.findAll();
		List<Loan> loans = new ArrayList<Loan>();
		
		Client clientSession = (Client) session.getAttribute("client");
		
		Optional<Client> client = clientRepository.findById(clientSession.getCode());
		
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
	public String contractLoan(HttpSession session, String companyId, String installments, String total) {
		Loan loan = new Loan();
		
		Client clientSession = (Client) session.getAttribute("client");
		
		loan.setCompany(companyRepository.findById((long) Integer.parseInt(companyId)).get());
		loan.setInstallments(Integer.parseInt(installments));
		loan.setTotal(Double.parseDouble(total));
		loan.setState("Pendente");
		loan.setClient(clientSession);
		
		loanService.saveLoan(loan);
		
		return "redirect:/client/home";
	}
	
	@PostMapping("/client/loan/update")
	public String clientUpdateLoan(String codeLoan) {
		Optional<Loan> op = loanRepository.findById((long) Integer.parseInt(codeLoan));
		Loan loan = op.get();
		
		loan.setState("Finalizado");
		
		loanService.saveLoan(loan);
		
		return "redirect:/client/home";
	}
	

}
