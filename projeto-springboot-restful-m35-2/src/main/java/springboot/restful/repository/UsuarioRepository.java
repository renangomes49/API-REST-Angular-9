package springboot.restful.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springboot.restful.model.Usuario;

@Transactional
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	@Query("select u from Usuario u where u.login = ?1")
	public Usuario procurarUserPorLogin(String login);
	
	@Query(value = "select u from Usuario u where u.nome like %?1%")
	public List<Usuario> procurarUserPorNome(String nome);
	
	@Modifying
	@Query(nativeQuery = true, value = "update usuario set token = ?1 where login = ?2")
	public void atualizarTokenUser(String token, String login);
	
	//inserindo papel ao usuario cadastrado
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_role (usuario_id, role_id) values (?1,2)")
	public void inserirAcessoRolePadrao(Long idUsuario);

	@Modifying
	@Query(value = "update usuario set senha = ?1 where id = ?2", nativeQuery = true)
	public void atualizarSenha(String senha, Long codUser);
	
	default Page<Usuario> procurarUsuariosPorNomePage(String nome, PageRequest page){
		
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		
		/* Configurando para pesquisar por nome e paginação */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome", 
										 ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Usuario> example = Example.of(usuario, exampleMatcher);
		
		Page<Usuario> retorno = findAll(example, page);
		
		return retorno;
		
	}
	
	
}





