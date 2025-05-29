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

        if (isDigit(currentChar)) {
            StringBuilder lexema = new StringBuilder();
            boolean isFloat = false;
            do {
                lexema.append((char) currentChar);
                advance();
            } while (isDigit(currentChar));

            if (currentChar == '.') {
                isFloat = true;
                lexema.append('.');
                advance();
                if (!isDigit(currentChar)) {
                    return new TokenInfo(lexema.toString(), Token.INVALID_FLOAT, linha, startCol);
                }
                while (isDigit(currentChar)) {
                    lexema.append((char) currentChar);
                    advance();
                }
            }

            return new TokenInfo(lexema.toString(),
                    isFloat ? Token.FLOAT_CONST : Token.INTEGER_CONST,
                    linha, startCol);
        }

        if (currentChar == '\'') {
            StringBuilder lexema = new StringBuilder();
            lexema.append((char) currentChar);
            advance();

            if (currentChar == '\'' || currentChar == '\n' || currentChar == -1) {
                return new TokenInfo(lexema.toString(), Token.INVALID_CHAR_CONST, linha, startCol);
            }

            lexema.append((char) currentChar);
            advance();

            if (currentChar != '\'') {
                return new TokenInfo(lexema.toString(), Token.INVALID_CHAR_CONST, linha, startCol);
            }

            lexema.append((char) currentChar);
            advance();

            return new TokenInfo(lexema.toString(), Token.CHAR_CONST, linha, startCol);
        }

        if (currentChar == '/') {
            advance();
            if (currentChar == '/') {
                while (currentChar != '\n' && currentChar != -1) {
                    advance();
                }
                return new TokenInfo("//", Token.LINE_COMMENT, linha, startCol);
            } else if (currentChar == '*') {
                advance();
                while (true) {
                    if (currentChar == -1) {
                        return new TokenInfo("/*", Token.UNTERMINATED_COMMENT, linha, startCol);
                    } else if (currentChar == '*') {
                        advance();
                        if (currentChar == '/') {
                            advance();
                            return new TokenInfo("/*...*/", Token.BLOCK_COMMENT, linha, startCol);
                        }
                    } else {
                        advance();
                    }
                }
            } else {
                return new TokenInfo("/", Token.DIVIDE, linha, startCol);
            }
        }

        switch (currentChar) {
            case '+': advance(); return new TokenInfo("+", Token.PLUS, linha, startCol);
            case '-': advance(); return new TokenInfo("-", Token.MINUS, linha, startCol);
            case '*': advance(); return new TokenInfo("*", Token.MULTIPLY, linha, startCol);
            case '<':
                advance();
                if (currentChar == '=') {
                    advance();
                    return new TokenInfo("<=", Token.LESS_EQUAL, linha, startCol);
                }
                return new TokenInfo("<", Token.LESS_THAN, linha, startCol);
            case '>':
                advance();
                if (currentChar == '=') {
                    advance();
                    return new TokenInfo(">=", Token.GREATER_EQUAL, linha, startCol);
                }
                return new TokenInfo(">", Token.GREATER_THAN, linha, startCol);
            case '=':
                advance();
                if (currentChar == '=') {
                    advance();
                    return new TokenInfo("==", Token.EQUAL, linha, startCol);
                }
                return new TokenInfo("=", Token.INVALID_CHAR, linha, startCol);
            case '!':
                advance();
                if (currentChar == '=') {
                    advance();
                    return new TokenInfo("!=", Token.NOT_EQUAL, linha, startCol);
                }
                return new TokenInfo("!", Token.ISOLATED_EXCLAMATION, linha, startCol);
        }

        char ch = (char) currentChar;
        advance();
        switch (ch) {
            case '(': return new TokenInfo("(", Token.LPAREN, linha, startCol);
            case ')': return new TokenInfo(")", Token.RPAREN, linha, startCol);
            case '{': return new TokenInfo("{", Token.LBRACE, linha, startCol);
            case '}': return new TokenInfo("}", Token.RBRACE, linha, startCol);
            case ',': return new TokenInfo(",", Token.COMMA, linha, startCol);
            case ';': return new TokenInfo(";", Token.SEMICOLON, linha, startCol);
        }
        return new TokenInfo(Character.toString((char) currentChar), Token.INVALID_CHAR, linha, startCol);
    }

    public void close() throws IOException {
        reader.close();
    }
}
