package springboot.restful.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import springboot.restful.ApplicationContextLoad;
import springboot.restful.model.Usuario;
import springboot.restful.repository.UsuarioRepository;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	/* Tempo de validade do Token - 2 dias (172800000 ms) */
	private static final long EXPIRATION_TIME = 172800000;
	
	/* Uma senha única para compor a autenticação e ajudar na segurança */
	private static final String SECRET = "senhaEstremamenteSecreta";
	
	/* Prefixo padrão de Token */
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/* Gerando token de autenticação e adicionando ao cabeçalho e reposta HTTP */
	/* Primeiro Acesso */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException{
		
		/* Montagem do Token */
		String JWT = Jwts.builder() /* Chama o gerador de Token */
						.setSubject(username) /* Adiciona o usuário */
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Tempo de expiração */
						.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, SECRET).compact(); /* Compactação e algoritmos de geração de senha */
		
		/* Junta o token com o prefixo */
		String token = TOKEN_PREFIX + " " + JWT;  // Exe.: Bearer 3nunefr3n9rn38rneufno
		
		/* Adicionando no cabeçalho HTTP */
		response.addHeader(HEADER_STRING, token);  // Exe.: Authorization: Bearer 3nunefr3n9rn38rneufno
		
		/* Atualizando o token no banco de dados */
		ApplicationContextLoad.getApplicationContext()
		.getBean(UsuarioRepository.class).atualizarTokenUser(JWT, username);
		
		/* Liberando resposta para portas diferentes que usam a API ou caso clientes Web */
		liberacaoCors(response);
		
		/* Escreve token como resposta no corpo HTTP */
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
		
	}
	
	/* Verificar o usuario, 2º Acesso*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {	
		
		try {
			
			/* Pega o token enviado no cabeçalho HTTP */
			String token = request.getHeader(HEADER_STRING);
			
			this.liberacaoCors(response);
			if(token == null) {
				
				return null;
			}
			
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			if(token != null) {
				
				/* Faz a validação do token do usuário na requisição */
				String user = Jwts.parser().setSigningKey(SECRET)    // Exe.: Bearer 3nunefr3n9rn38rneufno
								  .parseClaimsJws(tokenLimpo) // Exe.: 3nunefr3n9rn38rneufno
								  .getBody().getSubject(); // Exe.: Renan
				
				if(user != null) {
					Usuario usuario = ApplicationContextLoad.getApplicationContext()
										.getBean(UsuarioRepository.class).procurarUserPorLogin(user);
					if(usuario != null) {
						 
						// verificando o token enviado com o token que está gravado no banco de dados
						if(tokenLimpo.equals(usuario.getToken())) {
						
							return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
						}
					}
					
				}
				
			}
			
			
		}catch (ExpiredJwtException e) {
			//System.out.println("OI");
			try {
				response.getOutputStream().println("Seu TOKEN está espirado. Gere um novo TOKEN");
			} catch (IOException e1) {
				
			}
			
		}
		
		return null; // Não Autorizado
	}

	private void liberacaoCors(HttpServletResponse response) {
		
		if(response.getHeader("Access-Control-Allow-Origin") == null ) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers","*");
		}
		
		if(response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Methods") == null) {
		   response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}
	
}



















