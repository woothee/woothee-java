package is.tagomor.woothee.browser;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class YandexBrowser extends AgentCategory {
  private static Pattern yandexBrowserRegex = Pattern.compile("YaBrowser/([.0-9]+)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    int pos = ua.indexOf("YaBrowser/");
    if (pos < 0) // not YaBrowser
      return false;

    String version = DataSet.VALUE_UNKNOWN;

    Matcher matched = yandexBrowserRegex.matcher(ua);
    if (matched.find(pos)) {
      version = matched.group(1);
    }
    updateMap(result, DataSet.get("YaBrowser"));
    updateVersion(result, version);
    return true;
  }
}
