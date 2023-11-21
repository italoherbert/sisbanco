package italo.sisbanco.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.Erros;
import italo.sisbanco.exception.ServiceException;
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
	private ContaMapper contaMapper;
	
	public void registra( ContaSaveRequest request ) throws ServiceException {
		boolean existe = contaRepository.existeTitular( request.getTitular() );
		if ( existe )
			throw new ServiceException( Erros.TITULAR_JA_EXISTE );
		
		Conta conta = contaMapper.novoBean();
		contaMapper.carrega( conta, request );
		contaRepository.save( conta );
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
