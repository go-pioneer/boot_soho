package com.soho.spring.utils;

import java.util.regex.Pattern;

/**
 * @author shadow
 */
public class XSSUtils {

    // 获取字符串转义后真实长度
    public static int getStringLen(String object) {
        if (object == null || "".equals(object)) {
            return 0;
        }
        return unstrip(object).length();
    }

    public static String unstrip(String object) {
        return object.replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<")
                .replaceAll("&#x27;", "\'")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "#")
                .replaceAll("＃", "#");
    }

    public static String strip(String object) {
        if (object == null || object.isEmpty()) {
            return object;
        } else {
            object = cleanXSS(object);
        }

        StringBuffer sb = new StringBuffer(object.length() + 16);
        for (int i = 0; i < object.length(); i++) {
            char c = object.charAt(i);
            switch (c) {
                case '>':
                    sb.append("&gt;");// 转义大于号
                    break;
                case '<':
                    sb.append("&lt;");// 转义小于号
                    break;
                case '\'':
                    sb.append("&#x27;");// 转义单引号
                    break;
                case '\"':
                    sb.append("&quot;");// 转义双引号
                    break;
                case '&':
                    sb.append("&amp;");// 转义&
                    break;
                case '#':
                    sb.append("＃");// 转义#
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static String cleanXSS(String object) {
        if (object != null && !"".equals(object)) {
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            object = scriptPattern.matcher(object).replaceAll("");
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
            // scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            // object = scriptPattern.matcher(object).replaceAll("");
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            object = scriptPattern.matcher(object).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            object = scriptPattern.matcher(object).replaceAll("");
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            object = scriptPattern.matcher(object).replaceAll("");
            // Avoid e-xpression(...) expressions
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            object = scriptPattern.matcher(object).replaceAll("");
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            object = scriptPattern.matcher(object).replaceAll("");
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            object = scriptPattern.matcher(object).replaceAll("");
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            object = scriptPattern.matcher(object).replaceAll("");
        }
        return object;
    }

}
