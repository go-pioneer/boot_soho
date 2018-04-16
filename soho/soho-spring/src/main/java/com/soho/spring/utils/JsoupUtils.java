package com.soho.spring.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * @author shadow
 */
public class JsoupUtils {

    static Whitelist whitelist = null;

    static {
        whitelist = new Whitelist()
                .addTags(
                        "a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em",
                        "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub",
                        "sup", "u", "ul", "img")
                .addAttributes("a", "href")
                .addAttributes("blockquote", "cite")
                .addAttributes("q", "cite")
                .addAttributes("code", "class")
                .addAttributes("span", "class", "data-id")
                .addAttributes("pre", "style")
                .addAttributes("img", "src")
                .addProtocols("a", "href", "ftp", "http", "https", "mailto")
                .addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("cite", "cite", "http", "https")
                .addEnforcedAttribute("a", "rel", "nofollow");
    }

    public static String safety(String content) {
        return Jsoup.clean(content, whitelist);
    }

    public static void main(String[] args) {
        String s = "<p onclick='alert('无限弹出网页攻击');'> 5.5 英寸（对角线）显示屏，1920*1080 分辨率<span class=\"Apple-converted-space\">&nbsp;</span><br> 3D Touch<br> 1200 万像素摄像头<br> 配备嵌入式 M10 运动协处理器的 A10 Fusion 芯片<br> 4K 视频拍摄 (30 fps) 以及慢动作视频拍摄 (120 fps, 1080p)<br> 配备 Retina 闪光灯的 700 万像素 FaceTime HD 摄像头<br> 内置于主屏幕按钮的 Touch ID 指纹识别传感器<br> 具备 MIMO 技术的 802.11a/b/g/n/ac 无线网络 </p> \n" +
                "<p> 法律免责声明 </p> \n" +
                "<p> 1 iPhone 7 和 iPhone 7 Plus 可防溅、抗水、防尘，在受控实验室条件下经测试，其效果在 IEC 60529 标准下达到 IP67 级别。防溅、抗水、防尘功能并非永久有效，防护性能可能会因日常磨损而下降。请勿为潮湿状态下的 iPhone 充电；请参阅使用手册了解清洁和干燥说明。由于浸入液体而导致的损坏不在保修范围之内。<br> 2 亮黑色 iPhone 7 和 iPhone 7 Plus 高光泽度的外观，采用包含九道精密工序的阳极氧化与抛光工艺打造而成。其表面硬度与其他采用阳极氧化处理的 Apple 产品相同；但是，高光泽度表面可能随着日常使用而出现细微磨损。如果对此有所担心，建议你选择一款合适的保护壳来保护你的 iPhone。<br> 3 Apple Pay 仅在部分市场提供。请到苹果官网查看 Apple Pay 适用的国家和地区列表。<br> 4 所有电池性能信息取决于网络设置和许多其它因素，实际结果可能有所不同。电池充电周期次数有限，最终可能需由 Apple 服务提供商更换。 电池使用时间和充电周期次数依设置和使用情况的不同而可能有所差异。详情请参阅 www.apple.com/cn/batteries 和 www.apple.com/cn/iphone/battery.html。<br> 5 需要使用数据计划。4G LTE Advanced 和 4G LTE 功能适用于特定国家或地区的特定运营商。速度依据理论上的数据吞吐量，并基于现场状况和不同运营商而可能有所差异。有关 4G LTE 支持的详情，请联系你的运营商并查看 www.apple.com/iphone/LTE。<br> 6 实际可供使用的空间小于总容量，并会因许多因素而可能有所差异。<br> 7 某些功能仅适用于部分国家或地区。烦请到苹果官网查看详情<br> 8 所有产品信息，以苹果官网为准（apple.com.cn)<br> 　<span class=\"Apple-converted-space\">&nbsp;</span><br> 技术规格<span class=\"Apple-converted-space\">&nbsp;</span><br> 请前往 www.apple.com/cn/iphone-7/specs/ 查看完整内容。 </p> \n" +
                "<p> <br> </p> \n" +
                "<img src=\"http://oth3x62gr.bkt.clouddn.com/FgONVPYfgc98L185QZAxb36w4Oxa\"> \n" +
                "<br>";
        System.out.println("合计字符串长度: " + s.length());
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            long l2 = System.currentTimeMillis();
            String content = JsoupUtils.safety(s);
            // System.out.println(content);
            System.out.println(((System.currentTimeMillis() - l2)) + "毫秒");
        }
        // System.out.println(content);
        System.out.println("最终合计耗时:" + ((System.currentTimeMillis() - l1)) + "毫秒");
    }

}
