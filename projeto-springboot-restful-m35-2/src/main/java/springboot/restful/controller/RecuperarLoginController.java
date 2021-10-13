package springboot.restful.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.excecoes.ObjetoErro;
import springboot.restful.model.Usuario;
import springboot.restful.repository.UsuarioRepository;
import springboot.restful.service.EnviarEmailService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/recuperar")
public class RecuperarLoginController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EnviarEmailService enviarEmailService;
	
	@ResponseBody
	@PostMapping(value = "/")
	public ResponseEntity<ObjetoErro> recuperar(@RequestBody Usuario usuario) throws Exception{
		
		ObjetoErro objetoErro = new ObjetoErro();
		
		Usuario user = usuarioRepository.procurarUserPorLogin(usuario.getLogin());
	
		if(user == null) {
			objetoErro.setCodigoError("404"); /* Não encontrado */
			objetoErro.setDescricaoError("Usuário não encontrado");
		}else {
			
			/* Rotina de envio de e-mail - Inicio*/
	
			//Gerando uma senha nova
			//a senha nova será gerada de acordo com a data do dia
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime()); 
			
			//criptografando a senha
			String senhaCriptografada = new BCryptPasswordEncoder().encode(senhaNova);
			
			//atualizando no banco de dados
			this.usuarioRepository.atualizarSenha(senhaCriptografada, user.getId());
			
			//chamando o service para enviar E-mail
			this.enviarEmailService.enviarEmail(usuario.getLogin(), "Recuperação de senha", "Sua nova senha é: " + senhaNova);
			
			/* Rotina de envio de e-mail - Fim*/
			
			objetoErro.setCodigoError("404"); /* Encontrado */
			objetoErro.setDescricaoError("Acesso enviado para o seu e-mail");
		}
		
		return new ResponseEntity<ObjetoErro>(objetoErro, HttpStatus.OK);
	
	}
	
	
}




