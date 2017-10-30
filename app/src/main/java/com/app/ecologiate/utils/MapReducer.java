package com.app.ecologiate.utils;


import java.util.ArrayList;
import java.util.List;

public class MapReducer {

    // Map
    public interface IMapper<V, T> {

        T each(V i);

    }

    public static <V, T> List<T> map(IMapper<V, T> processor, List<V> input) {
        List<T> o = new ArrayList<>();
        for(V i : input) {
            o.add(processor.each(i));
        }
        return o;
    }

    // Reduce
    public interface IReducer<V, T> {

        T reduce(T c, V i);

    }

    public static <V, T> T reduce(IReducer<V, T> processor, V[] input, T initiator) {
        T o = initiator;
        for(V i : input) {
            o = processor.reduce(o, i);
        }
        return o;
    }

}
