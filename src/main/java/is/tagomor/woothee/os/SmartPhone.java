package is.tagomor.woothee.os;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class SmartPhone extends AgentCategory {
  private static Pattern firefoxOsPattern = Pattern.compile("^Mozilla/[.0-9]+ \\((?:Mobile|Tablet);(?:.*;)? rv:([.0-9]+)\\) Gecko/[.0-9]+ Firefox/[.0-9]+$");
  private static Pattern blackberry10Pattern = Pattern.compile("BB10(?:.+)Version/([.0-9]+)");
  private static Pattern blackberryPattern = Pattern.compile("BlackBerry(?:\\d+)/([.0-9]+) ");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    
    Map<String,String> data = null;
    String version = null;

    if (ua.indexOf("iPhone") > -1)
      data = DataSet.get("iPhone");
    else if (ua.indexOf("iPad") > -1)
      data = DataSet.get("iPad");
    else if (ua.indexOf("iPod") > -1)
      data = DataSet.get("iPod");
    else if (ua.indexOf("Android") > -1)
      data = DataSet.get("Android");
    else if (ua.indexOf("CFNetwork") > -1)
      data = DataSet.get("iOS");
    else if (ua.indexOf("BB10") > -1) {
      data = DataSet.get("BlackBerry10");
      Matcher bb10 = blackberry10Pattern.matcher(ua);
      if (bb10.find()) {
        version = bb10.group(1);
      }
    }
    else if (ua.indexOf("BlackBerry") > -1) {
      data = DataSet.get("BlackBerry");
      Matcher blackberry = blackberryPattern.matcher(ua);
      if (blackberry.find()) {
        version = blackberry.group(1);
      }
    }

    if (result.containsKey(DataSet.DATASET_KEY_NAME) && result.get(DataSet.DATASET_KEY_NAME) == DataSet.get("Firefox").get(DataSet.DATASET_KEY_NAME)) {
      // Firefox OS specific pattern
      // http://lawrencemandel.com/2012/07/27/decision-made-firefox-os-user-agent-string/
      // https://github.com/woothee/woothee/issues/2
      Matcher firefoxOs = firefoxOsPattern.matcher(ua);
      if (firefoxOs.find()) {
        data = DataSet.get("FirefoxOS");
        version = firefoxOs.group(1);
      }
    }

    if (data == null)
      return false;

    updateCategory(result, data.get(DataSet.DATASET_KEY_CATEGORY));
    updateOs(result, data.get(DataSet.DATASET_KEY_NAME));
    if (version != null) {
      updateOsVersion(result, version);
    }
    return true;
  }
}
