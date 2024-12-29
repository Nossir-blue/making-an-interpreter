package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static interpreter.TokenType.*;
import static interpreter.ErrorReporter.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private Integer start = 0, current = 0, line = 1;
    private static final Map<String, TokenType> keywords;

    /**
     * Constructor for the scanner to get in the source code
     * @param source
     */
    public Scanner(String source){
        this.source = source;
    }

    /**
     * This class is a Token List that scans tokens
     * its core is a loop that will scan every single token while it's not at the end
     * after it reached the end, it will add and return a new Token
     * @return
     */
    public List<Token> scanTokens(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    /**
     * this function checks if we reached the end of the source code
     * @return
     */
    private Boolean isAtEnd(){
        return current >= source.length();
    }

    /**
     * this method is called by the scanTokens method
     * it scans every single lexeme to identify a token
     * and then adds them to the token calling the addToken method
     */
    private void scanToken(){
        Character c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if(match('/')){
                    // A comment goes until the end of the lne.
                    while(peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"': string(); break;
            default:
                if(isDigit(c)){
                    number();
                } else if(isAlpha(c)){
                    identifier();
                } 
                else {
                    ErrorReporter.error(line, "Caractere inesperado");
                }
                break;
        }
    }

    static{
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    /**
     * This method will look for identifiers, in this case
     * variable names, function names, paramenter names
     * you "name" it (ba dum ts)
     */
    private void identifier(){
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(start,current);
        TokenType type = keywords.get(text);
        if(type == null) type = IDENTIFIER;

        addToken(type);
    }

    /**
     * checks if it's a letter
     * @param c
     * @return
     */
    private boolean isAlpha(Character c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= '<') || c =='_';
    }

    /**
     * checks if it has numbers and letter at the same time
     * (more like, letters and number in that order)
     * @param c
     * @return
     */
    private boolean isAlphaNumeric(Character c){
        return isAlpha(c) || isDigit(c);
    }

    /**
     * checks if it's a number
     * @param c
     * @return
     */
    private boolean isDigit(Character c){
        return c >= '0' && c <= '9';
    }

    /**
     * consumes every number
     * when it reaches a period, it will consume the dot and consume other
     * numbers
     */
    private void number(){
        while(isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while(isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * consumes string after quotation marks and checks if
     * there's a another quotation mark
     */
    private void string(){ 
        while(peek() != '"' && !isAtEnd()){
            if(peek() == '\n') line++;
            advance();
        }

        if(isAtEnd()){
            ErrorReporter.error(line, "String mal terminada");
            return;
        }
        advance();

        String value = source.substring(start + 1, current -1);
        addToken(STRING, value);
    }

    /**
     * This method will advance to a next lexeme
     * @return
     */
    private Character advance(){
        current++;
        return source.charAt(current - 1);
    }

    /**
     * adds the type to the overloaded method
     * @param type
     */
    private void addToken(TokenType type){
        addToken(type, null);
    }

    /**
     * Overcharged method wherer it gets the type and the object literal
     * adds the in the token list
     * @param type
     * @param literal
     */
    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * Checks if the expected character after an lexeme to see 
     * what kind of token it is
     */
    private Boolean match(Character expected){
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }
    /**
     * Sees if there's no more inputs and reached the end of the source
     * if not, then return the current source position
     * @return
     */
    private Character peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }
    /**
     * Checks ahead of the peek
     * @return
     */
    private Character peekNext(){
        if(current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }
}
