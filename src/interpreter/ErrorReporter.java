package interpreter;

public class ErrorReporter {
    static boolean hadError = false;

    // error handling
    static void error(int line, String message){
        report(line, "", message);
    }
    
    private static void report(int line, String where, String message){
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
