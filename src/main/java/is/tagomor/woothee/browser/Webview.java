package is.tagomor.woothee.browser;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class Webview extends AgentCategory {
  private static Pattern osxLikeRegex = Pattern.compile("iP(?:hone;|ad;|od) .*like Mac OS X");
  private static Pattern osVersion = Pattern.compile("Version/([.0-9]+)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    int pos = ua.indexOf("like Mac OS X");
    if (pos < 0)
      return false;

    String version = DataSet.VALUE_UNKNOWN;

    Matcher webview = osxLikeRegex.matcher(ua);
    if (webview.find()) {
      Matcher v = osVersion.matcher(ua);
      if (v.find()) {
        version = v.group(1);
      }
      updateMap(result, DataSet.get("Webview"));
      updateVersion(result, version);
      return true;
    }
    return false;
  }
}
