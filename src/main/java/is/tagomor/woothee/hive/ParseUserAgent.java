package is.tagomor.woothee.hive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;

import is.tagomor.woothee.Classifier;
import is.tagomor.woothee.DataSet;

/**
 * Hive Generic UDF to parse user agent and returns a Struct with fields name,
 * categeory, os, os_version, version, vendor, manufacturer and model.
 * 
 */
public class ParseUserAgent extends GenericUDF {

	public static final String ATTRIBUTE_MANUFACTURER = "manufacturer";
	public static final String ATTRIBUTE_MODEL = "model";

	private Object[] result;

	@Override
	public String getDisplayString(String[] arg0) {
		return "User Agent";
	}

	/*
	 * @see
	 * org.apache.hadoop.hive.ql.udf.generic.GenericUDF#initialize(org.apache.
	 * hadoop.hive.serde2.objectinspector.ObjectInspector[])
	 */
	@Override
	public ObjectInspector initialize(ObjectInspector[] arg0) throws UDFArgumentException {
		// Define the field names for the struct<> and their types
		ArrayList<String> structFieldNames = new ArrayList<String>();
		ArrayList<ObjectInspector> structFieldObjectInspectors = new ArrayList<ObjectInspector>();

		// fill struct field names

		// name
		structFieldNames.add(DataSet.ATTRIBUTE_NAME);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		// categeory
		structFieldNames.add(DataSet.ATTRIBUTE_CATEGORY);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		// os
		structFieldNames.add(DataSet.ATTRIBUTE_OS);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		// version
		structFieldNames.add(DataSet.ATTRIBUTE_VERSION);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		// os version
		structFieldNames.add(DataSet.ATTRIBUTE_OS_VERSION);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		// vendor
		structFieldNames.add(DataSet.ATTRIBUTE_VENDOR);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		// manufacturer
		structFieldNames.add(ATTRIBUTE_MANUFACTURER);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		// model
		structFieldNames.add(ATTRIBUTE_MODEL);
		structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

		StructObjectInspector si = ObjectInspectorFactory.getStandardStructObjectInspector(structFieldNames,
				structFieldObjectInspectors);
		return si;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hadoop.hive.ql.udf.generic.GenericUDF#evaluate(org.apache.
	 * hadoop.hive.ql.udf.generic.GenericUDF.DeferredObject[])
	 */
	@Override
	public Object evaluate(DeferredObject[] args) throws HiveException {
		if (args == null || args.length < 1) {
			throw new HiveException("args is empty");
		}
		if (args[0].get() == null) {
			throw new HiveException("args contains null instead of object");
		}

		Object argObj = args[0].get();

		// get argument
		String userAgent = null;
		if (argObj instanceof Text) {
			userAgent = ((Text) argObj).toString();
		} else if (argObj instanceof String) {
			userAgent = (String) argObj;
		} else {
			throw new HiveException(
					"Argument is neither a Text nor String, it is a " + argObj.getClass().getCanonicalName());
		}
		// parse UA string and return struct, which is just an array of objects:
		// Object[]
		return parseUserAgentString(userAgent);
	}

	/**
	 * @param userAgent
	 * @return
	 */
	private Object parseUserAgentString(String userAgent) {
		result = new Object[8];
		Map<String, String> m = null;
		if (userAgent != null) {
			m = Classifier.parse(userAgent);
		} else {
			m = Classifier.parse(null);
		}
		String name = m.get(DataSet.ATTRIBUTE_NAME);
		String category = m.get(DataSet.ATTRIBUTE_CATEGORY);
		String os = m.get(DataSet.ATTRIBUTE_OS);
		String version = m.get(DataSet.ATTRIBUTE_VERSION);
		String osVersion = m.get(DataSet.ATTRIBUTE_OS_VERSION);
		String vendor = m.get(DataSet.ATTRIBUTE_VENDOR);
		String manufacturer = "";
		String model = "";

		os = os.trim();

		if (os.equalsIgnoreCase("iphone") | os.equalsIgnoreCase("ipad") | os.equalsIgnoreCase("ipod")) {
			manufacturer = "APPLE";
			model = os;
		} else if (os.replaceAll(" ", "").equalsIgnoreCase("macosx")) {
			manufacturer = "APPLE";
		} else if (os.equalsIgnoreCase("android")) {
			manufacturer = regex_extract(userAgent, ".* Manufacturer/(.*)? .*", 1);
			String model_ext = regex_extract(userAgent,
					".* \\(Linux; (U; )?Android [0-9.]*; ?([a-z][a-z]-[a-z][a-z])?;? ([^\\);]*)?(; wv)?.*", 3);
			if (model_ext != null) {
				model = regex_extract(model_ext, "(.*)( Build\\/.*)|(.*)", 1);
			}
		}

		os = (os.equalsIgnoreCase("iphone") | os.equalsIgnoreCase("iphone") | os.equalsIgnoreCase("iphone")) ? "iOS"
				: os;
		result[0] = new Text(name != null ? name : "");
		result[1] = new Text(category != null ? category : "");
		result[2] = new Text(os != null ? os : "");
		result[3] = new Text(version != null ? version : "");
		result[4] = new Text(osVersion != null ? osVersion : "");
		result[5] = new Text(vendor != null ? vendor : "");
		result[6] = new Text(manufacturer != null ? manufacturer.toUpperCase() : "");
		result[7] = new Text(model != null ? model : "");

		return result;
	}

	/**
	 * @param stringToSearch
	 * @param pattern
	 * @param iGroup
	 * @return
	 */
	public String regex_extract(String stringToSearch, String pattern, int iGroup) {

		Pattern p = Pattern.compile(pattern); // the pattern to search for
		Matcher m = p.matcher(stringToSearch);

		// if we find a match, get the group
		if (m.find()) {
			// we're only looking for one group, so get it
			try {
				return m.group(iGroup);
			} catch (IndexOutOfBoundsException e) {
				return null;
			} catch (IllegalStateException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ParseUserAgent agent = new ParseUserAgent();
			String model = agent.regex_extract(agent.regex_extract(
					"Mozilla/5.0 (Linux; U; Android 4.3; " + "en-us; SGH-T889 Build/JSS15J) AppleWebKit/534.30 "
							+ "(KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 "
							+ "Manufacturer/samsung SOUNDTOUCH_MOBILE_APP/" + "39ea012a-bad4-4496-9d69-6910e80b2a0a",
					".* \\(Linux; (U; )?Android [0-9.]*; ?([a-z][a-z]-[a-z][a-z])?;? ([^\\);]*)?(; wv)?.*", 3),
					"(.*)( Build\\/.*)|(.*)", 1);
			System.out.println(model);
			agent.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
