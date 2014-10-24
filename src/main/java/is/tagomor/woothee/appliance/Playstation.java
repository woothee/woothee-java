package is.tagomor.woothee.appliance;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class Playstation extends AgentCategory {
  private static Pattern playstation3Pattern = Pattern.compile("PLAYSTATION 3;? ([.0-9]+)\\)");
  private static Pattern playstation4Pattern = Pattern.compile("PlayStation 4 ([.0-9]+)\\)");
  private static Pattern playstationPortablePattern = Pattern.compile("PSP \\(PlayStation Portable\\); ([.0-9]+)\\)");
  private static Pattern playstationVitaPattern = Pattern.compile("PlayStation Vita ([.0-9]+)\\)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    Map<String,String> data = null;
    String version = null;

    if (ua.indexOf("PSP (PlayStation Portable);") > -1) {
      data = DataSet.get("PSP");
      Matcher psp = playstationPortablePattern.matcher(ua);
      if (psp.find()) {
        version = psp.group(1);
      }
    }
    else if (ua.indexOf("PlayStation Vita") > -1) {
      data = DataSet.get("PSVita");
      Matcher vita = playstationVitaPattern.matcher(ua);
      if (vita.find()) {
        version = vita.group(1);
      }
    }
    else if (ua.indexOf("PLAYSTATION 3 ") > -1 || ua.indexOf("PLAYSTATION 3;") > -1) {
      data = DataSet.get("PS3");
      Matcher ps3 = playstation3Pattern.matcher(ua);
      if (ps3.find()) {
        version = ps3.group(1);
      }
    }
    else if (ua.indexOf("PlayStation 4 ") > -1) {
      data = DataSet.get("PS4");
      Matcher ps4 = playstation4Pattern.matcher(ua);
      if (ps4.find()) {
        version = ps4.group(1);
      }
    }

    if (data == null)
      return false;

    updateMap(result, data);
    if (version != null) {
      updateOsVersion(result, version);
    }
    return true;
  }
}
