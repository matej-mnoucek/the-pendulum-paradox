package com.thependulumparadox.misc;

public class Range<T extends Comparable>
{
    private T lowerBound;
    private T upperBound;

    public Range(T lowerBound, T upperBound)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public boolean contains(T value)
    {
        if (lowerBound.compareTo(value) <= 0
                && value.compareTo(upperBound) <= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
