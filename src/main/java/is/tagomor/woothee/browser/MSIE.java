package is.tagomor.woothee.browser;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class MSIE extends AgentCategory {
  private static Pattern msie = Pattern.compile("MSIE ([.0-9]+);");
  private static Pattern trident = Pattern.compile("Trident/([.0-9]+);");
  private static Pattern tridentVersion = Pattern.compile(" rv:([.0-9]+)");
  private static Pattern iemobile = Pattern.compile("IEMobile/([.0-9]+);");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    if (ua.indexOf("compatible; MSIE") >= 0 || ua.indexOf("Trident/") >= 0 || ua.indexOf("IEMobile") >= 0) {
      String version = DataSet.VALUE_UNKNOWN;

      boolean matched = false;

      Matcher mie = msie.matcher(ua);
      if (mie.find()) {
        version = mie.group(1);
        matched = true;
      }

      if (!matched) {
        Matcher tri = trident.matcher(ua);
        Matcher triVer = tridentVersion.matcher(ua);
        if (tri.find() && triVer.find()) {
          version = triVer.group(1);
          matched = true;
        }
      }
      if (!matched) {
        Matcher iem = iemobile.matcher(ua);
        if (iem.find()) {
          version = iem.group(1);
          matched = true;
        }
      }

      updateMap(result, DataSet.get("MSIE"));
      updateVersion(result, version);
      return true;
    }

    return false; // not MSIE (nor Sleipnir)
  }
}

