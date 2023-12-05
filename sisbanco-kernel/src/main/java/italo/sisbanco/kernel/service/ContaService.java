package italo.sisbanco.kernel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException.FeignClientException;
import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.integration.KeycloakMicroserviceIntegration;
import italo.sisbanco.kernel.integration.model.UserCreated;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.request.conta.ContaFiltroRequest;
import italo.sisbanco.kernel.model.request.conta.ContaSaveRequest;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.service.manager.ContaAlterManager;
import italo.sisbanco.kernel.service.mapper.ContaMapper;

@Service
public class ContaService {
	
	@Autowired
	private KeycloakMicroserviceIntegration keycloak;
	
	@Autowired
	private ContaAlterManager contaAlterManager;

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private ContaMapper contaMapper;
	
	public ContaResponse registra( ContaSaveRequest request, String authorizationHeader ) throws ServiceException {
		boolean existe = contaRepository.existeTitular( request.getTitular() );
		if ( existe )
			throw new ServiceException( Erros.TITULAR_JA_EXISTE );
				
		try {
			ResponseEntity<UserCreated> resp = 
					keycloak.registraUser( request.getUser(), authorizationHeader );
			
			if ( resp.getStatusCode().is2xxSuccessful() ) {
				String userId = resp.getBody().getUserId();
				
				Conta conta = contaMapper.novoBean();
				contaMapper.carregaParaRegistro( conta, request, userId );
				contaRepository.save( conta );
				
				ContaResponse contaResp = contaMapper.novoContaResponse();
				contaMapper.carregaResponse( contaResp, conta ); 
				return contaResp;
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
		
		contaMapper.carregaParaAlteracao( conta, request ); 
		contaRepository.save( conta );	
	}
	
	public void alteraSaldo( Long contaId, ValorRequest valor ) throws ServiceException {
		contaAlterManager.alteraSaldo( contaId, valor.getValor() ); 		
	}
	
	public void alteraCredito( Long contaId, ValorRequest valor ) throws ServiceException {
		contaAlterManager.alteraCredito( contaId, valor.getValor() ); 		
	}
	
	public void alteraDebitoSimplesLimite( Long contaId, ValorRequest valor ) throws ServiceException {
		contaAlterManager.alteraDebitoSimplesLimite( contaId, valor.getValor() ); 		
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
		String titularFiltro = request.getTitular()+"%";
		
		List<Conta> contas = contaRepository.filtra( titularFiltro );
		
		List<ContaResponse> responses = new ArrayList<>();
		for( Conta c : contas ) {
			ContaResponse resp = contaMapper.novoContaResponse();
			contaMapper.carregaResponse( resp, c );
			responses.add( resp );
		}
		
		return responses;
	}
	
	public void deleta( Long contaId, String authorizationHeader ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		String userId = conta.getUserId();
				
		keycloak.deletaUser( userId, authorizationHeader );
		contaRepository.deleteById( contaId ); 
	}
	
}
