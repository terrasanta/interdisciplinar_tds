package com.opet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.opet.util.Reader;

public class Checkpoint {
	//início de declarações de variáveis globais, ou seja, que possuem escopo em todas as funções criadas
	private static Connection conn;
	private static ResultSet rs;
	private static PreparedStatement stmt;
	private static int idUsuario = 0;
	private static boolean logado = false;
	//fim das declarações globais
	
	//função MAIN. Cria um link de conexao com o banco de dados e chama o método de autenticação.
	//se autenticado corretamente, chama o menu de execução do programa
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
	//função de autenticação
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
	public static void adicionaTarefa(){
		System.out.println("Adiciona");
	}
	public static void editaTarefa(){
		System.out.println("Edita");
	}
	public static void excluiTarefa(){
		System.out.println("Exclui");
	}
	public static void listaTarefas(){
		System.out.println("Lista");
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
			System.out.println("Digite 1 para ");
			System.out.println("Digite 2 para ");
			System.out.println("Digite 3 para ");
			System.out.println("Digite 4 para ");
			System.out.println("Digite 5 para ");
			System.out.println("Digite 6 para ");
			System.out.println("Digite 0 para voltar ao menu principal");
			optf = Reader.readInt();
			switch(optf){
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
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
	public static void cadastraTipoServico(){
		
	}
	public static void editaTipoServico(){
		
	}
	public static void excluiTipoServico(){
		
	}
	
	public static void divisor(){
		System.out.println("==================================================");
	}
	public static void conexao() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","system","system");
		conn.setAutoCommit(false);
	}
}
