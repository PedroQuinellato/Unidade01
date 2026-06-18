package com.exemplo.cognitivo;

import java.util.*;

/**
 * Ponto de entrada da aplicação.
 *
 * Demonstra o consumo do Azure AI Language Service (Análise de Sentimentos)
 * em dois modos:
 *   1. Batch demo  – analisa um conjunto de frases pré-definidas.
 *   2. Interativo  – o usuário digita frases e recebe o resultado em tempo real.
 *
 * Exercício 4 – PUC Minas | Computação em Nuvem e Serviços Cognitivos
 */
public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║   Exercício 4 – Azure AI Language: Análise de Sentimentos   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        AnaliseSentimentoService servico = new AnaliseSentimentoService();

        // ── 1. MODO BATCH: frases de demonstração ─────────────────────────
        System.out.println("► Analisando conjunto de frases de demonstração...\n");

        List<String> frasesBatch = List.of(
            "Adorei o atendimento! O produto chegou no prazo e superou minhas expectativas.",
            "Péssima experiência. O item veio com defeito e o suporte demorou uma semana para responder.",
            "O produto é razoável, nem muito bom nem ruim. Entrega dentro do prazo.",
            "Estou completamente insatisfeito. Nunca mais compro nessa loja!",
            "Ótimo custo-benefício. Recomendo muito a todos os amigos.",
            "Não sei se vale a pena. O preço é alto mas a qualidade é mediana."
        );

        List<ResultadoSentimento> resultados = servico.analisar(frasesBatch);

        System.out.println("┌─────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-12s │ %6s │ %6s │ %6s │ %-40s │%n",
                "Sentimento", "+ Pos", "~ Neu", "- Neg", "Trecho do texto");
        System.out.println("├─────────────────────────────────────────────────────────────────────────┤");

        for (ResultadoSentimento r : resultados) {
            String trecho = r.getTexto().length() > 38
                    ? r.getTexto().substring(0, 35) + "..."
                    : r.getTexto();
            System.out.printf("│ %-12s │ %5.0f%% │ %5.0f%% │ %5.0f%% │ %-38s │%n",
                    r.getSentimentoPtBr(),
                    r.getPositivo()  * 100,
                    r.getNeutro()    * 100,
                    r.getNegativo()  * 100,
                    trecho);
        }

        System.out.println("└─────────────────────────────────────────────────────────────────────────┘");
        System.out.println();

        // ── Resumo estatístico ─────────────────────────────────────────────
        imprimirResumo(resultados);

        // ── 2. MODO INTERATIVO ─────────────────────────────────────────────
        System.out.println("\n─────────────────────────────────────────────────────────");
        System.out.println("► Modo interativo — digite uma frase e pressione ENTER.");
        System.out.println("  (Digite 'sair' para encerrar)");
        System.out.println("─────────────────────────────────────────────────────────\n");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Frase > ");
            String entrada = scanner.nextLine().trim();

            if (entrada.equalsIgnoreCase("sair") || entrada.isEmpty()) {
                System.out.println("\nAplicação encerrada. Até logo!");
                break;
            }

            List<ResultadoSentimento> res = servico.analisar(List.of(entrada));
            if (!res.isEmpty()) {
                ResultadoSentimento r = res.get(0);
                System.out.println("  ↳ " + r.getSentimentoPtBr()
                    + String.format("  (Positivo: %.0f%%  Neutro: %.0f%%  Negativo: %.0f%%)",
                        r.getPositivo() * 100, r.getNeutro() * 100, r.getNegativo() * 100));
            }
            System.out.println();
        }
    }

    /** Exibe um resumo contando quantos textos caíram em cada categoria. */
    private static void imprimirResumo(List<ResultadoSentimento> resultados) {
        Map<String, Long> contagem = new LinkedHashMap<>();
        contagem.put("positive", 0L);
        contagem.put("neutral",  0L);
        contagem.put("negative", 0L);
        contagem.put("mixed",    0L);

        for (ResultadoSentimento r : resultados) {
            contagem.merge(r.getSentimento().toLowerCase(), 1L, Long::sum);
        }

        System.out.println("► Resumo dos resultados:");
        contagem.forEach((k, v) -> {
            String label = switch (k) {
                case "positive" -> "Positivos";
                case "negative" -> "Negativos";
                case "neutral"  -> "Neutros  ";
                default         -> "Mistos   ";
            };
            System.out.printf("   %s : %d texto(s)%n", label, v);
        });
    }
}
