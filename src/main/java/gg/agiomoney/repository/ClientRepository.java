package gg.agiomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.agiomoney.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long>  {

}


