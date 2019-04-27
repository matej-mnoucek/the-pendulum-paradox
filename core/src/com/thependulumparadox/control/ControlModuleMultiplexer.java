package com.thependulumparadox.control;

import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.observer.IEvent;

import javax.naming.ldap.Control;

/**
 * This class allows entity to be controlled by more than one control module at the same time
 * The scenario in our case allows player entity to receive input from UI buttons
 * as well as from keyboard (at the same time)
 */
public class ControlModuleMultiplexer extends EventControlModule
{
    private ControlModule first;
    private ControlModule second;

    public ControlModuleMultiplexer(ControlModule first, ControlModule second)
    {
        this.first = first;
        this.second = second;

        // First
        if (first != null)
        {
            first.leftStart.addHandler((args) -> {
                leftStart.invoke(null);
            });
            first.rightStart.addHandler((args) -> {
                rightStart.invoke(null);
            });
            first.jumpStart.addHandler((args) -> {
                jumpStart.invoke(null);
            });
            first.attackStart.addHandler((args) -> {
                attackStart.invoke(null);
            });
            first.meleeStart.addHandler((args) -> {
                meleeStart.invoke(null);
            });

            first.left.addHandler((args) -> {
                left.invoke(null);
            });
            first.right.addHandler((args) -> {
                right.invoke(null);
            });
            first.jump.addHandler((args) -> {
                jump.invoke(null);
            });
            first.attack.addHandler((args) -> {
                attack.invoke(null);
            });
            first.melee.addHandler((args) -> {
                melee.invoke(null);
            });

            first.leftEnd.addHandler((args) -> {
                leftEnd.invoke(null);
            });
            first.rightEnd.addHandler((args) -> {
                rightEnd.invoke(null);
            });
            first.jumpEnd.addHandler((args) -> {
                jumpEnd.invoke(null);
            });
            first.attackEnd.addHandler((args) -> {
                attackEnd.invoke(null);
            });
            first.meleeEnd.addHandler((args) -> {
                meleeEnd.invoke(null);
            });
        }


        // Second module
        if (second != null)
        {
            second.leftStart.addHandler((args) -> {
                leftStart.invoke(null);
            });
            second.rightStart.addHandler((args) -> {
                rightStart.invoke(null);
            });
            second.jumpStart.addHandler((args) -> {
                jumpStart.invoke(null);
            });
            second.attackStart.addHandler((args) -> {
                attackStart.invoke(null);
            });
            second.meleeStart.addHandler((args) -> {
                meleeStart.invoke(null);
            });

            second.left.addHandler((args) -> {
                left.invoke(null);
            });
            second.right.addHandler((args) -> {
                right.invoke(null);
            });
            second.jump.addHandler((args) -> {
                jump.invoke(null);
            });
            second.attack.addHandler((args) -> {
                attack.invoke(null);
            });
            second.melee.addHandler((args) -> {
                melee.invoke(null);
            });

            second.leftEnd.addHandler((args) -> {
                leftEnd.invoke(null);
            });
            second.rightEnd.addHandler((args) -> {
                rightEnd.invoke(null);
            });
            second.jumpEnd.addHandler((args) -> {
                jumpEnd.invoke(null);
            });
            second.attackEnd.addHandler((args) -> {
                attackEnd.invoke(null);
            });
            second.meleeEnd.addHandler((args) -> {
                meleeEnd.invoke(null);
            });
        }
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        if(first != null)
            first.update(delta);

        if(second != null)
            second.update(delta);
    }
}
