package gg.agiomoney.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gg.agiomoney.model.Client;
import gg.agiomoney.repository.ClientRepository;

@Service
public class ClientService {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Transactional
	public void saveClient(Client client) {
		logger.trace("Entrou em salvar");
		clientRepository.save(client);
	}

}

