package Jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import Jlox.Compiler.*;

public class Jlox {
  static boolean hadError = false;
  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  public static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));
    if (hadError) System.exit(65);
  }

  public static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    while (true) {
      System.out.print("Jlox: $ ");
      String line = reader.readLine();
      if (line == null) break;
      run(line);
      hadError = false;
    }

    System.out.println("\nJlox: Bye bye!");
  }

  public static void run(String code) throws IOException {
    Scanner scanner = new Scanner(code);
    List<Token> tokens = scanner.scanTokens();
    Parser parser = new Parser(tokens);
    Expr expression = parser.parse();

    if (hadError) return;
    System.out.println(new AstPrinter().print(expression));
  }

  public static void error(int line, String message) {
    report(line, "", message);
  }

  public static void report(int line, String where, String message) {
    System.err.println(
      "[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }

  public static void error(Token token, String message) {
    if (token.type == TokenType.EOF)
      report(token.line, " at end", message);
    else
      report(token.line, " at '" + token.lexeme + "'", message);
  }
}
