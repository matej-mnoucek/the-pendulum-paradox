package com.thependulumparadox.command;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementation of command queue
 * @see ICommandQueue
 */
public class CommandQueue implements ICommandQueue
{
    public enum LoopMode {LOOP, ONCE}
    private LoopMode mode;

    private Queue<ICommand> mainQueue;
    private Queue<ICommand> backupQueue;

    public CommandQueue(LoopMode mode)
    {
        this.mainQueue = new LinkedList<>();
        this.backupQueue = new LinkedList<>();
        this.mode = mode;
    }

    public void loop(boolean loop)
    {
        if(loop)
        {
            mode = LoopMode.LOOP;
        }
        else
        {
            mode= LoopMode.ONCE;
        }
    }

    public boolean add(ICommand command)
    {
        return mainQueue.add(command);
    }

    public boolean remove(ICommand command)
    {
        return mainQueue.remove(command);
    }

    public boolean update(float delta)
    {
        ICommand command = mainQueue.peek();
        if (command == null)
        {
            return false;
        }

        if(command.execute(delta))
        {
            backupQueue.add(mainQueue.remove());
        }

        // Swap queues
        if (mainQueue.size() == 0 && mode == LoopMode.LOOP)
        {
            Queue<ICommand> queue = mainQueue;
            mainQueue = backupQueue;
            backupQueue = queue;
        }

        return true;
    }
}
