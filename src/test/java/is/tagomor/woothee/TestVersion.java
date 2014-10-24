package is.tagomor.woothee;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import is.tagomor.woothee.Classifier;

public final class TestVersion {

  public static String message(final String setName, final String target, final String attribute) {
    return String.format("%s test(%s): %s", setName, attribute, target);
  }

  @Test
  public void testVersionString() {
    Pattern v = Pattern.compile("^[0-9]+\\.[0-9]+\\.[0-9]+$");
    Matcher m = v.matcher( Classifier.VERSION );
    assertTrue("VERSION matches semantic versioning pattern", m.find());
    assertEquals("VERSION pattern starts at the head", 0, m.start());
  }
}

