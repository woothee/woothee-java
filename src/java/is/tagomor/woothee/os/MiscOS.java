package is.tagomor.woothee.os;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;
import is.tagomor.woothee.Classifier;

public class MiscOS extends AgentCategory {
  private static Pattern mac_ppc = Pattern.compile("rv:(\\d+\\.\\d+\\.\\d+)");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    Map<String,String> data = null;
    String osVersion = null;

    if (ua.indexOf("(Win98;") > -1) {
      data = DataSet.get("Win98");
      osVersion = "98";
    }
    else if (ua.indexOf("Macintosh; U; PPC;") > -1 || ua.indexOf("Mac_PowerPC") > -1) {
      data = DataSet.get("MacOS");
      Matcher ppc = mac_ppc.matcher(ua);
      if (ppc.find()) {
        osVersion = ppc.group(1);
      }
    }
    else if (ua.indexOf("X11; FreeBSD ") > -1) {
      data = DataSet.get("BSD");
    }
    else if (ua.indexOf("X11; CrOS ") > -1) {
      data = DataSet.get("ChromeOS");
    }

    if (data != null) {
      updateCategory(result, data.get(DataSet.DATASET_KEY_CATEGORY));
      updateOs(result, data.get(DataSet.DATASET_KEY_NAME));
      if (osVersion != null) {
        updateOsVersion(result, osVersion);
      }
      return true;
    }

    return false;
  }
}
