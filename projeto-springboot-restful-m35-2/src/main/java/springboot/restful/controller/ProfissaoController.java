package springboot.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.model.Profissao;
import springboot.restful.repository.ProfissaoRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/profissao")
public class ProfissaoController {

	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Profissao>> profissoes(){
		
		List<Profissao> profissoes = profissaoRepository.findAll();
		
		return new ResponseEntity<List<Profissao>>(profissoes, HttpStatus.OK);
	}
	
}
 