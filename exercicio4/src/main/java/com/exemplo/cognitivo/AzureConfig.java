package com.exemplo.cognitivo;

/**
 * Configurações de acesso ao Azure AI Language Service.
 * ATENÇÃO: Nunca suba chaves reais para o GitHub!
 * Substitua as constantes abaixo pelas suas credenciais locais antes de executar.
 */
public class AzureConfig {

    // Obtenha em: Portal Azure > lang-exercicio4 > Chaves e Ponto de Extremidade
    public static final String ENDPOINT =
            "https://lang-exercicio4.cognitiveservices.azure.com/";

    public static final String API_KEY =
            System.getenv().getOrDefault("AZURE_LANGUAGE_KEY", "COLOQUE_SUA_CHAVE_AQUI");

    public static final String API_VERSION = "2023-04-01";

    private AzureConfig() {}
}
