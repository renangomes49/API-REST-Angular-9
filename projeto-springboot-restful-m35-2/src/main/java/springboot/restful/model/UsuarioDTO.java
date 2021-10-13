package springboot.restful.model;

import java.io.Serializable;
import java.util.List;

public class UsuarioDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userLogin;
	private String userNome;
	private List<Telefone> telefones;
	
	
	public UsuarioDTO(Usuario usuario) {

		this.userLogin = usuario.getLogin();
		this.userNome = usuario.getNome();
		this.telefones = usuario.getTelefones();
		
	}
	
	public String getUserLogin() {
		return userLogin;
	}
	
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
	public String getUserNome() {
		return userNome;
	}
	
	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}
	
	public List<Telefone> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	
	
}
