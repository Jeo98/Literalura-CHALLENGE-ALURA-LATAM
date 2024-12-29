package Literalura.CHALLENGE.ALURA.Literalura.modelos;

public enum Idiomas {

    ESPAÑOL("es","español"),
    INGLES ("en","ingles"),
    FRANCES("fr", "frances"),
    PORTUGUES("pt","portugues"),
    LATIN("la", "latin"),
    ALEMAN("de", "aleman"),
    ITALIANO("it", "italiano");

    private String langLiter;
    private String languageLiter;

    Idiomas(String langLiter, String languageLiter) {

        this.langLiter = langLiter;
        this.languageLiter = languageLiter;
    }

    public static Idiomas fromString(String text) {

        for (Idiomas language: Idiomas.values()) {
            if (language.langLiter.equalsIgnoreCase(text)) {
                return language;
            }
        }

        throw new IllegalArgumentException();
    }

    public static Idiomas fromTotalString(String text) {

        for (Idiomas language: Idiomas.values()) {
            if (language.languageLiter.equalsIgnoreCase(text)) {
                return language;
            }
        }
        throw new IllegalArgumentException();
    }
}

