package com.thependulumparadox.control;

import com.thependulumparadox.command.CommandQueue;
import com.thependulumparadox.command.DelayCommand;
import com.thependulumparadox.command.ICommand;
import com.thependulumparadox.command.InvokeCommand;

/**
 * Implementation of a control module that is driven by list of predefined commands
 * Used as pseudo AI input provider for all enemies in the game
 * In the constructor there is a list of predefined behaviour
 * @see com.thependulumparadox.command.ICommandQueue
 * @see ICommand
 * @see ControlModule
 */
public class AIRunAroundControlModule extends ControlModule
{
    private final CommandQueue queue;

    // Wandering command sequence example
    //queue.add(new DelayCommand(2.0f));
    //queue.add(new InvokeCommand(left, 0.2f));
    //queue.add(new DelayCommand(2.0f));
    //queue.add(new InvokeCommand(right, 0.2f));

    public AIRunAroundControlModule()
    {
        queue = new CommandQueue(CommandQueue.LoopMode.LOOP);

        queue.add(new InvokeCommand(left, 0.3f));
        queue.add(new InvokeCommand(right, 0.6f));
        queue.add(new InvokeCommand(left, 0.3f));
        queue.add(new InvokeCommand(left, 0.8f));
        queue.add(new InvokeCommand(right, 1.6f));
        queue.add(new InvokeCommand(left, 0.8f));
    }

    public void addCommand(ICommand command)
    {
        queue.add(command);
    }

    @Override
    public void update(float delta)
    {
        queue.update(delta);
    }
}
