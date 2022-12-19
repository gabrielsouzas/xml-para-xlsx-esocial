package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Funcionario;
import util.Conexao;

public class FuncionarioDao {

	public boolean save(Funcionario funcionario) {

		String sql = "INSERT INTO funcionario(nome,cpf) VALUES(?,?)";

		Connection conn = null;
		PreparedStatement pstm = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL(); 
			pstm = conn.prepareStatement(sql);

			pstm.setString(1, funcionario.getNome());
			pstm.setString(2, funcionario.getCpf());

			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean excluirPorId(int id) {

		String sql = "DELETE FROM funcionario WHERE id = ?";

		Connection conn = null;
		PreparedStatement pstm = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL(); 
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean update(Funcionario funcionario) {

		String sql = "UPDATE funcionario SET nome = ?, cpf = ? WHERE id = ?";

		Connection conn = null;
		PreparedStatement pstm = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL();
			pstm = conn.prepareStatement(sql);

			pstm.setString(1, funcionario.getNome());
			pstm.setString(2, funcionario.getCpf());
			
			pstm.setLong(3, funcionario.getId());

			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public List<Funcionario> getFuncionarios() {

		String sql = "SELECT * FROM funcionario ORDER BY nome";

		List<Funcionario> funcionarios = new ArrayList<Funcionario>();

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL();
			pstm = conn.prepareStatement(sql);
			rset = pstm.executeQuery();

			while (rset.next()) {

				Funcionario funcionario = new Funcionario();

				funcionario.setId(rset.getInt("id"));
				funcionario.setNome(rset.getString("nome"));
				funcionario.setCpf(rset.getString("cpf"));
				
				funcionarios.add(funcionario);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return funcionarios;
	}

	public Funcionario selectbyid(int id) {

		Funcionario funcionario = null;

		String sql = "SELECT * FROM funcionario WHERE id = ?";

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL();
			pstm = conn.prepareStatement(sql);			
			pstm.setInt(1, id);
			rset = pstm.executeQuery();

			if(rset.next()) {

				funcionario = new Funcionario();
				funcionario.setId(rset.getInt("id"));
				funcionario.setNome(rset.getString("nome"));
				funcionario.setCpf(rset.getString("cpf"));
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}				
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return funcionario;
	}

	public List<Funcionario> selectByNome(String nome) {

		List<Funcionario> funcionarios = new ArrayList<Funcionario>();

		String sql = "SELECT * FROM funcionario WHERE LOWER(nome) LIKE LOWER(?)";

		Connection conn = null;
		PreparedStatement pstm = null;

		ResultSet rset = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL();
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, "%" + nome + "%");			
			rset = pstm.executeQuery();

			while (rset.next()) {

				Funcionario funcionario = new Funcionario();
				funcionario.setId(rset.getInt("id"));
				funcionario.setNome(rset.getString("nome"));
				funcionario.setCpf(rset.getString("cpf"));

				funcionarios.add(funcionario);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {			
				if (rset != null) {
					rset.close();
				}			
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return funcionarios;
	}
	
	public List<Funcionario> selectByCpf(String cpf) {

		List<Funcionario> funcionarios = new ArrayList<Funcionario>();

		String sql = "SELECT * FROM funcionario WHERE cpf LIKE ?";

		Connection conn = null;
		PreparedStatement pstm = null;

		ResultSet rset = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL();
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, "%" + cpf + "%");			
			rset = pstm.executeQuery();

			while (rset.next()) {

				Funcionario funcionario = new Funcionario();
				funcionario.setId(rset.getInt("id"));
				funcionario.setNome(rset.getString("nome"));
				funcionario.setCpf(rset.getString("cpf"));

				funcionarios.add(funcionario);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {			
				if (rset != null) {
					rset.close();
				}			
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return funcionarios;
	}
	
	public Funcionario selectByOneCpf(String cpf) {

		Funcionario funcionario = null;

		String sql = "SELECT * FROM funcionario WHERE cpf LIKE ?";

		Connection conn = null;
		PreparedStatement pstm = null;

		ResultSet rset = null;

		try {
			conn = Conexao.createConnectionToPostgreSQL();
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, cpf);			
			rset = pstm.executeQuery();

			if(rset.next()) {

				funcionario = new Funcionario();
				funcionario.setId(rset.getInt("id"));
				funcionario.setNome(rset.getString("nome"));
				funcionario.setCpf(rset.getString("cpf"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {			
				if (rset != null) {
					rset.close();
				}			
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return funcionario;
	}

}
