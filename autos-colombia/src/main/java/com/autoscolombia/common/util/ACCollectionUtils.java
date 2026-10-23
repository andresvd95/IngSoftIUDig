package com.autoscolombia.common.util;

import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ACCollectionUtils {

    public static <T, R> List<R> mapCollectionToList(Collection<T> objects, Function<T, R> mapper) {

        return CollectionUtils.emptyIfNull(objects)
                .stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .toList();
    }
}
