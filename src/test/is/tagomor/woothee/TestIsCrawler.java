package is.tagomor.woothee;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

import org.ho.yaml.Yaml;

import is.tagomor.woothee.Classifier;

public final class TestIsCrawler {

  public static String message(final String setName, final String target, final String attribute) {
    return String.format("%s test(%s): %s", setName, attribute, target);
  }

  @SuppressWarnings("unchecked")
  public void executeCheckIsCrawler(final File testSet, final String setName, final boolean should_true) throws FileNotFoundException {
    List testsets = (List) Yaml.load(testSet);
    for (Object ts : testsets) {
      Map t = (Map) ts;
      String target = (String) t.get("target");
      if (should_true && t.get("category").equals("crawler"))
        assertTrue(message(setName, target, "isCrawler"), Classifier.isCrawler(target));
      else
        assertFalse(message(setName, target, "isCrawler"), Classifier.isCrawler(target));
    }
  }

  public File getTargetFile(final String filename) {
    String basedir = System.getProperties().getProperty("testset.dirname");
    if (basedir == null) {
      basedir = new File("./woothee/testsets").getAbsolutePath();
    }
    return new File(basedir.concat("/").concat(filename));
  }

  @Test
  public void testsetCrawler() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("crawler.yaml"), "Crawler", true);
  }

  @Test
  public void testsetCrawlerGoogle() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("crawler_google.yaml"), "Crawler/Google", true);
  }

  @Test
  public void testsetPCWindows() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("pc_windows.yaml"), "PC/Windows", false);
  }

  @Test
  public void testsetPCMisc() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("pc_misc.yaml"), "PC/Misc", false);
  }

  @Test
  public void testsetMobilePhoneDocomo() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("mobilephone_docomo.yaml"), "MobilePhone/docomo", false);
  }

  @Test
  public void testsetMobilePhoneAU() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("mobilephone_au.yaml"), "MobilePhone/au", false);
  }

  @Test
  public void testsetMobilePhoneSoftbank() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("mobilephone_softbank.yaml"), "MobilePhone/softbank", false);
  }

  @Test
  public void testsetMobilePhoneWillcom() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("mobilephone_willcom.yaml"), "MobilePhone/willcom", false);
  }

  @Test
  public void testsetMobilePhoneMisc() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("mobilephone_misc.yaml"), "MobilePhone/misc", false);
  }

  @Test
  public void testsetSmartPhoneIOS() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("smartphone_ios.yaml"), "SmartPhone/ios", false);
  }

  @Test
  public void testsetSmartPhoneAndroid() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("smartphone_android.yaml"), "SmartPhone/android", false);
  }

  @Test
  public void testsetSmartPhoneMisc() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("smartphone_misc.yaml"), "SmartPhone/misc", false);
  }

  @Test
  public void testsetAppliance() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("appliance.yaml"), "Appliance", false);
  }

  @Test
  public void testsetPCLowPriority() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("pc_lowpriority.yaml"), "PC/LowPriority", false);
  }

  @Test
  public void testsetMisc() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("misc.yaml"), "Misc", false);
  }

  @Test
  public void testsetCrawlerNonMajor() throws FileNotFoundException {
    executeCheckIsCrawler(getTargetFile("crawler_nonmajor.yaml"), "Crawler/NonMajor", false);
  }
}
