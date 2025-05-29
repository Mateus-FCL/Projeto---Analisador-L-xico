import java.io.IOException;
import java.io.FileNotFoundException;

public class LexicalAnalyzer {

    public static void main(String[] args) {
    if (args.length != 1) {
        System.out.println("Uso: java LexicalAnalyzer <arquivo>");
        return;
    }

    try {
        Lexer lexer = new Lexer(args[0]);
        TokenInfo token;
        do {
            token = lexer.nextToken();
            System.out.printf("Token: %-15s Lexema: %-10s Linha: %-3d Coluna: %-3d%n",
                token.tipoToken, token.lexema, token.linha, token.coluna);
        } while (token.tipoToken != Token.EOF);
        System.out.println("Análise léxica concluída com sucesso!");
    } catch (IOException e) {
        System.err.println("Erro ao ler o arquivo: " + e.getMessage());
    }
}
}
