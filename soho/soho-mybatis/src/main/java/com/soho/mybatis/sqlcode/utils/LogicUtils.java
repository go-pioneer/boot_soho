package com.soho.mybatis.sqlcode.utils;

import com.soho.mybatis.sqlcode.aconst.OPT;

public class LogicUtils {

    public static boolean isDefault(Object opt) {
        if (opt == null || "".equals(opt.toString())) {
            return false;
        }
        String val = opt.toString();
        if (val.equals(OPT.EQ.toString()) || val.equals(OPT.NOT_EQ.toString()) || val.equals(OPT.LT.toString()) || val.equals(OPT.LTE.toString())
                || val.equals(OPT.GT.toString()) || val.equals(OPT.GTE.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object opt) {
        if (opt == null || "".equals(opt.toString())) {
            return false;
        }
        String val = opt.toString();
        if (val.equals(OPT.IS_NOT_NULL.toString()) || val.equals(OPT.IS_NULL.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isBetween(Object opt) {
        if (opt == null || "".equals(opt.toString())) {
            return false;
        }
        String val = opt.toString();
        if (val.equals(OPT.BETWEEN.toString()) || val.equals(OPT.NOT_BETWEEN.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isIn(Object opt) {
        if (opt == null || "".equals(opt.toString())) {
            return false;
        }
        String val = opt.toString();
        if (val.equals(OPT.IN.toString()) || val.equals(OPT.NOT_IN.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isLike(Object opt) {
        if (opt == null || "".equals(opt.toString())) {
            return false;
        }
        String val = opt.toString();
        if (val.equals(OPT.LIKE.toString()) || val.equals(OPT.NOT_LIKE.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isOr(Object opt) {
        if (opt == null || "".equals(opt.toString())) {
            return false;
        }
        String val = opt.toString();
        if (val.equals(OPT.OR.toString())) {
            return true;
        }
        return false;
    }

}
