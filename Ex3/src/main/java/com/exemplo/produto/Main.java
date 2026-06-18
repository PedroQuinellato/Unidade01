package com.exemplo.produto;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe principal da aplicação.
 * Define todas as rotas HTTP usando o Spark Framework.
 *
 * Rotas disponíveis:
 *   GET  /              → redireciona para /produtos
 *   GET  /produtos      → lista todos os produtos
 *   GET  /produtos/novo → exibe formulário de cadastro
 *   POST /produtos      → processa cadastro (lê dados do formulário HTML)
 *   GET  /produtos/:id/editar → exibe formulário de edição
 *   POST /produtos/:id/editar → processa atualização
 *   GET  /produtos/:id/excluir → remove produto
 */
public class Main {

    private static final ProdutoDAO dao = new ProdutoDAO();

    public static void main(String[] args) {

        // ── Configuração do Spark ──────────────────────────────────────────
        port(8080);

        // Servir arquivos estáticos da pasta resources/public
        staticFiles.location("/public");

        // ── Rota raiz ─────────────────────────────────────────────────────
        get("/", (req, res) -> {
            res.redirect("/produtos");
            return null;
        });

        // ── LISTAR todos os produtos ───────────────────────────────────────
        get("/produtos", (req, res) -> {
            List<Produto> lista = dao.listarTodos();
            String mensagem = req.queryParams("msg");

            StringBuilder sb = new StringBuilder();
            sb.append(htmlHeader("Lista de Produtos"));

            // Mensagem de feedback (sucesso/erro)
            if (mensagem != null) {
                sb.append(htmlMensagem(mensagem));
            }

            sb.append("<h2>Produtos Cadastrados</h2>");
            sb.append("<a href='/produtos/novo' class='btn btn-novo'>+ Novo Produto</a>");

            if (lista.isEmpty()) {
                sb.append("<p class='vazio'>Nenhum produto cadastrado ainda.</p>");
            } else {
                sb.append("<table>");
                sb.append("<tr><th>ID</th><th>Nome</th><th>Descrição</th><th>Preço (R$)</th><th>Qtd</th><th>Ações</th></tr>");
                for (Produto p : lista) {
                    sb.append("<tr>");
                    sb.append("<td>").append(p.getId()).append("</td>");
                    sb.append("<td>").append(escHtml(p.getNome())).append("</td>");
                    sb.append("<td>").append(escHtml(p.getDescricao())).append("</td>");
                    sb.append("<td>").append(String.format("%.2f", p.getPreco())).append("</td>");
                    sb.append("<td>").append(p.getQuantidade()).append("</td>");
                    sb.append("<td class='acoes'>");
                    sb.append("<a href='/produtos/").append(p.getId()).append("/editar' class='btn btn-edit'>Editar</a> ");
                    sb.append("<a href='/produtos/").append(p.getId()).append("/excluir' class='btn btn-del' onclick=\"return confirm('Excluir produto?')\">Excluir</a>");
                    sb.append("</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
            }

            sb.append(htmlFooter());
            return sb.toString();
        });

        // ── FORMULÁRIO: novo produto ───────────────────────────────────────
        get("/produtos/novo", (req, res) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(htmlHeader("Novo Produto"));
            sb.append("<h2>Cadastrar Produto</h2>");
            sb.append(formProduto("/produtos", "POST", null));
            sb.append(htmlFooter());
            return sb.toString();
        });

        // ── CRIAR produto (lê dados do formulário HTML) ────────────────────
        post("/produtos", (req, res) -> {
            String nome      = req.queryParams("nome");
            String descricao = req.queryParams("descricao");
            String precoStr  = req.queryParams("preco");
            String qtdStr    = req.queryParams("quantidade");

            // Validação básica
            if (nome == null || nome.trim().isEmpty()) {
                res.redirect("/produtos/novo?erro=Nome+obrigatorio");
                return null;
            }

            try {
                double preco     = Double.parseDouble(precoStr.replace(",", "."));
                int    quantidade = Integer.parseInt(qtdStr);

                Produto p = new Produto(nome.trim(), descricao, preco, quantidade);
                boolean ok = dao.inserir(p);

                if (ok) {
                    res.redirect("/produtos?msg=Produto+cadastrado+com+sucesso!");
                } else {
                    res.redirect("/produtos/novo?erro=Erro+ao+cadastrar+produto");
                }
            } catch (NumberFormatException e) {
                res.redirect("/produtos/novo?erro=Preco+ou+quantidade+invalidos");
            }
            return null;
        });

        // ── FORMULÁRIO: editar produto ─────────────────────────────────────
        get("/produtos/:id/editar", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Produto p = dao.buscarPorId(id);

            if (p == null) {
                res.redirect("/produtos?msg=Produto+nao+encontrado");
                return null;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(htmlHeader("Editar Produto"));
            sb.append("<h2>Editar Produto #").append(id).append("</h2>");
            sb.append(formProduto("/produtos/" + id + "/editar", "POST", p));
            sb.append(htmlFooter());
            return sb.toString();
        });

        // ── ATUALIZAR produto ──────────────────────────────────────────────
        post("/produtos/:id/editar", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            String nome      = req.queryParams("nome");
            String descricao = req.queryParams("descricao");
            String precoStr  = req.queryParams("preco");
            String qtdStr    = req.queryParams("quantidade");

            try {
                double preco      = Double.parseDouble(precoStr.replace(",", "."));
                int    quantidade  = Integer.parseInt(qtdStr);

                Produto p = new Produto(id, nome.trim(), descricao, preco, quantidade);
                boolean ok = dao.atualizar(p);

                if (ok) {
                    res.redirect("/produtos?msg=Produto+atualizado+com+sucesso!");
                } else {
                    res.redirect("/produtos/" + id + "/editar?erro=Erro+ao+atualizar");
                }
            } catch (NumberFormatException e) {
                res.redirect("/produtos/" + id + "/editar?erro=Valores+invalidos");
            }
            return null;
        });

        // ── EXCLUIR produto ────────────────────────────────────────────────
        get("/produtos/:id/excluir", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            boolean ok = dao.excluir(id);

            if (ok) {
                res.redirect("/produtos?msg=Produto+excluido+com+sucesso!");
            } else {
                res.redirect("/produtos?msg=Erro+ao+excluir+produto");
            }
            return null;
        });

