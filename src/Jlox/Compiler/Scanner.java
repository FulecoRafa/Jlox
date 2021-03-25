package Jlox.Compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Jlox.Jlox;

import static Jlox.Compiler.TokenType.*;

public class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 0;

  private static final HashMap<String, TokenType> keywords;

  static {
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

  public Scanner(String source) {
    this.source = source;
  }

  public List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      // Single character
      case '(': addToken(LEFT_PAREN);break;
      case ')': addToken(RIGHT_PAREN);break;
      case '{': addToken(LEFT_BRACE);break;
      case '}': addToken(RIGHT_BRACE);break;
      case ',': addToken(COMMA);break;
      case '.': addToken(DOT);break;
      case '+': addToken(PLUS);break;
      case '-': addToken(MINUS);break;
      case ';': addToken(SEMICOLON);break;
      case '*': addToken(STAR);break;
      // One or two character
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
      // Comment or slash
      case '/':
        if (match('/')) {
          while(peek() != '\n' && !isAtEnd()) advance();
        } else {
          addToken(SLASH);
        }
        break;
      // Ignore whitespaces
      case ' ':
      case '\t':
      case '\r': break;
      // New line
      case '\n': line++; break;
      // Longer lexemes
      case '"': string(); break;
      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          Jlox.error(line, "Unexpected character.");
        }
        break;
    }
  }

  // Add token knowing only the type
  private void addToken(TokenType type) {
    addToken(type, null);
  }

  // Add token knowing type and literal
  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

  // Consume character
  private char advance() {
    return source.charAt(current++);
  }

  // Consume if match
  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }

  // Character lookahead
  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

  // Double character lookahead
  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  /* Verifications */
  // Verifies if index is out of bounds
  private boolean isAtEnd() {
    return current >= source.length();
  }

  // Verifies if character is a digit
  private boolean isDigit(char d) {
    return d>='0' && d<='9';
  }

  // Verifies if character is a valid letter character
  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
           (c == '_');
  }

  // Verifies if character is a valid name character
  private boolean isAlphaNumeric(char c) {
    return isDigit(c) || isAlpha(c);
  }

  /* Long lexeme methods */

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }

    if (isAtEnd()) {
      Jlox.error(line, "Unterminated String.");
      return;
    }

    // Closing "
    advance();

    String value = source.substring(start+1, current-1); // Trim "
    addToken(STRING, value);
  }

  private void number() {
    while (isDigit(peek())) advance();

    if (peek() == '.' && isDigit(peekNext())) {
      advance(); // consume .
      while (isDigit(peek())) advance();
    }

    addToken(NUMBER,
      Double.parseDouble(source.substring(start, current)));
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null) type = IDENTIFIER;
    addToken(type);
  }
}
