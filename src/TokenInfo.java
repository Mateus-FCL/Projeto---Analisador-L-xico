public class TokenInfo {
    private final String lexema;
    private final Token tipoToken;
    private final int linha;
    private final int coluna;

    public TokenInfo(String lexema, Token tipoToken, int linha, int coluna) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getLexema() {
        return lexema;
    }

    public Token getTipoToken() {
        return tipoToken;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    @Override
    public String toString() {
        return String.format("Token: %-20s Lexema: %-10s Linha: %d, Coluna: %d",
                tipoToken, lexema, linha, coluna);
    }
}
