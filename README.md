# Woothee java

The Java implementation of Project Woothee, which is multi-language user-agent strings parsers.

https://github.com/woothee/woothee

## Usage

### Parsing user-agent

```java
// import is.tagomor.woothee.Classifier;
// import is.tagomor.woothee.DataSet;

Map r = Classifier.parse("user agent string");
    
r.get("name")
// => name of browser (or string like name of user-agent)

r.get("category")
// => "pc", "smartphone", "mobilephone", "appliance", "crawler", "misc", "unknown"

r.get("os")
// => os from user-agent, or carrier name of mobile phones

r.get("version");
// => version of browser, or terminal type name of mobile phones

r.get("os_version")
// => version of operating systems (for some typical cases)
```

Parse user-agent string and returns a Map object with keys `name`, `category`, `os`, `version` and `vendor`.

For unknown user-agent (or partially failed to parse), result objects may have value 'UNKNOWN'.

* `category`
 * labels of user terminal type, one of 'pc', 'smartphone', 'mobilephone', 'appliance', 'crawler' or 'misc' (or 'UNKNOWN')
* `name`
 * the name of browser, like 'Internet Explorer', 'Firefox', 'GoogleBot'
* `version`
 * version string, like '8.0' for IE, '9.0.1' for Firefix, '0.2.149.27' for Chrome, and so on
* `os`
 * ex: 'Windows 7', 'Mac OSX', 'iPhone', 'iPad', 'Android'
 * This field used to indicate cellar phone carrier for category 'mobilephone'
* `vendor`
 * optional field, shows browser vendor
* `os_version`
 * optional field, shows version of operating systems

### Finding crawlers (almost all, not all) in fast

```java
Classifier.isCrawler(str); // boolean
```

Try to see useragent's category is 'crawler' or not, by casual(fast) method. Minor case of crawlers is not tested in this method. To check crawler strictly, use `Classifier.parse(str).get("category") == "crawler"`.

### Parsing user-agent in Hive queries

```sql
-- add woothee.jar to classpath
add jar woothee.jar;
-- create functions for each methods
CREATE TEMPORARY FUNCTION parse_agent as 'is.tagomor.woothee.hive.ParseAgent';
CREATE TEMPORARY FUNCTION is_pc as 'is.tagomor.woothee.hive.IsPC';
CREATE TEMPORARY FUNCTION is_smartphone as 'is.tagomor.woothee.hive.IsSmartPhone';
CREATE TEMPORARY FUNCTION is_mobilephone as 'is.tagomor.woothee.hive.IsMobilePhone';
CREATE TEMPORARY FUNCTION is_appliance as 'is.tagomor.woothee.hive.IsAppliance';
CREATE TEMPORARY FUNCTION is_crawler as 'is.tagomor.woothee.hive.IsCrawler';
CREATE TEMPORARY FUNCTION is_misc as 'is.tagomor.woothee.hive.IsMisc';
CREATE TEMPORARY FUNCTION is_unknown as 'is.tagomor.woothee.hive.IsUnknown';
CREATE TEMPORARY FUNCTION is_in as 'is.tagomor.woothee.hive.IsIn';
CREATE TEMPORARY FUNCTION pc as 'is.tagomor.woothee.hive.PC';
CREATE TEMPORARY FUNCTION smartphone as 'is.tagomor.woothee.hive.SmartPhone';
CREATE TEMPORARY FUNCTION mobilephone as 'is.tagomor.woothee.hive.MobilePhone';
CREATE TEMPORARY FUNCTION appliance as 'is.tagomor.woothee.hive.Appliance';
CREATE TEMPORARY FUNCTION crawler as 'is.tagomor.woothee.hive.Crawler';
CREATE TEMPORARY FUNCTION misc as 'is.tagomor.woothee.hive.Misc';
CREATE TEMPORARY FUNCTION unknown as 'is.tagomor.woothee.hive.Unknown';
CREATE TEMPORARY FUNCTION oneof as 'is.tagomor.woothee.hive.OneOf';
-- select
SELECT
  COUNT(pc(parsed_agent)) as pc_pageviews,
  COUNT(oneof(parsed_agent, array('pc', 'mobilephone', 'smartphone', 'appliance'))) as total_pageviews
FROM (
  SELECT parse_agent(useragent) as parsed_agent FROM access_log WHERE date='today'
) x
WHERE NOT is_crawler(parsed_agent) AND NOT is_unknown(parsed_agent)
```

## Build your own woothee.jar with Hive UDFs

1. Install git, JDK and Maven
2. Do `mvn -P hiveudf` with two `-D` options for `hadoop-version` and `hive-version`
 * `mvn package -P hiveudf -Dhadoop-version=0.23.11 -Dhive-version=0.13.0`
* * * * *

## Authors

* TAGOMORI Satoshi <tagomoris@gmail.com>

## License

Copyright 2012- TAGOMORI Satoshi (tagomoris)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
