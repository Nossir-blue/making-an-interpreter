package interpreter;

public class ErrorReporter {
    
    static void error(int line, String message){
        report(line, "", message);
    }

    private static void report(int line, String where, String message){
        System.err.println("[Line " + line + "] Error" + where + ": " + message);
    }

    static void error(Token token, String message){
        if(token.type == TokenType.EOF){
            report(token.line, " at end", message);
        } else {
            report(token.line, " at " + token.lexeme, message);
        }
    }
}
