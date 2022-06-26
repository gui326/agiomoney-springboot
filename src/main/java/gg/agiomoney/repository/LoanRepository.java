package gg.agiomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.agiomoney.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

}
