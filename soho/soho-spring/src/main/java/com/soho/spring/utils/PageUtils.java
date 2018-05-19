package com.soho.spring.utils;

/**
 * @author shadow
 */
public class PageUtils {

    public static String getHtml(Integer pageNo, Integer pageNumber) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<div class='am-u-lg-12 am-cf'><div class='am-fr'><ul class='am-pagination tpl-pagination'>");
        buffer.append("<li><a href='javascript:void(0);' onclick='paginator('" + pageNo + "');'>«</a></li>");
        if (pageNumber <= 10 || pageNo <= 5) { // 总页数<10或当前页<=5
            int end = (pageNo <= 5 && pageNumber > 10) ? 10 : pageNumber;
            end = end > pageNumber ? pageNumber : end;
            for (int i = 1; i <= end; i++) {
                buffer.append(addPart(pageNo, i));
            }
            return buffer.toString();
        }
        int begin = pageNo - 5 <= 0 ? 1 : pageNo - 5;
        for (int i = begin; i <= pageNo; i++) { // 开始=当前页-5
            buffer.append(addPart(pageNo, i));
        }
        int end = pageNo + 4 > pageNumber ? pageNumber : pageNo + 4; // 结束=当前页+4
        for (int i = pageNo + 1; i <= end; i++) {
            buffer.append(addPart(pageNo, i));
        }
        buffer.append("<li><a href='javascript:void(0);' onclick='paginator('" + pageNo + "');'>»</a></li>");
        buffer.append("</ul></div></div>");
        return buffer.toString();
    }

    private static String addPart(int pageNo, int i) {
        boolean current = (pageNo == i) ? true : false;
        if (current) {
            return new StringBuffer().append("<li><a href='javascript:void(0);' class='am-active' onclick='paginator('" + i + "');'>" + i + "</a></li>").toString();
        } else {
            return new StringBuffer().append("<li><a href='javascript:void(0);' onclick='paginator('" + i + "');'>" + i + "</a></li>").toString();
        }
    }

    public static void main(String[] args) {
        String html = getHtml(10, 20);
        System.out.println(html);
    }

}
