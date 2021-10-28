package top.guoziyang.fxxkjwts.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseUtil {
    private static Logger logger = LoggerFactory.getLogger(CourseUtil.class);
    private static final List<String> ignoreCategories = new ArrayList<>(Arrays.asList("文化素质核心", "MOOC", "素质讲座"));

    public static Map<String, String> courseCategoryMap = new HashMap<>();

    public static void setCourseCategory(String content) {
        String pattern = ".*?queryXsxk\\?pageXklb=(.*?)\".*\">(.*?)</";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        while(m.find()) {
            if(ignoreCategories.contains(m.group(2))) {
                continue;
            }
            courseCategoryMap.put(m.group(2), m.group(1));
        }
    }

}
