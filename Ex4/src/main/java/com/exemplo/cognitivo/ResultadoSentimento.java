package com.exemplo.cognitivo;

/**
 * Representa o resultado da análise de sentimento de um texto.
 */
public class ResultadoSentimento {

    private final String texto;
    private final String sentimento;      // positive | negative | neutral | mixed
    private final double positivo;
    private final double neutro;
    private final double negativo;

    public ResultadoSentimento(String texto, String sentimento,
                               double positivo, double neutro, double negativo) {
        this.texto      = texto;
        this.sentimento = sentimento;
        this.positivo   = positivo;
        this.neutro     = neutro;
        this.negativo   = negativo;
    }

    public String getTexto()      { return texto;      }
    public String getSentimento() { return sentimento; }
    public double getPositivo()   { return positivo;   }
    public double getNeutro()     { return neutro;     }
    public double getNegativo()   { return negativo;   }

    /** Traduz o rótulo em inglês para português. */
    public String getSentimentoPtBr() {
        return switch (sentimento.toLowerCase()) {
            case "positive" -> "Positivo 😊";
            case "negative" -> "Negativo 😞";
            case "neutral"  -> "Neutro 😐";
            case "mixed"    -> "Misto 😕";
            default         -> sentimento;
        };
    }

    @Override
    public String toString() {
        return String.format(
            "Sentimento: %-12s | +%.0f%%  ~%.0f%%  -%.0f%%  | \"%s\"",
            getSentimentoPtBr(),
            positivo * 100, neutro * 100, negativo * 100,
            texto.length() > 60 ? texto.substring(0, 57) + "..." : texto
        );
    }
}
