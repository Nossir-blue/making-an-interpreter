package interpreter;

public class ErrorReporter {
    
    static void error(int line, String message){
        report(line, "", message);
    }

    private static void report(int line, String where, String message){
        System.err.println("[Line " + line + "] Error" + where + ": " + message);
    }
}
