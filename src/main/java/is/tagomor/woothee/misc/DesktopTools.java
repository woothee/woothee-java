package is.tagomor.woothee.misc;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;

public class DesktopTools extends AgentCategory {
  public static boolean challenge(final String ua, final Map<String,String> result) {
    Map<String,String> data = null;

    if (ua.indexOf("AppleSyndication/") > -1) {
      data = DataSet.get("SafariRSSReader");
    }
    else if (ua.indexOf("compatible; Google Desktop/") > -1) {
      data = DataSet.get("GoogleDesktop");
    }
    else if (ua.indexOf("Windows-RSS-Platform") > -1) {
      data = DataSet.get("WindowsRSSReader");
    }

    if (data == null)
      return false;

    updateMap(result, data);
    return true;
  }
}
