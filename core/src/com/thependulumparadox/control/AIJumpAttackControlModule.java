package com.thependulumparadox.control;

import com.thependulumparadox.command.CommandQueue;
import com.thependulumparadox.command.DelayCommand;
import com.thependulumparadox.command.ICommand;
import com.thependulumparadox.command.ValueCommand;
import com.thependulumparadox.command.InvokeCommand;

public class AIJumpAttackControlModule extends ControlModule
{
    private final CommandQueue queue;

    // Wandering command sequence example
    //queue.add(new DelayCommand(2.0f));
    //queue.add(new InvokeCommand(left, 0.2f));
    //queue.add(new DelayCommand(2.0f));
    //queue.add(new InvokeCommand(right, 0.2f));

    public AIJumpAttackControlModule()
    {
        queue = new CommandQueue(CommandQueue.LoopMode.LOOP);

        queue.add(new DelayCommand(1.0f));
        queue.add(new InvokeCommand(left, 1.0f));
        queue.add(new InvokeCommand(jumpStart));
        queue.add(new InvokeCommand(right, 1.0f));
        queue.add(new DelayCommand(1.0f));
        queue.add(new InvokeCommand(right, 1.0f));
        queue.add(new InvokeCommand(jumpStart));
        queue.add(new InvokeCommand(left, 1.0f));
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
