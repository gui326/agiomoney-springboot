package gg.agiomoney.repository.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gg.agiomoney.model.Loan;

public class LoanQueriesImpl implements LoanQueries{

private static final Logger logger = LoggerFactory.getLogger(LoanQueriesImpl.class);
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	@Transactional
	public List<Loan> findLoansByClientId(Long codigo) {
		logger.trace("Entrou no método findLoansByClientId");
		logger.debug("codigo recebido como parametro {}", codigo);
		String jpql = "Select l from Loan l where l.code_client = 1";
		
		TypedQuery<Loan> query = manager.createQuery(jpql, Loan.class);
		query.setParameter("codigo", codigo);
		List<Loan> loans = query.getResultList();
		
		logger.debug("Código loans");
		for (Loan l : loans) {
			logger.debug("{}", l);
			logger.debug("codigo: {}", l.getCode());
		}
		
		return loans;	
	}
	
}
