package com.opet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.opet.util.Reader;

public class Checkpoint {
	private static Connection conn;
	private static ResultSet rs;
	private static PreparedStatement stmt;
	private static int idUsuario = 0;
	private static boolean logado = false;

	public static void main(String[] args) throws Exception, ClassNotFoundException, SQLException {
		conexao();
		divisor();
		System.out.println("Bem vindo ao sistema Checkpoint");
		while(logado==false){
			logado = autenticacao();
			if(logado==true){
				menu();
			}
		}	
	}
	public static boolean autenticacao() throws SQLException{
		
		System.out.println("Você precisa se autenticar para utilizar o Sistema");
		divisor();
		System.out.print("Digite seu e-mail: ");
		String email = Reader.readString();
		stmt = conn.prepareStatement("select id_usuario from usuarios_inter where e_mail = ?");
		stmt.setString(1, email);
		rs = stmt.executeQuery();
		if(rs.next()){
			idUsuario = rs.getInt("id_usuario");
			System.out.print("Agora digite a sua senha: ");
			String senha = Reader.readString();
			senha = Criptografia.criptografar(senha);
			stmt = conn.prepareStatement("select senha from usuarios_inter where id_usuario = ?");
			stmt.setInt(1, idUsuario);
			rs = stmt.executeQuery();
			while(rs.next()){
				String senha_banco = rs.getString("senha");
				if(senha.equals(senha_banco)){
					System.out.println("Autenticado com sucesso!");
					divisor();
					return true;
				}else{
					System.out.println("Senha incorreta");
					return false;
				}
			}
			
		}else{
			System.out.println("E-mail não cadastrado no sistema");
			return false;
		}
		return false;
	}
	public static void menu() throws Exception{
		int opt = -1;
		do{
			divisor();
			System.out.println("MENU DE NAVEGAÇÂO");
			divisor();
			System.out.println("Digite 1 para adicionar uma tarefa");
			System.out.println("Digite 2 para editar uma tarefa existente");
			System.out.println("Digite 3 para excluir uma tarefa existente");
			System.out.println("Digite 4 para listar as tarefas existentes");
			System.out.println("Digite 5 para funções de controle");
			System.out.println("Digite 0 para fazer loggout");
			opt = Reader.readInt();
			switch(opt){
				case 1:
					adicionaTarefa();
					break;
				case 2:
					editaTarefa();
					break;
				case 3:
					excluiTarefa();
					break;
				case 4:
					listaTarefas();
					break;
				case 5:
					funcoesControle();
					break;
				case 0:
					logado = false;
					System.out.println("Obrigado por usar nosso sistema. Até logo!");
					break;
				default:
					System.out.println("Menu não localizado. Verifique as opções");
			}
			
		}while(opt != 0);
	}
	public static void adicionaTarefa() throws Exception{
		System.out.println("Você escolheu: Adicionar uma nova tarefa: ");
		divisor();
		boolean servicos = existeTipoServico();
		if(servicos==false){
			System.out.println("Para adicionar uma tarefa é necessário ter ao menos um tipo de serviço inserido e no momento não existe nenhum.");
			System.out.println("Escolha o menu \"funções de controle->Cadastrar tipos de serviço e depois tente novamente");
		}else{
			System.out.println("Escolha na lista abaixo o [ID] do tipo de serviço que você deseja incluir e digite");
			listaTiposServico();
			divisor();
			System.out.print("Digite aqui o ID: ");
			int id = Reader.readInt();
			
			stmt = conn.prepareStatement("select especificacao from tipos_servicos_inter where id_tipos_servicos = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			rs.next();
			String especificacao = rs.getString("especificacao");
			
			System.out.print("Nome do cliente: ");
			String nome = Reader.readString();
			System.out.print("Endereço do Cliente: ");
			String endereco = Reader.readString();
			System.out.print("Complemento do endereço: ");
			String complemento = Reader.readString();
			System.out.print("Bairro: ");
			String bairro = Reader.readString();
			System.out.print("Telefone do cliente (somente números): ");
			double telefone = Reader.readDouble();
			System.out.print("Email do cliente: ");
			String email_cliente = Reader.readString();
			System.out.print("Comentários a respeito desta tarefa: ");
			String comentarios = Reader.readString();
			
			System.out.println("");
			divisor();
			
			System.out.println("Revise os dados antes de inserir: ");
			System.out.println("Tipo de serviço: "+especificacao);
			System.out.println("Nome do Cliente: "+nome);
			System.out.println("Endereço do Cliente: "+endereco);
			System.out.println("Complemento do endereço: "+complemento);
			System.out.println("Bairro: "+bairro);
			System.out.println("Telefone do cliente: "+telefone);
			System.out.println("Email do cliente: "+email_cliente);
			System.out.println("Comentários a respeito desta tarefa: "+comentarios);
			System.out.println("");
			System.out.println("Deseja continuar? (S/n)");
			
			String continuar = Reader.readString();
			
			do{
				if(continuar.equals("n")){
					System.out.println("Operação cancelada.");
				}else if(continuar.equals("S")){
					stmt = conn.prepareStatement("insert into servicos_inter "
							+ "(id_servico, id_tipo_servico, nome_cliente, endereco_cliente, complemento_end, bairro, telefone_cliente, email_cliente, comentarios) "
							+ "values "
							+ "(seq_servicos_inter.nextval, ?, ?, ?, ?, ?, ?, ?, ?)");
					stmt.setInt(1, id);
					stmt.setString(2, nome);
					stmt.setString(3, endereco);
					stmt.setString(4, complemento);
					stmt.setString(5, bairro);
					stmt.setDouble(6, telefone);
					stmt.setString(7, email_cliente);
					stmt.setString(8, comentarios);
					
					int confirm = stmt.executeUpdate();
					
					if(confirm == 0){
						conn.rollback();
						System.out.println("Erro ao inserir nova tarefa");
					}else{
						conn.commit();
						System.out.println("Tarefa inserida com sucesso");
					}
				}else{
					System.out.println("Escolha entre \"S\" ou \"n\".");
				}
			}while(continuar=="S" || continuar=="n");
			
		}
		
	}
	public static void editaTarefa(){
		System.out.println("Edita");
	}
	public static void excluiTarefa(){
		System.out.println("Exclui");
	}
	public static void listaTarefas() throws Exception{
		System.out.println("LISTAGEM DE SERVIÇOS");
		divisor();
		
		stmt = conn.prepareStatement("select s.id_servico, s.bairro, t.especificacao from servicos_inter s, tipos_servicos_inter t where t.id_tipos_servicos = s.id_tipo_servico");
		rs = stmt.executeQuery();
		if(rs.next()){
			do{
				System.out.println("["+ rs.getInt("id_servico") +"] - Bairro: "+rs.getString("bairro")+" - Tipo de Serviço: "+rs.getString("especificacao"));
			}while(rs.next());
			System.out.println("Deseja acessar um serviço? (S/n)");
			String acessa = Reader.readString();
			do{
				if(acessa.equals("S")){
					System.out.print("Digite o ID do serviço que você deseja acessar: ");
					int id = Reader.readInt();
					stmt = conn.prepareStatement("select s.*, t.especificacao from servicos_inter s, tipos_servicos_inter t where s.id_servico = ? and t.id_tipos_servicos = s.id_tipo_servico");
					stmt.setInt(1, id);
					rs = stmt.executeQuery();
					while(rs.next()){
						System.out.println("Tipo de serviço: "+rs.getString("especificacao"));
						System.out.println("Nome do Cliente: "+rs.getString("nome_cliente"));
						System.out.println("Endereço do Cliente: "+rs.getString("endereco_cliente"));
						System.out.println("Complemento do endereço: "+rs.getString("complemento_end"));
						System.out.println("Bairro: "+rs.getString("bairro"));
						System.out.println("Telefone do cliente: "+rs.getInt("telefone"));
						System.out.println("Email do cliente: "+rs.getString("email_cliente"));
						System.out.println("Comentários a respeito desta tarefa: "+rs.getString("comentarios"));
					}
				}else if(acessa.equals("n")){
					System.out.println("Voltando ao menu principal...");
				}else{
					System.out.println("Escolha entre \"S\" ou \"n\".");
				}
			}while(acessa=="S" || acessa=="n");
		}else{
			System.out.println("Nenhum tipo de serviço encontrado");
		}
	}
	public static void abreTarefa(int idTarefa){
		
	}
	public static void concluiTarefa(int idTarefa){
		
	}
	public static void cancelaTarefa(int idTarefa){
		
	}
	public static void funcoesControle() throws Exception{
		int optf = -1;
		do{
			divisor();
			System.out.println("MENU DE NAVEGAÇÂO");
			divisor();
			System.out.println("Digite 1 para cadastrar status");
			System.out.println("Digite 2 para editar status");
			System.out.println("Digite 3 para excluir status");
			System.out.println("Digite 4 para cadastrar um novo tipo de serviço");
			System.out.println("Digite 5 para editar um tipo de serviço");
			System.out.println("Digite 6 para excluir um tipo de serviço");
			System.out.println("Digite 0 para voltar ao menu principal");
			optf = Reader.readInt();
			switch(optf){
				case 1:
					cadastraStatus();
					break;
				case 2:
					editaStatus();
					break;
				case 3:
					excluiStatus();
					break;
				case 4:
					cadastraTipoServico();
					break;
				case 5:
					editaTipoServico();
					break;
				case 6:
					excluiTipoServico();
					break;
				case 0:
					System.out.println("Saindo das funções de controle e retornando ao menu principal...");
					break;
				default:
					System.out.println("Opção não localizada. Verifique");
			}
			
		}while(optf != 0);
	}
	public static void cadastraStatus(){
		
	}
	public static void editaStatus(){
		
	}
	public static void excluiStatus(){
		
	}
	public static void cadastraTipoServico() throws SQLException{
		divisor();
		System.out.println("Cadastrar um novo tipo de serviço");
		divisor();
		System.out.println("Os seguintes serviços já fazem parte do sistema");
		listaTiposServico();
		System.out.println("Se o serviço que você deseja não está listado, digite \"continuar\", senão, digite \"ENTER\"");
		String ctn = Reader.readString();
		if(ctn.equals("continuar")){
			divisor();
			System.out.println("Digite a especificação do novo serviço: ");
			String servico = Reader.readString();
			
			stmt = conn.prepareStatement("insert into tipos_servicos_inter (id_tipos_servicos, especificacao) values (seq_tipo_servico_inter.nextval, ?)");
			stmt.setString(1, servico);
			
			int confirm = stmt.executeUpdate();
			if(confirm == 0){
				conn.rollback();
				System.out.println("Erro ao inserir novo tipo de serviço");
			}else{
				conn.commit();
				System.out.println("Novo tipo de serviço inserido com sucesso");
			}
		}else{
			System.out.println("Voltando para o menu inicial...");
			divisor();
		}
		
	}
	public static void editaTipoServico(){
		
	}
	public static void excluiTipoServico(){
		
	}
	public static boolean existeTipoServico() throws SQLException{
		stmt = conn.prepareStatement("select id_tipos_servicos from tipos_servicos_inter");
		rs = stmt.executeQuery();
		if(rs.next()){
			return true;
		}
		return false;
	}
	
	private static void divisor(){
		System.out.println("==================================================");
	}
	private static void conexao() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","system","system");
		conn.setAutoCommit(false);
	}
	private static void listaTiposServico() throws SQLException {
		stmt = conn.prepareStatement("select id_tipos_servicos, especificacao from tipos_servicos_inter");
		rs = stmt.executeQuery();
		if(rs.next()){
			do{
				System.out.println("["+ rs.getInt("id_tipos_servicos") +"] - "+rs.getString("especificacao"));
			}while(rs.next());
		}else{
			System.out.println("Nenhum tipo de serviço encontrado");
		}
	}
}