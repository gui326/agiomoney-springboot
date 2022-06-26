package gg.agiomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.agiomoney.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{

}
