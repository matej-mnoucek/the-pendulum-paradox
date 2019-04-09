package com.thependulumparadox.dependencyinjection;

import java.util.ArrayList;
import java.util.List;

public class DependencyContainer implements IDependencyInjection
{
    private static DependencyContainer container = null;
    private List<IInjectable> services = new ArrayList<>();

    public static DependencyContainer get()
    {
        if (container.equals(null))
        {
            container = new DependencyContainer();
            return container;
        }
        else
        {
            return container;
        }
    }

    public <T extends IInjectable> void addService(T service)
    {
        services.add(service);
    }

    @Override
    public <T extends IInjectable> void removeService(Class<T> service)
    {
        for (IInjectable s : services)
        {
            if (s.getClass().equals(service))
            {
                services.remove(service);
                return;
            }
        }
    }

    public <T extends IInjectable> T getService(Class<T> service)
    {
        for (IInjectable s : services)
        {
            if (s.getClass().equals(service))
            {
                return (T)s;
            }
        }

        return null;
    }
}
