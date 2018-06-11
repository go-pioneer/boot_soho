package com.soho.spring.utils;

/**
 * @author shadow
 */
public class PaginationUtils {

    public static String getHtml(String function, Integer pageNo, Integer pageSize, Integer pageNumber) {
        if (pageNo > pageNumber) {
            pageNo = pageNumber;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("<div class=\"am-u-lg-12 am-cf\">");
        buffer.append("<input type=\"hidden\" id=\"" + function + "_pageNo\" name=\"pageNo\" value=\"" + pageNo + "\"/>");
        buffer.append("<input type=\"hidden\" id=\"" + function + "_pageSize\" name=\"pageSize\" value=\"" + pageSize + "\"/>");
        buffer.append("<div class='am-fr'><ul class='am-pagination tpl-pagination'>");
        buffer.append("<li><a title=\"上一页\" href=\"javascript:void(0);\" onclick=\"" + function + "(\'" + function + "\'," + ((pageNo - 1) < 1 ? 1 : (pageNo - 1)) + ");\">«</a></li>");
        if (pageNumber <= 10 || pageNo <= 5) { // 总页数<10或当前页<=5
            int end = (pageNo <= 5 && pageNumber > 10) ? 10 : pageNumber;
            end = end > pageNumber ? pageNumber : end;
            buffer.append(addPart(function, pageNo, pageSize, 1, end));
        } else {
            int begin = pageNo - 5 <= 0 ? 1 : pageNo - 5;
            buffer.append(addPart(function, pageNo, pageSize, begin, pageNo));
            int end = pageNo + 4 > pageNumber ? pageNumber : pageNo + 4; // 结束=当前页+4
            buffer.append(addPart(function, pageNo, pageSize, pageNo + 1, end));
        }
        buffer.append("<li><a title=\"下一页\" href=\"javascript:void(0);\" onclick=\"" + function + "(\'" + function + "\'," + ((pageNo + 1) > pageNumber ? pageNumber : pageNo + 1) + ");\">»</a></li></ul></div></div>");
        return buffer.toString();
    }

    private static String addPart(String function, int pageNo, int pageSize, int start, int end) {
        StringBuffer buffer = new StringBuffer();
        for (int i = start; i <= end; i++) {
            boolean current = (pageNo == i) ? true : false;
            if (current) {
                buffer.append("<li class=\"am-active\"><a title=\"第" + i + "页\" href=\"javascript:void(0);\">" + i + "</a></li>").toString();
            } else {
                buffer.append("<li><a title=\"第" + i + "页\" href=\"javascript:void(0);\" onclick=\"" + function + "(\'" + function + "\'," + i + "," + pageSize + ");\">" + i + "</a></li>").toString();
            }
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        String html = getHtml("page", 10, 20, 20);
        System.out.println(html);
    }

}
