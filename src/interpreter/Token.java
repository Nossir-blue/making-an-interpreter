package interpreter;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final Integer line;

    public Token(TokenType type, String lexeme, Object literal, Integer line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token {\n type=" + type 
                + "\n lexeme=" + lexeme
                + "\n literal=" + literal 
                + "\n}\n";
    }

    
}
