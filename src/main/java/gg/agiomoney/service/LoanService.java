package gg.agiomoney.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gg.agiomoney.model.Loan;
import gg.agiomoney.repository.LoanRepository;

@Service
public class LoanService {
	
	private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Transactional
	public void saveLoan(Loan loan) {
		logger.trace("Entrou em salvar");
		loanRepository.save(loan);
	}

}

