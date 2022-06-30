package gg.agiomoney.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import gg.agiomoney.model.Client;
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
	public ModelAndView loginCompanyView() {
		logger.trace("Entrou em index");
		ModelAndView mv = new ModelAndView("/company/loginCompany");
		logger.trace("Encaminhando para a view index");
		return mv;
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
	public String companyProposalLoan(String codeLoan, Double newTotal) {
		Optional<Loan> op = loanRepository.findById((long) Integer.parseInt(codeLoan));
		Loan loan = op.get();
		
		loan.setState("Contraproposta");
		loan.setTotal(newTotal);
		
		loanService.saveLoan(loan);
		
		return "redirect:/company/home";
	}

}
