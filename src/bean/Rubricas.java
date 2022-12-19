package bean;

public class Rubricas {

	private String codigo;
	private String descricao;
	private String valor;
	private String funcionario;
	
	public Rubricas() {
		
	}

	public Rubricas(String codigo, String descricao, String valor, String funcionario) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.valor = valor;
		this.funcionario = funcionario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	
	
}
