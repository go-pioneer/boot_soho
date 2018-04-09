package com.soho.mybatis.crud.aconst;

/**
 * 表连接查询模式
 *
 * @author shadow
 */
public enum MODE {

    LEFT("LEFT JOIN"), RIGHT("RIGHT JOIN"), INNER("INNER JOIN");

    private MODE(String value) {
        this.value = value;
    }

    private String value;

    public String toString() {
        return value;
    }

}
