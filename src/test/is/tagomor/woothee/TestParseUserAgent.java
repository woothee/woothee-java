package is.tagomor.woothee;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

import org.ho.yaml.Yaml;

import is.tagomor.woothee.Classifier;

public final class TestParseUserAgent {

  public static String message(final String setName, final String target, final String attribute) {
    return String.format("%s test(%s): %s", setName, attribute, target);
  }

  @SuppressWarnings("unchecked")
  public void executeTestSet(final File testSet, final String setName) throws FileNotFoundException {
    List testsets = (List) Yaml.load(testSet);
    for (Object ts : testsets) {
      Map t = (Map) ts;
      String target = (String) t.get("target");
      Map r = Classifier.parse(target);
      assertEquals(message(setName, target, "name"), t.get("name"), r.get("name"));
      assertEquals(message(setName, target, "category"), t.get("category"), r.get("category"));
      if (t.get("os") != null)
        assertEquals(message(setName, target, "os"), t.get("os"), r.get("os"));
      if (t.get("os_version") != null)
        assertEquals(message(setName, target, "os_version"), t.get("os_version"), r.get("os_version"));
      if (t.get("version") != null)
        assertEquals(message(setName, target, "version"), t.get("version"), r.get("version"));
      if (t.get("vendor") != null)
        assertEquals(message(setName, target, "vendor"), t.get("vendor"), r.get("vendor"));
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
    executeTestSet(getTargetFile("crawler.yaml"), "Crawler");
  }

  @Test
  public void testsetCrawlerGoogle() throws FileNotFoundException {
    executeTestSet(getTargetFile("crawler_google.yaml"), "Crawler/Google");
  }

  @Test
  public void testsetPCWindows() throws FileNotFoundException {
    executeTestSet(getTargetFile("pc_windows.yaml"), "PC/Windows");
  }

  @Test
  public void testsetPCMisc() throws FileNotFoundException {
    executeTestSet(getTargetFile("pc_misc.yaml"), "PC/Misc");
  }

  @Test
  public void testsetMobilePhoneDocomo() throws FileNotFoundException {
    executeTestSet(getTargetFile("mobilephone_docomo.yaml"), "MobilePhone/docomo");
  }

  @Test
  public void testsetMobilePhoneAU() throws FileNotFoundException {
    executeTestSet(getTargetFile("mobilephone_au.yaml"), "MobilePhone/au");
  }

  @Test
  public void testsetMobilePhoneSoftbank() throws FileNotFoundException {
    executeTestSet(getTargetFile("mobilephone_softbank.yaml"), "MobilePhone/softbank");
  }

  @Test
  public void testsetMobilePhoneWillcom() throws FileNotFoundException {
    executeTestSet(getTargetFile("mobilephone_willcom.yaml"), "MobilePhone/willcom");
  }

  @Test
  public void testsetMobilePhoneMisc() throws FileNotFoundException {
    executeTestSet(getTargetFile("mobilephone_misc.yaml"), "MobilePhone/misc");
  }

  @Test
  public void testsetSmartPhoneIOS() throws FileNotFoundException {
    executeTestSet(getTargetFile("smartphone_ios.yaml"), "SmartPhone/ios");
  }

  @Test
  public void testsetSmartPhoneAndroid() throws FileNotFoundException {
    executeTestSet(getTargetFile("smartphone_android.yaml"), "SmartPhone/android");
  }

  @Test
  public void testsetSmartPhoneMisc() throws FileNotFoundException {
    executeTestSet(getTargetFile("smartphone_misc.yaml"), "SmartPhone/misc");
  }

  @Test
  public void testsetAppliance() throws FileNotFoundException {
    executeTestSet(getTargetFile("appliance.yaml"), "Appliance");
  }

  @Test
  public void testsetPCLowPriority() throws FileNotFoundException {
    executeTestSet(getTargetFile("pc_lowpriority.yaml"), "PC/LowPriority");
  }

  @Test
  public void testsetMisc() throws FileNotFoundException {
    executeTestSet(getTargetFile("misc.yaml"), "Misc");
  }

  @Test
  public void testsetCrawlerNonMajor() throws FileNotFoundException {
    executeTestSet(getTargetFile("crawler_nonmajor.yaml"), "Crawler/NonMajor");
  }
}
