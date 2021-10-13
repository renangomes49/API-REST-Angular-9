package springboot.restful.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;

@Entity
@SequenceGenerator(name = "sequence_role", sequenceName = "sequence_role", allocationSize = 1, initialValue = 1)
public class Role implements GrantedAuthority{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_role")
	private Long id;
	
	/* Papel do usuario no sistema. Ex.: ROLE_ADMIN, ROLE_SECRETARIO */
	private String nomeRole;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeRole() {
		return nomeRole;
	}
	
	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}


	/*Método retorna o nome do papel, acesso ou autorização.*/
	/*Exemplo de retorno.: ROLE_GERENTE.*/
	@Override
	public String getAuthority() {
		return this.nomeRole;
	} 
	
}
