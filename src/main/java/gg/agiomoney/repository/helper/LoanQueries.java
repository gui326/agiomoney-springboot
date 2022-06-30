package gg.agiomoney.repository.helper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import gg.agiomoney.model.Loan;

public interface LoanQueries {
	
	public Page<Loan> findLoansByClientIdPaginacao(Long id, Pageable pageable);
	
	public Page<Loan> findLoansByCompanyIdPaginacao(Long id, Pageable pageable);
	
}
