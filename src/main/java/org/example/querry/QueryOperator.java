package org.example.querry;

public enum QueryOperator {
    EQ("$eq"),
    NE("$ne"),
    GT("$gt"),
    LT("$lt"),
    GTE("$gte"),
    LTE("$lte"),
    IN("$in"),
    NIN("$nin"),
    EXISTS("$exists"),
    SIZE("$size"),
    REGEX("$regex"),
    TYPE("$type"),
    NOR("$nor"),
    OR("$or"),

    AND("$and"),
    SET("$set"),
    UNSET("$unset"),
    INC("$inc"),
    PUSH("$push");

    private final String operator;

    QueryOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
