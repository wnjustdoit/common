package com.caiya.common.db.core.sql;

/**
 * 排序对象
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/30
 **/
public class Order {

    private final String field;

    private final Direction direction;

    private Order(String field) {
        this.field = field;
        this.direction = Direction.ASC;
    }

    private Order(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public static Order newOrder(String field) {
        return new Order(field);
    }

    public static Order newOrder(String field, Direction direction) {
        return new Order(field, direction);
    }

    public String getField() {
        return field;
    }

    public Direction getDirection() {
        return direction;
    }

}
