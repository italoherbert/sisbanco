package italo.sisbanco.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException.FeignClientException;
import italo.sisbanco.Erros;
import italo.sisbanco.exception.ServiceException;
import italo.sisbanco.integration.KeycloakMicroserviceIntegration;
import italo.sisbanco.integration.model.UserCreated;
import italo.sisbanco.mapper.ContaMapper;
import italo.sisbanco.model.Conta;
import italo.sisbanco.model.request.conta.ContaFiltroRequest;
import italo.sisbanco.model.request.conta.ContaSaveRequest;
import italo.sisbanco.model.response.conta.ContaResponse;
import italo.sisbanco.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private KeycloakMicroserviceIntegration keycloak;
	
	@Autowired
	private ContaMapper contaMapper;
	
	public void registra( ContaSaveRequest request, String authorizationHeader ) throws ServiceException {
		boolean existe = contaRepository.existeTitular( request.getTitular() );
		if ( existe )
			throw new ServiceException( Erros.TITULAR_JA_EXISTE );
		
		Conta conta = contaMapper.novoBean();
		contaMapper.carrega( conta, request );
		try {
			ResponseEntity<UserCreated> resp = 
					keycloak.registraUser( request.getUser(), authorizationHeader );
			
			if ( resp.getStatusCode().is2xxSuccessful() ) {
				contaRepository.save( conta );
			} else {
				throw new ServiceException( Erros.KEYCLOAK_USER_NAO_CRIADO );
			}
		} catch ( FeignClientException e ) {
			throw new ServiceException( e.status(), e.contentUTF8() );
		}
	}
	
	public void altera( Long contaId, ContaSaveRequest request ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		
		String novoTitular = request.getTitular();
		String titular = conta.getTitular();
		
		if ( !novoTitular.equalsIgnoreCase( titular ) ) {
			boolean existe = contaRepository.existeTitular( novoTitular );
			if ( existe )
				throw new ServiceException( Erros.TITULAR_JA_EXISTE );
		}
		
		contaMapper.carrega( conta, request ); 
		contaRepository.save( conta );	
	}
	
	public ContaResponse get( Long contaId ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		
		ContaResponse resp = contaMapper.novoContaResponse();
		contaMapper.carregaResponse( resp, conta );
		return resp;
	}
	
	public ContaResponse getByUsername( String username ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.buscaPorUsername( username );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
				
		ContaResponse resp = contaMapper.novoContaResponse();
		contaMapper.carregaResponse( resp, conta );
		return resp;
	}
	
	public List<ContaResponse> filtra( ContaFiltroRequest request ) {
		List<Conta> contas = contaRepository.filtra( request.getTitular() );
		
		List<ContaResponse> responses = new ArrayList<>();
		for( Conta c : contas ) {
			ContaResponse resp = contaMapper.novoContaResponse();
			contaMapper.carregaResponse( resp, c );
			responses.add( resp );
		}
		
		return responses;
	}
	
	public void deleta( Long contaId ) throws ServiceException {
		boolean existe = contaRepository.existsById( contaId );
		if ( !existe )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		contaRepository.deleteById( contaId ); 
	}
	
}
