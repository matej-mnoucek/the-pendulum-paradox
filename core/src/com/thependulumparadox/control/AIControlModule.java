package com.thependulumparadox.control;

import com.thependulumparadox.command.CommandQueue;
import com.thependulumparadox.command.DelayCommand;
import com.thependulumparadox.command.ValueCommand;
import com.thependulumparadox.command.InvokeCommand;

public class AIControlModule extends ControlModule
{
    private CommandQueue queue = new CommandQueue(CommandQueue.LoopMode.LOOP);

    public AIControlModule()
    {
        queue.add(new DelayCommand(1.0f));
        queue.add(new InvokeCommand(left, 0.2f));
        queue.add(new DelayCommand(1.0f));
        //queue.add(new InvokeCommand(attackStart));
        queue.add(new DelayCommand(1.0f));
        queue.add(new InvokeCommand(right, 0.2f));
        queue.add(new DelayCommand(1.0f));
        //queue.add(new InvokeCommand(attackStart));
    }

    @Override
    public void update(float delta)
    {
        queue.update(delta);
    }
}
