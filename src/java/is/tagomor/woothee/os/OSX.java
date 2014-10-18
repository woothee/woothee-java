package is.tagomor.woothee.os;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.AgentCategory;
import is.tagomor.woothee.DataSet;
import is.tagomor.woothee.Classifier;

public class OSX extends AgentCategory {
  private static Pattern osxVer = Pattern.compile("Mac OS X (10[._]\\d+(?:[._]\\d+)?)(?:\\)|;)");
  private static Pattern iosVer = Pattern.compile("; CPU(?: iPhone)? OS (\\d+_\\d+(?:_\\d+)?) like Mac OS X");

  public static boolean challenge(final String ua, final Map<String,String> result) {
    int pos = ua.indexOf("Mac OS X");
    if (pos < 0) // not OSX
      return false;

    Map<String,String> data = DataSet.get("OSX");
    String version = null;

    if (ua.indexOf("like Mac OS X") > -1) {
      if (ua.indexOf("iPhone;") > -1)
        data = DataSet.get("iPhone");
      else if (ua.indexOf("iPad;") > -1)
        data = DataSet.get("iPad");
      else if (ua.indexOf("iPod") > -1)
        data = DataSet.get("iPod");
      Matcher ios = iosVer.matcher(ua);
      if (ios.find()) {
        version = ios.group(1).replace('_', '.');
      }
    } else {
      Matcher osx = osxVer.matcher(ua);
      if (osx.find(pos)) {
        version = osx.group(1).replace('_', '.');
      }

    }

    updateCategory(result, data.get(DataSet.DATASET_KEY_CATEGORY));
    updateOs(result, data.get(DataSet.DATASET_KEY_NAME));
    if (version != null) {
      updateOsVersion(result, version);
    }
    return true;
  }
}
