package org.example.querry;

import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class QueryBuilder {
    private final List<Bson> filters;

    public QueryBuilder() {
        filters = new ArrayList<>();
    }

    public QueryBuilder addCondition(QueryCondition condition) {
        filters.add(condition.toBson());
        return this;
    }

    public QueryBuilder addCondition(QueryOperator operator, QueryCondition... conditions) {
        Bson filter;
        switch (operator) {
            case AND:
                filter = and(getFilters(conditions));
                break;
            case OR:
                filter = or(getFilters(conditions));
                break;
            case NOR:
                filter = nor(getFilters(conditions));
                break;
            case SET:
                filter = set(conditions[0].getField(), conditions[0].getValue());
                break;
            case UNSET:
                filter = unset(conditions[0].getField());
                break;
            case INC:
                filter = inc(conditions[0].getField(), (Number) conditions[0].getValue());
                break;
            case REGEX:
                filter = regex(conditions[0].getField(), conditions[0].getValue().toString());
                break;
            default:
                filter = and(getFilters(conditions));
        }
        filters.add(filter);
        return this;
    }

    public Bson build() {
        List<Bson> filtersFinal = new ArrayList<>(filters);
        filters.clear();
        return and(filtersFinal);
    }

    private List<Bson> getFilters(QueryCondition... conditions) {
        List<Bson> bsonList = new ArrayList<>();
        for (QueryCondition condition : conditions) {
            bsonList.add(condition.toBson());
        }
        return bsonList;
    }



}

