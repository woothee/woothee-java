package is.tagomor.woothee.browser;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class SafariChrome extends AgentCategory {
  private static Pattern edgeVerRegex = Pattern.compile("Edge/([.0-9]+)");
  private static Pattern chromeVerRegex = Pattern.compile("(?:Chrome|CrMo|CriOS)/([.0-9]+)");
  private static Pattern operaVerRegex = Pattern.compile("OPR/([.0-9]+)");
  private static Pattern safariVerRegex = Pattern.compile("Version/([.0-9]+)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    int pos = ua.indexOf("Safari/");
    if (pos < 0) // not Safari nor Chrome
      return false;

    String version = DataSet.VALUE_UNKNOWN;

    int epos = ua.indexOf("Edge");
    if (epos > -1) {
      Matcher edge = edgeVerRegex.matcher(ua);
      if (edge.find(epos)) {
        version = edge.group(1);
        updateMap(result, DataSet.get("Edge"));
        updateVersion(result, version);
        return true;
      }
    }

    int cpos = ua.indexOf("Chrome");
    if (cpos < 0)
      cpos = ua.indexOf("CrMo");
    if (cpos < 0)
      cpos = ua.indexOf("CriOS");
    if (cpos > -1) {
      int opos = ua.indexOf("OPR");
      if (opos > -1) {
        Matcher opera = operaVerRegex.matcher(ua);
        if (opera.find(opos)) {
          version = opera.group(1);
          updateMap(result, DataSet.get("Opera"));
          updateVersion(result, version);
          return true;
        }
      }

      // Chrome
      Matcher chrome = chromeVerRegex.matcher(ua);
      if (chrome.find(cpos))
        version = chrome.group(1);
      updateMap(result, DataSet.get("Chrome"));
      updateVersion(result, version);
      return true;
    }

    // Safari (PC/Mobile)
    Matcher safari = safariVerRegex.matcher(ua);
    if (safari.find())
      version = safari.group(1);
    updateMap(result, DataSet.get("Safari"));
    updateVersion(result, version);
    return true;
  }
}
