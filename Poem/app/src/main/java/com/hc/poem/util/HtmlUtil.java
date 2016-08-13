package com.hc.poem.util;

/**
 * Created by houchen on 2016/8/12.
 * 生成html代码
 */
public class HtmlUtil {

    /**
     * 生成作者简介（头像、简介）
     * */
    public static String generateAuthorInfo(String profileUrl, String intro) {
        StringBuilder result = new StringBuilder();

        result.append("<!DOCTYPE html>");
        result.append("<html>");
        result.append("<head>");
        result.append("<meta charset=\"utf-8\">");
        result.append("<title></title>");
        result.append("<style type=\"text/css\">");
        result.append("body {");
        result.append("background-color: #EFEFEF;");
        result.append("}");
        result.append("img {");
        result.append("width: 110px;");
        result.append("float: left;");
        result.append("margin: 6px;");
        result.append("border: solid 1px #DDD;");
        result.append("box-shadow: 1px 1px 1px #CCC;");
        result.append("}");
        result.append("div {");
        result.append("font-size: 14px;");
        result.append("color: #999;");
        result.append("}");
        result.append("</style>");
        result.append("</head>");
        result.append("<body>");
        result.append("<div>");
        String imgLine = "<img src=\"" + profileUrl + "\">";
        result.append(imgLine);
        result.append(intro);
        result.append("</div>");
        result.append("</body>");
        result.append("</html>");

        return result.toString();
    }

}
