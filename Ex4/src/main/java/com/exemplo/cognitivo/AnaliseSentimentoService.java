package com.exemplo.cognitivo;

import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Serviço que consome a API REST do Azure AI Language
 * para realizar Análise de Sentimentos (Sentiment Analysis).
 *
 * Documentação oficial:
 * https://learn.microsoft.com/azure/ai-services/language-service/sentiment-opinion-mining/quickstart
 */
public class AnaliseSentimentoService {

    private static final String PATH =
            "/language/:analyze-text?api-version=" + AzureConfig.API_VERSION;

    private final HttpClient httpClient;

    public AnaliseSentimentoService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    /**
     * Analisa o sentimento de uma lista de textos.
     *
     * @param textos Lista de textos a serem analisados.
     * @return Lista de ResultadoSentimento na mesma ordem dos textos.
     * @throws Exception em caso de erro de comunicação com a API.
     */
    public List<ResultadoSentimento> analisar(List<String> textos) throws Exception {

        // ── Monta o corpo JSON da requisição ─────────────────────────────
        JsonObject corpo = new JsonObject();
        corpo.addProperty("kind", "SentimentAnalysis");

        JsonObject analysisInput = new JsonObject();
        JsonArray  documents     = new JsonArray();

        for (int i = 0; i < textos.size(); i++) {
            JsonObject doc = new JsonObject();
            doc.addProperty("id", String.valueOf(i + 1));
            doc.addProperty("language", "pt");           // Português
            doc.addProperty("text", textos.get(i));
            documents.add(doc);
        }

        analysisInput.add("documents", documents);
        corpo.add("analysisInput", analysisInput);

        JsonObject parameters = new JsonObject();
        parameters.addProperty("opinionMining", true);   // extrai aspectos detalhados
        corpo.add("parameters", parameters);

        String corpoJson = new Gson().toJson(corpo);

        // ── Envia a requisição HTTP ───────────────────────────────────────
        URI uri = new URI(AzureConfig.ENDPOINT + PATH);

        HttpRequest requisicao = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Ocp-Apim-Subscription-Key", AzureConfig.API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(corpoJson, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resposta =
                httpClient.send(requisicao, HttpResponse.BodyHandlers.ofString());

        if (resposta.statusCode() != 200) {
            throw new RuntimeException(
                "Erro na chamada à API Azure [HTTP " + resposta.statusCode() + "]: "
                + resposta.body());
        }

        // ── Interpreta a resposta JSON ────────────────────────────────────
        return parseResposta(resposta.body(), textos);
    }

    private List<ResultadoSentimento> parseResposta(String json, List<String> textos) {
        List<ResultadoSentimento> resultados = new ArrayList<>();
        JsonObject raiz = JsonParser.parseString(json).getAsJsonObject();

        JsonArray docs = raiz
                .getAsJsonObject("results")
                .getAsJsonArray("documents");

        for (int i = 0; i < docs.size(); i++) {
            JsonObject doc        = docs.get(i).getAsJsonObject();
            String     sentimento = doc.get("sentiment").getAsString();

            JsonObject scores  = doc.getAsJsonObject("confidenceScores");
            double positivo    = scores.get("positive").getAsDouble();
            double neutro      = scores.get("neutral").getAsDouble();
            double negativo    = scores.get("negative").getAsDouble();

            // Recupera o texto original pelo índice (id começa em 1)
            int idx  = Integer.parseInt(doc.get("id").getAsString()) - 1;
            String t = idx < textos.size() ? textos.get(idx) : "";

            resultados.add(new ResultadoSentimento(t, sentimento, positivo, neutro, negativo));
        }

        return resultados;
    }
}
