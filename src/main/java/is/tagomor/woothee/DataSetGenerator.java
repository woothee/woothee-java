package is.tagomor.woothee;

import java.io.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.ho.yaml.Yaml;

public final class DataSetGenerator {
  private static String TARGET_PATH = "src/main/java/is/tagomor/woothee/DataSet.java";

  // current dir is "woothee-java", maybe
  private static File CODE_HEADER = new File(new File(".").getAbsolutePath().concat("/src/misc/header_dataset.java"));
  private static File CODE_FOOTER = new File(new File(".").getAbsolutePath().concat("/src/misc/footer_dataset.java"));
  private static File SOURCE_FILE = new File(new File(".").getAbsolutePath().concat("/woothee/dataset.yaml"));

  /*
    args[0]: generated_timestamp (ex: `env LANG=C date`)
    args[1]: generated_username  (ex: `env LANG=C whoami`)
   */
  public static void main(final String args[]) throws FileNotFoundException, UnsupportedEncodingException, IOException {
    
    FileOutputStream writer = new FileOutputStream(TARGET_PATH);

    copyFromFile(CODE_HEADER, writer);

    String code = generateDataSetCode(SOURCE_FILE, args[0], args[1]);
    writer.write(code.getBytes("UTF-8"));

    copyFromFile(CODE_FOOTER, writer);

    writer.close();
  }

  public static void copyFromFile(File from, FileOutputStream output) throws FileNotFoundException, IOException {
    FileInputStream input = new FileInputStream(from);
    byte[] wbuf = new byte[2048];
    while (input.available() > 0) {
      int length = input.read(wbuf);
      output.write(wbuf, 0, length);
    }
    input.close();
  }

  @SuppressWarnings("unchecked")
  public static String generateDataSetCode(File source, String timestamp, String username) throws FileNotFoundException {
    StringBuffer buffer = new StringBuffer("");

    buffer.append(String.format("  // GENERATED from dataset.yaml at %s by %s\n", timestamp, username));
    buffer.append("  static {\n");
    buffer.append("    Map<String,String> h;\n");

    List datasetEntries = (List) Yaml.load(source);

    for (Object datasetObj : datasetEntries) {
      Map<String,String> dataset = (Map<String,String>) datasetObj;

      String label = (String) dataset.get("label");
      String type = (String) dataset.get("type");

      buffer.append("    h = new HashMap<String,String>(6, (float)1.0);\n");
      buffer.append(String.format("    h.put(DATASET_KEY_LABEL, \"%s\");\n", label));
      buffer.append(String.format("    h.put(DATASET_KEY_NAME, \"%s\");\n", dataset.get("name")));
      buffer.append(String.format("    h.put(DATASET_KEY_TYPE, \"%s\");\n", type));

      if (type.equals("browser")) {
        buffer.append(String.format("    h.put(DATASET_KEY_VENDOR, \"%s\");\n", dataset.get("vendor")));
      } else if (type.equals("os")) {
        buffer.append(String.format("    h.put(DATASET_KEY_CATEGORY, \"%s\");\n", dataset.get("category")));
      } else if (type.equals("full")) {
        buffer.append(String.format("    h.put(DATASET_KEY_VENDOR, \"%s\");\n", dataset.get("vendor")));
        buffer.append(String.format("    h.put(DATASET_KEY_CATEGORY, \"%s\");\n", dataset.get("category")));
        if (dataset.containsKey("os")) {
          buffer.append(String.format("    h.put(DATASET_KEY_OS, \"%s\");\n", dataset.get("os")));
        }
      }

      buffer.append(String.format("    DATASET.put(\"%s\", h);\n", label));
    }
    buffer.append("  }\n");

    return buffer.toString();
  }
}


