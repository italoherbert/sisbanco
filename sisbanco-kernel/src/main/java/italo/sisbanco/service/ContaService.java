package italo.sisbanco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.exception.ServiceException;
import italo.sisbanco.model.request.ContaRequest;
import italo.sisbanco.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;
	
	public void registraConta( ContaRequest request ) throws ServiceException {
		
	}
	
}
