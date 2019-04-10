package com.thependulumparadox.misc;

import java.util.ArrayList;
import java.util.List;

public class ObjectPool<T>
{
    private List<T> pool = new ArrayList<>();
    private List<T> taken = new ArrayList<>();

    public boolean addToPool(T object)
    {
        return pool.add(object);
    }

    public boolean removeFromPool(T object)
    {
        return pool.remove(object);
    }

    public T takeOut()
    {
        T object = pool.remove(0);

        if (object == null)
            return null;

        taken.add(object);
        return object;
    }

    public boolean pushBack(T object)
    {
        if (!taken.contains(object))
            return false;

        taken.remove(object);
        pool.add(object);

        return true;
    }
}
