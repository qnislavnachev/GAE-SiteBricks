package testapp.core;

import com.google.appengine.api.datastore.Query;

public class Queries {

    public static Query findSingleEntity(String kind, String propertyName, Object value) {
        return new Query(kind).setFilter(new Query.FilterPredicate(propertyName, Query.FilterOperator.EQUAL, value));
    }

    public static Query getAllEntitiesInKind(String kind) {
        return new Query(kind);
    }
}
