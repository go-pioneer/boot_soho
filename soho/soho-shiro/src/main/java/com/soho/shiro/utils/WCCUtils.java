package com.soho.shiro.utils;

import com.alibaba.fastjson.JSON;
import com.soho.shiro.initialize.RuleChain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 递归算法实现匹配
 */
public class WCCUtils {

    private static boolean match(String pattern, String content, int p, int c) {
        if ('\0' == pattern.charAt(p) && '\0' == content.charAt(c))
            return true;
        if ('*' == pattern.charAt(p) && '\0' != pattern.charAt(p + 1) && '\0' == content.charAt(c))
            return false;
        if ('?' == pattern.charAt(p) || pattern.charAt(p) == content.charAt(c))
            return match(pattern, content, p + 1, c + 1);
        if ('*' == pattern.charAt(p))
            return match(pattern, content, p + 1, c) || match(pattern, content, p, c + 1);
        return false;
    }

    public static boolean test(String pattern, String content) {
        if (null == pattern || null == content)
            return false;
        return match(pattern + '\0', content + '\0', 0, 0);
    }

    public static List<RuleChain> ruleChainComparator(List<RuleChain> chains) {
        Collections.sort(chains, new Comparator<RuleChain>() {
            @Override
            public int compare(RuleChain o1, RuleChain o2) {
                return test(o1.getUrl(), o2.getUrl()) ? 1 : -1;
            }
        });
        return chains;
    }

    public static void main(String[] args) {
        List<RuleChain> chains = new ArrayList<>();
        chains.add(new RuleChain("/user/my*", "role[user]"));
        chains.add(new RuleChain("/user/login*", "role[user]"));
        chains.add(new RuleChain("/user/myfriend/all", "role[user]"));
        chains.add(new RuleChain("/myuser/getmy", "role[user]"));
        chains.add(new RuleChain("/myuser*", "role[user]"));
        chains.add(new RuleChain("/**", "role[user]"));
        chains.add(new RuleChain("/user/**", "role[user]"));
        chains.add(new RuleChain("/my*/**", "role[user]"));
        chains.add(new RuleChain("/myuserfriend/getfriend", "role[user]"));
        ruleChainComparator(chains);
        System.out.println(JSON.toJSONString(chains));
    }

}