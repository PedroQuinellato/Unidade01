package com.exemplo.cognitivo;

/**
 * Configurações de acesso ao Azure AI Language Service.
 *
 * Como obter os valores:
 *   1. Acesse o Portal Azure: https://portal.azure.com
 *   2. Crie um recurso "Language Service" (ou "Cognitive Services")
 *   3. Vá em "Chaves e Ponto de Extremidade" (Keys and Endpoint)
 *   4. Copie a CHAVE 1 e o ENDPOINT para as constantes abaixo.
 *
 * Substitua os valores de exemplo pelos seus antes de executar.
 */
public class AzureConfig {

    // -----------------------------------------------------------------------
    //  SUBSTITUA PELOS SEUS VALORES DO PORTAL AZURE
    // -----------------------------------------------------------------------
    public static final String ENDPOINT =
            "https://SEU-RECURSO.cognitiveservices.azure.com/";

    public static final String API_KEY =
            "SUA_CHAVE_AQUI";
    // -----------------------------------------------------------------------

    // Versão da API REST utilizada
    public static final String API_VERSION = "2023-04-01";

    private AzureConfig() {}
}
