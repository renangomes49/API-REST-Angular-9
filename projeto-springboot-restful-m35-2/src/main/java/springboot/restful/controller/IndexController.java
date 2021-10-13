package springboot.restful.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import springboot.restful.model.Telefone;
import springboot.restful.model.UserChart;
import springboot.restful.model.Usuario;
import springboot.restful.model.UsuarioDTO;
import springboot.restful.repository.TelefoneRepository;
import springboot.restful.repository.UsuarioRepository;
import springboot.restful.service.ImplementacaoUserDetailsService;
import springboot.restful.service.RelatorioService;

@CrossOrigin(origins = "*") //todas requisições serão aceitas
//@CrossOrigin(origins = {"www.jdevtreinamento.com.br","www.google.com.br"}) ; Somente requisições vindo desse dominio serão aceitas pela API
@RestController /* Arquitetura Rest */
@RequestMapping("/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private RelatorioService relatorioService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*Serviço RestFul*/
	/* Método para retornar um usuário */
	@GetMapping(value = {"/{id}"}, produces = "application/json")
	public ResponseEntity<Usuario> inicio(@PathVariable (value = "id") Long id ) {
		
		Usuario usuario = usuarioRepository.findById(id).get();
		
		//UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
		
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	/* Método para deletar um usuárop */
	@DeleteMapping(value = {"/{id}"}, produces = "application/text")
	public ResponseEntity<String> deletar(@PathVariable (value = "id") Long id ) {
		
		usuarioRepository.deleteById(id);
		
		return new ResponseEntity<String>("Usuário deletado com sucesso!", HttpStatus.OK);
	}
	
	
	/* Método para retornar uma lista de usuários */
	/* Método para renderizar somente a primeira pagina da tela que contem a paginação*/
	/* @CrossOrigin(origins = "www.sistemadocliente1.com") => Listagem de usuários só será aceita se a requisição partir desse dominio */
	@GetMapping(value ={"/"}, produces = "application/json")
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuarios() throws InterruptedException{
		 
		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
		
		Page<Usuario> list = usuarioRepository.findAll(page);
				
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	//método para carregar os usuários para realizar a paginação
	// pagina 2 em diante
	@GetMapping(value ={"/pagina/{pagina}"}, produces = "application/json")
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuariosPorPagina(@PathVariable("pagina") int pagina) throws InterruptedException{
		 
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		
		Page<Usuario> list = usuarioRepository.findAll(page);
				
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	/* Método para retornar uma lista de usuários que contenham um determinado nome informado*/
	/* Método para renderizar somente a primeira pagina da tela que contem a paginação*/
	@GetMapping(value ={"/usuarios/{nome}"}, produces = "application/json")
	public ResponseEntity<Page<Usuario>> usuariosPorNome(@PathVariable("nome") String nome) throws InterruptedException{
		
		PageRequest page = null;	
		Page<Usuario> list = null;
		
		if(nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined") ) {
			
			page = PageRequest.of(0, 5, Sort.by("nome"));	
			list = usuarioRepository.findAll(page);
		}else {
			page = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.procurarUsuariosPorNomePage(nome,page);
		}
		
		
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	/* Método para retornar uma lista de usuários que contenham um determinado nome informado*/
	/* Método para renderizar da pagina 2 em diante*/
	@GetMapping(value ={"/usuarios/{nome}/pagina/{pagina}"}, produces = "application/json")
	@CachePut("a")
	public ResponseEntity<Page<Usuario>> usuariosPorNome(@PathVariable("nome") String nome,
														 @PathVariable("pagina") int pagina) throws InterruptedException{
		
		PageRequest page = null;	
		Page<Usuario> list = null;
		
		if(nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined") ) {
			
			page = PageRequest.of(pagina, 5, Sort.by("nome"));	
			list = usuarioRepository.findAll(page);
		}else {
			page = PageRequest.of(pagina, 5, Sort.by("nome"));
			list = usuarioRepository.procurarUsuariosPorNomePage(nome,page);
		}
			
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	/* Método para salvar usuário */
	@PostMapping(value = {"/"}, produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception {
		
		if(usuario.getTelefones() != null) {
			for(int pos = 0 ; pos < usuario.getTelefones().size() ; pos++) {
				usuario.getTelefones().get(pos).setUsuario(usuario);
			}
		}
		
		// Consumindo uma API pública externa - viacep.com.br - INICIO
		if(usuario.getCep() != null) {
			URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			Usuario usuarioAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
			
			usuario.setCep(usuarioAux.getCep());
			usuario.setLogradouro(usuarioAux.getLogradouro());
			usuario.setComplemento(usuarioAux.getComplemento());
			usuario.setBairro(usuarioAux.getBairro());
			usuario.setLocalidade(usuarioAux.getLocalidade());
			usuario.setUf(usuarioAux.getUf());
			
			// Consumindo uma API pública externa - viacep.com.br - FIM
		}
		
		// Criptografando a senha que vem da requisição para ser salva no Banco de Dados
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		
		Usuario auxUsuario = usuarioRepository.save(usuario);
		
		// inserindo o papel do usuário salvo. Ex. User, Admin
		// Padrao = ROLE_USER (id = 2)
		usuarioRepository.inserirAcessoRolePadrao(auxUsuario.getId());
		
		return new ResponseEntity<Usuario>(auxUsuario, HttpStatus.OK);
	}
	
	/* Método para atualizar usuário */
	@PutMapping(value = {"/"}, produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {
		
		if(usuario.getTelefones() != null) {
			for(int pos = 0 ; pos < usuario.getTelefones().size() ; pos++) {
				usuario.getTelefones().get(pos).setUsuario(usuario);
			}
		}
	
		// Criptografando a senha caso o usuário tenha alterado
		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();
		if(!userTemporario.getSenha().equals(usuario.getSenha())) {
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	//excluir telefone
	@DeleteMapping(value = "/excluir-telefone/{id}", produces = "application/text")
	public String excluirTelefone(@PathVariable("id") Long id) {
		
		telefoneRepository.deleteById(id);
		
		return "ok";
	}
	
	// Método - download relatório de todos os usuários
	@GetMapping(value = "/download-relatorio", produces = "application/text")
	public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception{
		
		byte[] pdf = relatorioService.gerarRelatório("relatorio-usuario", new HashMap<>() ,request.getServletContext());
		
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}
	
	// Método - download relatório dos usuários de acordo com os parametros da data de nascimento
	@GetMapping(value = "/download-relatorio-intervalo-datas/data-inicio/{dataIni}/data-final/{dataFin}", produces = "application/text")
	public ResponseEntity<String> downloadRelatorioIntervaloDatas(HttpServletRequest request,
																  @PathVariable("dataIni") String dataIni, 
																  @PathVariable("dataFin") String dataFin) throws Exception{
		
		
		
		//Passando os parametros para o relatorio
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DATA_INICIO", dataIni);
		params.put("DATA_FIM", dataFin);
		
		byte[] pdf = relatorioService.gerarRelatório("relatorio-usuario-com-parametro", params,request.getServletContext());
		
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}
	
	
	// end-point para os dados do gráfico
	@GetMapping(value = "/grafico", produces = "application/json")
	public ResponseEntity<UserChart> grafico(){
		
		UserChart userChart = new UserChart();
		
		List<String> resultado = jdbcTemplate.queryForList("select array_agg( '''' || nome || '''') from usuario where salario > 0 and nome <> '' union all select cast(array_agg(salario) as character varying[]) from usuario where salario > 0 and nome <> ''; ", String.class);
	
		if(!resultado.isEmpty()) {
			String nomesUsuario = resultado.get(0).replaceAll("\\{", "").replaceAll("\\}", "");
			String salariosUsuario = resultado.get(1).replaceAll("\\{", "").replaceAll("\\}", "");
			
			userChart.setNome(nomesUsuario);
			userChart.setSalario(salariosUsuario);
			
		}
		
		
		return new ResponseEntity<UserChart>(userChart, HttpStatus.OK);
	}
	
	
}






