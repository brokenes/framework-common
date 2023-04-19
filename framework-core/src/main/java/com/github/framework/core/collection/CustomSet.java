package com.github.framework.core.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 *  set操作工具类
 */
public class CustomSet {

    /**
     *
     * @Title: hashset
     * @Description: 将可变数组中的元素添加到HashSet中
     * @param elements 需要转换的可变数组元素
     * @return 转换后的HashSet集合
     *
     */
    @SafeVarargs
    public static <E> HashSet<E> hashset(E... elements) {
        final HashSet<E> set = new HashSet<E>(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    /**
     *
     * @Title: hashset
     * @Description: 将迭代器中的元素添加到HashSet中
     * @param elements 需要转换的迭代器元素
     * @return 转换后的HashSet集合
     *
     */
    public static <E> HashSet<E> hashset(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new HashSet<E>(cast(elements));
        } else {
            final HashSet<E> set = hashset();
            addAll(set, elements);
            return set;
        }
    }

    /**
     *
     * @Title: concurrentHashset
     * @Description: 将迭代器中的元素添加到Set中
     * @param elements 需要转换的迭代器元素
     * @return 转换后的Set集合
     */
    public static <E> Set<E> concurrentHashset(Iterable<? extends E> elements) {
        final Set<E> set = Collections.newSetFromMap(new ConcurrentHashMap<>(CustomMap.DEFAULT_SIZE));
        addAll(set, elements);
        return set;
    }

    /**
     *
     * @Title: linkedHashSet
     * @Description: 将迭代器中的元素添加到LinkedHashSet中
     * @param elements 需要转换的迭代器元素
     * @return 转换后的LinkedHashSet集合
     */
    public static <E> LinkedHashSet<E> linkedHashSet(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedHashSet<E>(cast(elements));
        }
        final LinkedHashSet<E> set = new LinkedHashSet<E>();
        addAll(set, elements);
        return set;
    }

    /**
     *
     * @Title: newTreeSet
     * @Description: 将迭代器中的元素添加到TreeSet中
     * @param elements 需要转换的迭代器元素
     * @return 转换后的TreeSet集合
     */
    public static <E extends Comparable<?>> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
        final TreeSet<E> set = new TreeSet<E>();
        addAll(set, elements);
        return set;
    }

    /**
     *
     * @Title: copyOnWriteArraySet
     * @Description: 将迭代器中的元素添加到CopyOnWriteArraySet中
     * @param elements 需要转换的迭代器元素
     * @return 转换后的CopyOnWriteArraySet集合
     *
     */
    public static <E> CopyOnWriteArraySet<E> copyOnWriteArraySet(Iterable<? extends E> elements) {
        final Collection<? extends E> elementsCollection = elements instanceof Collection ? cast(elements)
                : CustomList.arrayList(elements);
        return new CopyOnWriteArraySet<>(elementsCollection);
    }

    private static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection<T>) iterable;
    }

    private static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
        boolean wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add(iterator.next());
        }
        return wasModified;
    }

    /**
     *
     * @Title: addAll
     * @Description: 将迭代器elementsToAdd中的元素都到集合addTo中
     * @param addTo 添加到的目标集合
     * @param elementsToAdd 需要被添加的迭代器
     * @return 添加后的集合
     *
     */
    public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        if (elementsToAdd instanceof Collection) {
            final Collection<? extends T> c = cast(elementsToAdd);
            return addTo.addAll(c);
        }
        return addAll(addTo, elementsToAdd.iterator());
    }
}
