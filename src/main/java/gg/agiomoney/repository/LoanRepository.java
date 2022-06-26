package gg.agiomoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.agiomoney.model.Client;
import gg.agiomoney.model.Loan;
import gg.agiomoney.repository.helper.LoanQueries;

public interface LoanRepository extends JpaRepository<Loan, Long>, LoanQueries{

	public List<Loan> findByClientCode(Long code); 
	
}
