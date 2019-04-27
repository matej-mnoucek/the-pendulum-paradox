package com.thependulumparadox.misc;

/**
 * Class that implements simple numeric range
 * Used for determining visualisation of enhancements and bullets based on player stats
 * @param <T>
 */
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
