# Exercício 3 – CRUD de Produtos com Spark Framework

Aplicação web back-end em **Java** com formulário HTML para cadastro de produtos, usando **Eclipse + Maven + PostgreSQL + Spark Framework**.

## Funcionalidades

- ✅ **Listar** todos os produtos cadastrados (tabela HTML)
- ✅ **Cadastrar** novo produto via formulário HTML (`POST`)
- ✅ **Editar** produto existente via formulário HTML (`POST`)
- ✅ **Excluir** produto com confirmação
- ✅ Mensagens de feedback (sucesso / erro)
- ✅ CSS próprio servido como arquivo estático

## Rotas HTTP

| Método | Rota                    | Descrição                        |
|--------|-------------------------|----------------------------------|
| GET    | `/`                     | Redireciona para `/produtos`     |
| GET    | `/produtos`             | Lista todos os produtos          |
| GET    | `/produtos/novo`        | Exibe formulário de cadastro     |
| POST   | `/produtos`             | Salva novo produto (formulário)  |
| GET    | `/produtos/:id/editar`  | Formulário de edição             |
| POST   | `/produtos/:id/editar`  | Atualiza produto existente       |
| GET    | `/produtos/:id/excluir` | Exclui produto                   |

## Tecnologias

| Tecnologia         | Versão       |
|--------------------|--------------|
| Java               | 11+          |
| Maven              | 3.8+         |
| Spark Framework    | 2.9.4        |
| PostgreSQL         | 14+          |
| Driver JDBC        | 42.7.3       |

## Como executar

### 1. Banco de dados PostgreSQL

```bash
# Criar o banco
createdb exercicio3

# Executar o script SQL
psql -U postgres -d exercicio3 -f banco.sql
```

### 2. Configurar a conexão

Edite o arquivo `ConexaoDB.java` com suas credenciais locais:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/exercicio3";
private static final String USUARIO = "postgres";
private static final String SENHA   = "sua_senha";
```

### 3. Compilar e executar

```bash
# Via Maven na linha de comando
mvn clean package
java -jar target/ex3-spark-produto-1.0-SNAPSHOT.jar
```

Ou no **Eclipse**: botão direito em `Main.java` → *Run As → Java Application*.

### 4. Acessar no navegador

```
http://localhost:8080
```

## Estrutura do Projeto

```
ex3-spark-produto/
├── pom.xml                          ← Dependências Maven
├── banco.sql                        ← Script de criação do banco
├── README.md
└── src/
    └── main/
        ├── java/com/exemplo/produto/
        │   ├── Main.java            ← Rotas Spark (controller)
        │   ├── Produto.java         ← Modelo (entidade)
        │   ├── ProdutoDAO.java      ← Acesso ao banco (CRUD)
        │   └── ConexaoDB.java       ← Conexão JDBC
        └── resources/
            └── public/
                └── css/
                    └── estilo.css   ← Folha de estilo
```

## Diagrama de Camadas

```
Navegador (HTML Form)
       ↓ HTTP Request (GET/POST)
   Main.java  ──── Spark Routes
       ↓
ProdutoDAO.java ──── JDBC
       ↓
 PostgreSQL DB
```

## Autoavaliação

> Insira aqui sua nota de autoavaliação conforme solicitado pela tarefa.
