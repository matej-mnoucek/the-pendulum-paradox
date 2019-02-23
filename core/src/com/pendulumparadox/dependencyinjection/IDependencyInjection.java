package com.pendulumparadox.dependencyinjection;

public interface IDependencyInjection
{
    <T extends IInjectable> void addService(T service);
    <T extends IInjectable> void removeService(Class<T> service);
    <T extends IInjectable> T getService(Class<T> service);
}
