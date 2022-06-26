package gg.agiomoney.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gg.agiomoney.model.Company;
import gg.agiomoney.repository.CompanyRepository;

@Service
public class CompanyService {
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Transactional
	public void saveCompany(Company company) {
		logger.trace("Entrou em salvar");
		companyRepository.save(company);
	}

}
