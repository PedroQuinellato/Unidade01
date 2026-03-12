package app;

import dao.ProdutoDAO;
import model.Produto;
import java.util.Scanner;


public class Aplicacao {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ProdutoDAO dao = new ProdutoDAO();
		int opcao = 0;

		while (opcao != 5) {
			System.out.println("\n=== MENU DE PRODUTOS ===");
			System.out.println("1) Inserir");
			System.out.println("2) Listar");
			System.out.println("3) Atualizar");
			System.out.println("4) Excluir");
			System.out.println("5) Sair");
			System.out.print("Escolha uma opção: ");
			opcao = scanner.nextInt();
			scanner.nextLine(); // Limpa o buffer do teclado

			switch (opcao) {
			case 1:
				System.out.print("Nome do produto: ");
				String nome = scanner.nextLine();
				System.out.print("Preço: ");
				double preco = scanner.nextDouble();
				Produto p = new Produto(-1, nome, preco); // O banco gera o ID automático
				if (dao.inserir(p)) System.out.println("-> Produto inserido com sucesso!");
				break;
			case 2:
				System.out.println("\n--- Lista de Produtos ---");
				for (Produto prod : dao.listar()) {
					System.out.println(prod.toString());
				}
				break;
			case 3:
				System.out.print("ID do produto a atualizar: ");
				int idAtualizar = scanner.nextInt();
				scanner.nextLine();
				System.out.print("Novo nome: ");
				String novoNome = scanner.nextLine();
				System.out.print("Novo preço: ");
				double novoPreco = scanner.nextDouble();
				Produto pAtualizar = new Produto(idAtualizar, novoNome, novoPreco);
				if (dao.atualizar(pAtualizar)) System.out.println("-> Atualizado com sucesso!");
				break;
			case 4:
				System.out.print("ID do produto a excluir: ");
				int idExcluir = scanner.nextInt();
				if (dao.excluir(idExcluir)) System.out.println("-> Excluído com sucesso!");
				break;
			case 5:
				System.out.println("Saindo do sistema...");
				break;
			default:
				System.out.println("Opção inválida!");
			}
		}
		scanner.close();
	}
}