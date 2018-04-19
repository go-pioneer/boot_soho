package com.soho.spring.utils;

/**
 * @author shadow
 */
public class PageUtils {

    public static String getHtml(Integer pageNo, Integer pageSize, Integer pageNumber, Integer pageTotal) {
        StringBuffer buffer = new StringBuffer();
        if (pageNumber <= 10 || pageNo <= 5) { // 总页数<10或当前页<=5
            int end = (pageNo <= 5 && pageNumber > 10) ? 10 : pageNumber;
            end = end > pageNumber ? pageNumber : end;
            for (int i = 1; i <= end; i++) {
                if (pageNo.intValue() == i) {
                    buffer.append(" [").append(i).append("] ");
                } else {
                    buffer.append(" ").append(i).append(" ");
                }
            }
            return buffer.toString();
        }
        int begin = pageNo - 5 <= 0 ? 1 : pageNo - 5;
        for (int i = begin; i <= pageNo; i++) { // 开始=当前页-5
            if (pageNo.intValue() == i) {
                buffer.append(" [").append(i).append("] ");
            } else {
                buffer.append(" ").append(i).append(" ");
            }
        }
        int end = pageNo + 4 > pageNumber ? pageNumber : pageNo + 4; // 结束=当前页+4
        for (int i = pageNo + 1; i <= end; i++) {
            if (pageNo.intValue() == i) {
                buffer.append(" [").append(i).append("] ");
            } else {
                buffer.append(" ").append(i).append(" ");
            }
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        String html = getHtml(6, 10, 13, 200);
        System.out.println(html);
    }

}
