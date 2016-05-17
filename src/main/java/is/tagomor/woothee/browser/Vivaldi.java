package is.tagomor.woothee.browser;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class Vivaldi extends AgentCategory {
  private static Pattern vivaldiRegex = Pattern.compile("Vivaldi/([.0-9]+)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    int pos = ua.indexOf("Vivaldi/");
    if (pos < 0) // not Vivaldi
      return false;

    String version = DataSet.VALUE_UNKNOWN;

    Matcher vivaldi = vivaldiRegex.matcher(ua);
    if (vivaldi.find(pos)) {
      version = vivaldi.group(1);
    }
    updateMap(result, DataSet.get("Vivaldi"));
    updateVersion(result, version);
    return true;
  }
}
