package com.github.framework.core.collection;


import com.github.framework.core.lang.Lang;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 *
 * @Description: array\collection\list\数据结构工具类
 *
 */
public class CustomList {

    /**
     * 带index的forEach
     * （代码来自网上）
     * @param elements 要进行foreach的collection
     * @param action 对集合进行处理的方法
     * @param <E>
     */
    public static <E> void forEach(
            Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);
        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }

    /**
     * 删除中的空项
     *
     * @param c
     * @return
     */
    public static <T, C extends Collection<T>> C removeIfEmpty(C c) {
        if (isNotEmpty(c)) {
            List<T> l = arrayList();
            for (T t : c) {
                if (Lang.isEmpty(t)) {
                    continue;
                }
                l.add(t);
            }
            return (C) l;
        }
        return c;
    }

    /**
     * 删除数组中为Empty的项
     *
     * @param arys
     * @return
     */
    public static <T> T[] removeIfEmpty(T[] arys) {
        if (isEmpty(arys)) {
            return (T[]) new Object[0];
        }
        return toArray(removeIfEmpty(arrayList(arys)));
    }

    /**
     * 将某个Collection转成数组
     *
     * @param <T>
     * @param c
     * @return
     */
    public static <T> T[] toArray(Collection<T> c) {
        if (isEmpty(c)) {
            return (T[]) new Object[0];
        }
        T t = getFirst(c);
        if (Lang.isEmpty(t)) {
            return (T[]) new Object[0];
        }
        T[] tArray = (T[]) Array.newInstance(t.getClass(), c.size());
        c.toArray(tArray);
        return tArray;
    }

    /**
     * 根据一组可变参数对象构建array list
     * @param objs 可变参数对象
     * @param <E> item类型
     * @return
     */
    public static <E> List<E> arrayList(final E... objs) {
        final List<E> list = new ArrayList<>();
        if (objs != null && objs.length > 0) {
            for (final E obj : objs) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 根据Iterable创建一个新的List
     * @param iter 实现Iterable的任何集合对象
     * @param <E> item类型
     * @return
     */
    public static <E> List<E> arrayList(final Iterable<E> iter) {
        final List<E> list = arrayList();
        if (iter != null) {
            iter.forEach(e -> list.add(e));
        }
        return list;
    }

    /**
     * 判断一个集合是否为空或空集
     * @param c 集合对象
     * @return true-空或空集
     */
    public static boolean isEmpty(final Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(final Object... objects) {
        return objects == null || objects.length == 0;
    }

    public static boolean isNotEmpty(final Object...objects) {
        return !isEmpty(objects);
    }

    public static boolean isNotEmpty(final Collection c) {
        return !isEmpty(c);
    }

    public static <E> E getFirst(final Collection<E> c) {
        return isNotEmpty(c) ? c.iterator().next() : null;
    }

}
