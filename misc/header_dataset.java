package is.tagomor.woothee;

import java.util.Map;
import java.util.HashMap;

public final class DataSet {
  public static final String DATASET_KEY_LABEL = "label";
  public static final String DATASET_KEY_NAME = "name";
  public static final String DATASET_KEY_TYPE = "type";
  public static final String DATASET_KEY_CATEGORY = "category";
  public static final String DATASET_KEY_OS = "os";
  public static final String DATASET_KEY_VENDOR = "vendor";
  public static final String DATASET_KEY_VERSION = "version";

  public static final String DATASET_TYPE_BROWSER = "browser";
  public static final String DATASET_TYPE_OS = "os";
  public static final String DATASET_TYPE_FULL = "full";

  public static final String DATASET_CATEGORY_PC = "pc";
  public static final String DATASET_CATEGORY_SMARTPHONE = "smartphone";
  public static final String DATASET_CATEGORY_MOBILEPHONE = "mobilephone";
  public static final String DATASET_CATEGORY_CRAWLER = "crawler";
  public static final String DATASET_CATEGORY_APPLIANCE = "appliance";
  public static final String DATASET_CATEGORY_MISC = "misc";

  public static final String ATTRIBUTE_NAME = "name";
  public static final String ATTRIBUTE_CATEGORY = "category";
  public static final String ATTRIBUTE_OS = "os";
  public static final String ATTRIBUTE_VENDOR = "vendor";
  public static final String ATTRIBUTE_VERSION = "version";
  public static final String VALUE_UNKNOWN = "UNKNOWN";

  public static final String[] CATEGORY_LIST = {
    DATASET_CATEGORY_PC, DATASET_CATEGORY_SMARTPHONE, DATASET_CATEGORY_MOBILEPHONE,
    DATASET_CATEGORY_CRAWLER, DATASET_CATEGORY_APPLIANCE, DATASET_CATEGORY_MISC, VALUE_UNKNOWN
  };
  public static final String[] ATTRIBUTE_LIST = {
    ATTRIBUTE_NAME, ATTRIBUTE_CATEGORY, ATTRIBUTE_OS, ATTRIBUTE_VENDOR, ATTRIBUTE_VERSION
  };

  private static final Map<String,Map<String,String>> DATASET = new HashMap<String,Map<String,String>>();

