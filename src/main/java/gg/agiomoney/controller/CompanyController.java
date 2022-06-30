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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import gg.agiomoney.model.Company;
import gg.agiomoney.model.Loan;
import gg.agiomoney.repository.CompanyRepository;
import gg.agiomoney.repository.LoanRepository;
import gg.agiomoney.service.CompanyService;
import gg.agiomoney.service.LoanService;

@Controller
public class CompanyController {
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private LoanService loanService;
	
	@GetMapping("/company/login")
	public String loginCompanyView(HttpSession session, Model model) {
		
		Company company = (Company) session.getAttribute("company");
		
		if(company == null) {
			logger.trace("Encaminhando para a view index");
			return "/company/loginCompany";
		} else {
			return "redirect:/company/home";
		}
	}
	
	@PostMapping("/company/login")
	public String loginCompany(Model model, HttpSession session, String email, String password) {
		Company company = companyRepository.findByEmail(email);
		if(company == null) {
			model.addAttribute("mensagem", "Usuário não encontrado");
			return "/company/loginCompany";
		}
		
		if(!company.getPassword().equals(password)) {
			model.addAttribute("mensagem", "Senha incorreta");
			return "/company/loginCompany";
		}
		
		session.setAttribute("company", company);
		logger.trace("Encaminhando para a view index");
		return "redirect:/company/home";
	}
	
	@GetMapping("/company/logout")
	public ModelAndView logoutCompany(HttpSession session) {
		logger.trace("Entrou em index");
		
		session.removeAttribute("company");
		
		ModelAndView mv = new ModelAndView("/index");
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
	public ModelAndView registeredCompany(@Valid Company company, BindingResult result) {

		if (result.hasErrors()) {
			ModelAndView mv = new ModelAndView("/company/registerCompany");
			
			logger.debug("A empresa recebido para inserir não é válido");
			logger.debug("Erros encontrados:");
			for(FieldError erro : result.getFieldErrors()) {
				logger.debug("{}", erro);
			}
			logger.trace("Encaminhando para a view novocliente");
			
			return mv;
		} else {
			companyService.saveCompany(company);
			ModelAndView mv = new ModelAndView("mensagem");
			mv.addObject("mensagem", "Cadastro foi realizado com sucesso!, bem-vindo " + company.getName());
			
			return mv;
		}
	}
	
	@GetMapping("/company/home")
	public ModelAndView homeCompany(HttpSession session, @PageableDefault(size = 2) Pageable pageable) {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/company/areaCompany");
		
		Company company = (Company) session.getAttribute("company");
		
		Page<Loan> pagina = loanRepository.findLoansByCompanyIdPaginacao(company.getCode(), pageable);
		
		
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
				
		mv.addObject("company", company);
		
		return mv;
	}
	
	@PostMapping("/company/loan/accept")
	public String companyAcceptLoan(String codeLoan) {
		Optional<Loan> op = loanRepository.findById((long) Integer.parseInt(codeLoan));
		Loan loan = op.get();
		
		loan.setState("Finalizado");
		
		loanService.saveLoan(loan);
		
		return "redirect:/company/home";
	}
	
	@PostMapping("/company/loan/proposal")
	public String companyProposalLoan(String codeLoan, Double newTotal, RedirectAttributes atributos) {
		Optional<Loan> op = loanRepository.findById((long) Integer.parseInt(codeLoan));
		Loan loan = op.get();
		
		if(newTotal != null) {
		
			loan.setState("Contraproposta");
			loan.setTotal(newTotal);
			
			loanService.saveLoan(loan);
		
		} else {
			atributos.addFlashAttribute("mensagemError", "Valor necessário!");
		}
		
		return "redirect:/company/home";
	}

}
