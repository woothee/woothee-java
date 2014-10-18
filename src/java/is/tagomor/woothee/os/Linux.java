package is.tagomor.woothee.os;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;
import is.tagomor.woothee.Classifier;

public class Linux extends AgentCategory {
  private static Pattern androidOs = Pattern.compile("Android[- ](\\d+\\.\\d+(?:\\.\\d+)?)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    int pos = ua.indexOf("Linux");
    if (pos < 0) // not Linux
      return false;

    Map<String,String> data = DataSet.get("Linux");
    String version = null;

    if (ua.indexOf("Android") > -1) {
      data = DataSet.get("Android");
      Matcher android = androidOs.matcher(ua);
      if (android.find(pos)) {
        version = android.group(1);
      }
    }

    updateCategory(result, data.get(DataSet.DATASET_KEY_CATEGORY));
    updateOs(result, data.get(DataSet.DATASET_KEY_NAME));
    if (version != null) {
      updateOsVersion(result, version);
    }
    return true;
  }
}
