# Exercício 4 – Azure Cognitive Services: Análise de Sentimentos

Aplicação Java que consome o **Azure AI Language Service** para realizar
**Análise de Sentimentos** em textos em português.

---

## Produto 3 – Serviço Cognitivo utilizado

**Azure AI Language — Sentiment Analysis**

Serviço que classifica textos em: `Positivo`, `Negativo`, `Neutro` ou `Misto`,
retornando também a confiança (%) de cada classificação.

---

## Tecnologias

| Recurso              | Detalhe                                    |
|----------------------|--------------------------------------------|
| Linguagem            | Java 11+                                   |
| Build                | Maven                                      |
| Serviço Azure        | Azure AI Language (Text Analytics v5)      |
| API REST             | `POST /language/:analyze-text`             |
| Biblioteca HTTP      | `java.net.http.HttpClient` (nativo Java 11)|
| Parse JSON           | Gson 2.10.1                                |

---

## Passo a passo para configurar o Azure

### 1. Criar o recurso no Azure Portal

1. Acesse [portal.azure.com](https://portal.azure.com)
2. Clique em **Criar um recurso**
3. Pesquise **"Language Service"** e clique em **Criar**
4. Preencha:
   - **Assinatura**: Azure for Students
   - **Grupo de recursos**: crie um novo (ex: `rg-exercicio4`)
   - **Região**: Brazil South (ou East US)
   - **Nome**: escolha um nome único (ex: `lang-pucminas-seunome`)
   - **Tipo de preço**: **F0 (Gratuito)** ← importante!
5. Clique em **Revisar + criar** → **Criar**

### 2. Obter as credenciais

1. Após a criação, vá ao recurso
2. No menu esquerdo, clique em **Chaves e Ponto de Extremidade**
3. Copie:
   - **Chave 1** → cole em `AzureConfig.API_KEY`
   - **Ponto de extremidade** → cole em `AzureConfig.ENDPOINT`

### 3. Configurar o projeto Java

Edite o arquivo `AzureConfig.java`:

```java
public static final String ENDPOINT = "https://SEU-RECURSO.cognitiveservices.azure.com/";
public static final String API_KEY  = "sua_chave_aqui";
```

---

## Como executar

```bash
# 1. Compilar
mvn clean package

# 2. Executar
java -jar target/exercicio4-cognitivo-1.0-SNAPSHOT.jar
```

No **Eclipse**: botão direito em `Main.java` → *Run As → Java Application*

---

## Exemplo de saída

```
╔══════════════════════════════════════════════════════════════╗
║   Exercício 4 – Azure AI Language: Análise de Sentimentos   ║
╚══════════════════════════════════════════════════════════════╝

► Analisando conjunto de frases de demonstração...

┌─────────────────────────────────────────────────────────────────────────┐
│ Sentimento   │  + Pos │  ~ Neu │  - Neg │ Trecho do texto              │
├─────────────────────────────────────────────────────────────────────────┤
│ Positivo 😊  │   99%  │    0%  │    0%  │ Adorei o atendimento! O pro... │
│ Negativo 😞  │    0%  │    1%  │   99%  │ Péssima experiência. O item... │
│ Neutro 😐    │   12%  │   77%  │   11%  │ O produto é razoável, nem m... │
│ Negativo 😞  │    0%  │    0%  │  100%  │ Estou completamente insatis... │
│ Positivo 😊  │   98%  │    1%  │    1%  │ Ótimo custo-benefício. Reco... │
│ Misto 😕     │   45%  │   10%  │   45%  │ Não sei se vale a pena. O p... │
└─────────────────────────────────────────────────────────────────────────┘

► Resumo dos resultados:
   Positivos : 2 texto(s)
   Negativos : 2 texto(s)
   Neutros   : 1 texto(s)
   Mistos    : 1 texto(s)

─────────────────────────────────────────────────────────
► Modo interativo — digite uma frase e pressione ENTER.
  (Digite 'sair' para encerrar)
─────────────────────────────────────────────────────────

Frase > O curso está muito bem organizado, aprendi bastante!
  ↳ Positivo 😊  (Positivo: 98%  Neutro: 1%  Negativo: 1%)
```

---

## Estrutura do Projeto

```
exercicio4/
├── pom.xml
├── README.md
├── capturas/
│   ├── 01-portal-azure-usuario.png          ← Produto 1
│   ├── 02-azure-postgresql-recurso.png      ← Produto 2 (i)
│   ├── 03-azure-postgresql-query.png        ← Produto 2 (ii)
│   └── 04-azure-language-recurso.png        ← Produto 3 (i)
└── src/main/java/com/exemplo/cognitivo/
    ├── AzureConfig.java                     ← Credenciais Azure
    ├── ResultadoSentimento.java             ← Modelo de dados
    ├── AnaliseSentimentoService.java        ← Chamada à API REST
    └── Main.java                            ← Ponto de entrada
```

---

## Como funciona a API (fluxo)

```
Main.java
   │
   ▼
AnaliseSentimentoService.analisar(textos)
   │  Monta JSON com os textos
   │  POST https://<endpoint>/language/:analyze-text?api-version=2023-04-01
   │  Header: Ocp-Apim-Subscription-Key: <chave>
   │
   ▼
Azure AI Language Service
   │  Processa em português (language: "pt")
   │  Retorna JSON com sentimento + scores de confiança
   │
   ▼
ResultadoSentimento[]
   │  Sentimento: positive | negative | neutral | mixed
   │  Scores: positivo%, neutro%, negativo%
   │
   ▼
Exibição no console (tabela formatada + modo interativo)
```

---

## Referências

- [Azure AI Language – Quickstart](https://learn.microsoft.com/azure/ai-services/language-service/sentiment-opinion-mining/quickstart)
- [Azure for Students](https://azure.microsoft.com/pt-br/free/students/)
- [Documentação da API REST](https://learn.microsoft.com/rest/api/language/text-analysis-runtime/analyze-text)