        System.out.println("======================================");
        System.out.println(" Aplicação iniciada: http://localhost:8080");
        System.out.println("======================================");
    }

    // ── Helpers HTML ──────────────────────────────────────────────────────────

    private static String htmlHeader(String titulo) {
        return "<!DOCTYPE html>\n<html lang='pt-BR'>\n<head>\n"
            + "<meta charset='UTF-8'>\n"
            + "<meta name='viewport' content='width=device-width, initial-scale=1'>\n"
            + "<title>" + titulo + " | Exercício 3</title>\n"
            + "<link rel='stylesheet' href='/css/estilo.css'>\n"
            + "</head>\n<body>\n"
            + "<header><h1>🛒 Gerenciamento de Produtos</h1>"
            + "<nav><a href='/produtos'>Lista</a> | <a href='/produtos/novo'>Novo</a></nav>"
            + "</header>\n<main>\n";
    }

    private static String htmlFooter() {
        return "</main>\n<footer><p>Exercício 3 – Spark Framework + PostgreSQL</p></footer>\n</body>\n</html>";
    }

    private static String htmlMensagem(String msg) {
        boolean erro = msg.toLowerCase().contains("erro") || msg.toLowerCase().contains("nao");
        String cls = erro ? "msg-erro" : "msg-ok";
        return "<p class='" + cls + "'>" + escHtml(msg) + "</p>";
    }

    /**
     * Gera o formulário HTML de cadastro/edição.
     * Quando produto != null, preenche os campos para edição.
     */
    private static String formProduto(String action, String method, Produto p) {
        String nome      = p != null ? escHtml(p.getNome())      : "";
        String descricao = p != null ? escHtml(p.getDescricao()) : "";
        String preco     = p != null ? String.format("%.2f", p.getPreco()) : "";
        String qtd       = p != null ? String.valueOf(p.getQuantidade())   : "";

        return "<form action='" + action + "' method='" + method + "' class='form-produto'>\n"

            + "  <div class='campo'>\n"
            + "    <label for='nome'>Nome do Produto *</label>\n"
            + "    <input type='text' id='nome' name='nome' value='" + nome + "' required maxlength='100' placeholder='Ex: Notebook Dell'>\n"
            + "  </div>\n"

            + "  <div class='campo'>\n"
            + "    <label for='descricao'>Descrição</label>\n"
            + "    <textarea id='descricao' name='descricao' rows='3' maxlength='255' placeholder='Descrição breve do produto'>" + descricao + "</textarea>\n"
            + "  </div>\n"

            + "  <div class='campo'>\n"
            + "    <label for='preco'>Preço (R$) *</label>\n"
            + "    <input type='number' id='preco' name='preco' value='" + preco + "' required min='0' step='0.01' placeholder='0.00'>\n"
            + "  </div>\n"

            + "  <div class='campo'>\n"
            + "    <label for='quantidade'>Quantidade em Estoque *</label>\n"
            + "    <input type='number' id='quantidade' name='quantidade' value='" + qtd + "' required min='0' placeholder='0'>\n"
            + "  </div>\n"

            + "  <div class='botoes'>\n"
            + "    <button type='submit' class='btn btn-novo'>Salvar</button>\n"
            + "    <a href='/produtos' class='btn btn-cancel'>Cancelar</a>\n"
            + "  </div>\n"

            + "</form>\n";
    }

    /** Escapa caracteres HTML para prevenir XSS. */
    private static String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
