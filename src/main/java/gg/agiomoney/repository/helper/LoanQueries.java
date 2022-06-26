package gg.agiomoney.repository.helper;

import java.util.List;

import gg.agiomoney.model.Loan;

public interface LoanQueries {

	public List<Loan> findLoansByClientId(Long id);
	
}
