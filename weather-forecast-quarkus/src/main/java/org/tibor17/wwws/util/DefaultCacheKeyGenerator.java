package org.tibor17.wwws.util;

import io.quarkus.cache.CacheKeyGenerator;

import java.lang.reflect.Method;

public final class DefaultCacheKeyGenerator implements CacheKeyGenerator {
    @Override
    public Object generate(Method method, Object... methodParams) {
        return switch (method.getName()) {
            case "findConnectionUrl" -> "remote-connection-url";
            case "findAuthKey" -> "auth-key";
            case "findAllLocations" -> "geos";
            default -> method.getName();
        };
    }
}
