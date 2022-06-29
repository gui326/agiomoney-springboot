package gg.agiomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.agiomoney.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long>  {
	
	public Client findByEmail(String email);
	
	public Client findByPassword(String password);

}


