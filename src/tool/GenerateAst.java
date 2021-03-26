package tool;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }
    String outputDir = args[0];
    defineAst(outputDir, "Expr", Arrays.asList(
      "Binary   :   Expr left, Token operator, Expr right",
      "Grouping :   Expr expression",
      "Literal  :   Object value",
      "Unary    :   Token operator, Expr right"
    ));
  }

  private static void defineAst
    (String outputDir, String basename, List<String> types)
    throws IOException {
      String path = outputDir + "/" + basename + ".java";
      PrintWriter writer = new PrintWriter(path, "UTF-8");
      writer.println("package Jlox.Compiler;\n");
      writer.println("import java.util.List;\n");
      writer.println("abstract class " + basename + " {");

      defineVisitor(writer, basename, types);

      for (String type: types) {
        String className = type.split(":")[0].trim();
        String fields = type.split(":")[1].trim();
        defineType(writer, basename, className, fields);
      }

      writer.println("\n  abstract <R> R accept (Visitor<R> visitor);");

      writer.println("}");
      writer.close();
  }

  private static void defineType
    (PrintWriter writer, String basename, String className, String fieldList) {
      writer.println("  static class " + className + " extends " + basename + " {");
      writer.println("    " + className + " (" + fieldList + ") {");

      String[] fields = fieldList.split(", ");
      for (String field : fields) {
        String name = field.split(" ")[1];
        writer.println("      this." + name + " = " + name + ";");
      }
      writer.println("    }\n");

      writer.println("    @Override");
      writer.println("    <R> R accept (Visitor<R> visitor) {");
      writer.println("      return visitor.visit" + className + basename +"(this);");
      writer.println("    }");

      for (String field : fields) {
        writer.println("    final " + field + ";");
      }
      writer.println("  }");
  }

  private static void defineVisitor(PrintWriter writer, String basename, List<String> types) {
    writer.println("  interface Visitor<R> {");

    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + basename + " (" + typeName + " " + basename.toLowerCase() + ");");
    }

    writer.println("  }");
  }
}
