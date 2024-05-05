package org.example.querry;

import lombok.Getter;
import org.bson.conversions.Bson;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import static org.example.querry.QueryOperator.EQ;

@Getter
public class QueryCondition {
    private String field;
    private QueryOperator operator;
    private Object value;

    public QueryCondition(String field, QueryOperator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public QueryCondition(String field, Object value) {
        this.field = field;
        this.operator = EQ; //EQ as default
        this.value = value;
    }

    public Bson toBson() {
        switch (operator) {
            case EQ:
                return eq(field, value);
            case NE:
                return ne(field, value);
            case GT:
                return gt(field, value);
            case GTE:
                return gte(field, value);
            case LT:
                return lt(field, value);
            case LTE:
                return lte(field, value);
            case IN:
                return in(field, (Iterable<?>) value);
            default:
                return new Document();
        }
    }

}
