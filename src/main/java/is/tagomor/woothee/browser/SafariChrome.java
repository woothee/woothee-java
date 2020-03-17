package is.tagomor.woothee.browser;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class SafariChrome extends AgentCategory {
  private static Pattern edgeVerRegex = Pattern.compile("(?:Edge|Edg|EdgiOS|EdgA)/([.0-9]+)");
  private static Pattern firefoxiOSPattern = Pattern.compile("FxiOS/([.0-9]+)");
  private static Pattern chromeVerRegex = Pattern.compile("(?:Chrome|CrMo|CriOS)/([.0-9]+)");
  private static Pattern operaVerRegex = Pattern.compile("OPR/([.0-9]+)");
  private static Pattern gsaVerRegex = Pattern.compile("GSA/([.0-9]+)");
  private static Pattern safariVerRegex = Pattern.compile("Version/([.0-9]+)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    int pos = ua.indexOf("Safari/");
    if (pos < 0) // not Safari nor Chrome
      return false;

    if (ua.indexOf("Chrome") > -1 && ua.indexOf("wv") > -1) // Chrome but Android Webview
      return false;

    String version = DataSet.VALUE_UNKNOWN;

    int epos = ua.indexOf("Edg");
    if (epos > -1) {
      Matcher edge = edgeVerRegex.matcher(ua);
      if (edge.find(epos)) {
        version = edge.group(1);
        updateMap(result, DataSet.get("Edge"));
        updateVersion(result, version);
        return true;
      }
    }

    int fpos = ua.indexOf("FxiOS");
    if (fpos > -1) {
      Matcher ffox = firefoxiOSPattern.matcher(ua);
      if (ffox.find(fpos)) {
        version = ffox.group(1);
        updateMap(result, DataSet.get("Firefox"));
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

    int gsaPos = ua.indexOf("GSA");
    if (gsaPos > -1) {
      Matcher gsa = gsaVerRegex.matcher(ua);
      if (gsa.find(gsaPos)) {
        version = gsa.group(1);
        updateMap(result, DataSet.get("GSA"));
        updateVersion(result, version);
        return true;
      }
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
