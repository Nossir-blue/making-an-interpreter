package interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static interpreter.ErrorReporter.*;

public class App {

    public static void main(String[] args) throws Exception {
        if(args.length > 1){
            System.out.println("Usage: jLox [script]");;
            System.exit(64);
        } else if (args.length == 1){
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
    /**
     * este método lê o ficheiro pelo terminal através do caminho fornecido
     * @param path
     * @throws IOException
     */
    public static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(hadError) System.exit(65);
    }
    /**
     * Este método lê dados de entrada pelo interpretador
     * @throws IOException
     */
    public static void runPrompt() throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReadereader = new BufferedReader(inputStreamReader);
        for(;;){
            System.out.print("> ");
            String line = bufferedReadereader.readLine();
            if(line.isEmpty()) break;
            run(line);
            hadError = false;
        }
    }
    /**
     * método que recebe o fluxo de dados, scaneia e põe na lista de tokens
     * @param source
     */
    public static void run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token : tokens){
            System.out.println(token);
        }
    }   

}
