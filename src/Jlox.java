public class Jlox {
  public static void main(String[] args) {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  public static void runFile(String path) {
    System.out.println("Running file " + path);
  }

  public static void runPrompt() {
    System.out.println("Jlox: $ ");
  }
}
