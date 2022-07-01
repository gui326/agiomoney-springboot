package gg.agiomoney.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	@GetMapping("/client/login")
	public String loginClientView(Model model, HttpSession session) {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/client/loginClient");
		
		Client client = (Client) session.getAttribute("client");
		
		if(client == null) {
			logger.trace("Encaminhando para a view index");
			return "/client/loginClient";
		} else {
			return "redirect:/client/home";
		}
	}
	
	@PostMapping("/client/login")
	public String loginClient(Model model, HttpSession session, String email, String password) {
		logger.trace("Entrou em index");
		
		
		Client client = clientRepository.findByEmail(email);
		if(client == null) {
			model.addAttribute("mensagem", "Usuário não encontrado");
			return "/client/loginClient";
		}
		
		Boolean correctPassowrd  = passwordEncoder.matches(password, client.getPassword());
		
		if(!correctPassowrd) {
			model.addAttribute("mensagem", "Senha incorreta");
			return "/client/loginClient";
		}
		
		session.setAttribute("client", client);
		logger.trace("Encaminhando para a view index");
		return "redirect:/client/home";
	}
	
	@GetMapping("/client/logout")
	public ModelAndView logoutClient(HttpSession session) {
		logger.trace("Entrou em index");
		
		session.removeAttribute("client");
		
		ModelAndView mv = new ModelAndView("/index");
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
	public ModelAndView registeredClient(@Valid Client client, BindingResult result) {
		logger.trace("Entrou em index");
		
		if (result.hasErrors()) {
			ModelAndView mv = new ModelAndView("/client/registerClient");
			
			logger.debug("O cliente recebido para inserir não é válido");
			logger.debug("Erros enscontrados:");
			for(FieldError erro : result.getFieldErrors()) {
				logger.debug("{}", erro);
			}
			logger.trace("Encaminhando para a view novocliente");
			
			return mv;
		} else {
			client.setPassword(passwordEncoder.encode(client.getPassword()));
			clientService.saveClient(client);
			ModelAndView mv = new ModelAndView("mensagem");
			mv.addObject("mensagem", "Cadastro Realizado com sucesso!, bem-vindo " + client.getName());
			
			return mv;
		}
		
	}
	
	@GetMapping("/client/home")
	public ModelAndView homeClient(HttpSession session, @PageableDefault(size = 4) Pageable pageable) {
		logger.trace("Entrou em homeClient");
		ModelAndView mv = new ModelAndView("/client/areaClient");
		
		Client client = (Client) session.getAttribute("client");
		
		Page<Loan> pagina = loanRepository.findLoansByClientIdPaginacao(client.getCode(), pageable);
		
		logger.trace("client: {}", client.getCode());
		


		
		// Para um visual mais harmonioso o maximoPaginasMostrar deve ser ímpar
		int maximoPaginasMostrar = 4;
		int metadeMaximoPaginasMostrar = maximoPaginasMostrar / 2;
		int totalDePaginas = pagina.getTotalPages();
		int paginaAtual = pagina.getNumber() + 1;
		int inicio;
		int fim;

		if (totalDePaginas <= maximoPaginasMostrar) {
			inicio = 1;
			fim = totalDePaginas;
		} else {
			if (paginaAtual <= metadeMaximoPaginasMostrar + 1) {
				inicio = 1;
				fim = maximoPaginasMostrar;
			} else {
				inicio = paginaAtual - metadeMaximoPaginasMostrar;
				if (paginaAtual + metadeMaximoPaginasMostrar <= totalDePaginas) {
					fim = paginaAtual + metadeMaximoPaginasMostrar;
				} else {
					fim = totalDePaginas;
					inicio = totalDePaginas - maximoPaginasMostrar + 1;
				}
			}
		}
		List<Integer> numerosPaginas = new ArrayList<>();
		for (int cont = inicio; cont <= fim; cont++) {
			numerosPaginas.add(cont);
		}
		
		mv.addObject("numerosPaginas", numerosPaginas);
		mv.addObject("pagina", pagina);

		
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
		
		if(value != null) {
			if(value > 0) {
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
			} else {
				mv.addObject("error", "valor precisa ser maior que 0");
			}
		} else {
			mv.addObject("error", "valor necessário");
		}
		
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
