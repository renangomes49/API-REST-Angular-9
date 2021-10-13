package springboot.restful.model;

public class UserChart {

	// Classe criada para receber os dados de Nome e Salario do Usuário para passar para o gráfico (Chart.js)
	
	private String nome;
	private String salario;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSalario() {
		return salario;
	}
	
	public void setSalario(String salario) {
		this.salario = salario;
	}
	
	
	
}
