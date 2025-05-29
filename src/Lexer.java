import java.io.*;
import java.util.*;

public class Lexer {
    private final BufferedReader reader;
    private int currentChar;
    private int linha = 1;
    private int coluna = 0;
    private boolean reachedEOF = false;

    public Lexer(String filename) throws IOException {
        reader = new BufferedReader(new FileReader(filename));
        advance();
    }

    private void advance() throws IOException {
        currentChar = reader.read();
        if (currentChar == '\n') {
            linha++;
            coluna = 0;
        } else {
            coluna++;
        }
    }

    private boolean isLetter(int c) {
        return Character.isLetter(c);
    }

    private boolean isDigit(int c) {
        return Character.isDigit(c);
    }

    private boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    public TokenInfo nextToken() throws IOException {
        // isso aqui é pra se tiver algum espaço em braco
        while (!reachedEOF && isWhitespace(currentChar)) {
            advance();
        }

        if (currentChar == -1) {
            reachedEOF = true;
            return new TokenInfo("EOF", Token.EOF, linha, coluna);
        }

        int startCol = coluna;

        if (isLetter(currentChar) || currentChar == '_') {
            StringBuilder lexema = new StringBuilder();
            do {
                lexema.append((char) currentChar);
                advance();
            } while (isLetter(currentChar) || isDigit(currentChar) || currentChar == '_');

            String palavra = lexema.toString();
            switch (palavra) {
                case "main": return new TokenInfo(palavra, Token.MAIN, linha, startCol);
                case "int": return new TokenInfo(palavra, Token.INT, linha, startCol);
                case "float": return new TokenInfo(palavra, Token.FLOAT, linha, startCol);
                case "char": return new TokenInfo(palavra, Token.CHAR, linha, startCol);
                case "if": return new TokenInfo(palavra, Token.IF, linha, startCol);
                case "else": return new TokenInfo(palavra, Token.ELSE, linha, startCol);
                case "while": return new TokenInfo(palavra, Token.WHILE, linha, startCol);
                case "do": return new TokenInfo(palavra, Token.DO, linha, startCol);
                case "for": return new TokenInfo(palavra, Token.FOR, linha, startCol);
                default: return new TokenInfo(palavra, Token.IDENTIFIER, linha, startCol);
            }
        }
        return new TokenInfo(Character.toString((char) currentChar), Token.INVALID_CHAR, linha, startCol);
    }

    public void close() throws IOException {
        reader.close();
    }
}
