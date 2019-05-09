# TDT4240 - Software architecture
## Semester project - The Pendulum Paradox
The Pendulum Paradox is Jazz Jackrabbit inspired platformer game for Android. It also utilizes multiplayer for 2 players via Google Play Game Service.

## The story
The Android Andrea Falk travel through time when suddenly her time machine starts falling apart (because of improperly tightened screws of course) and then breaks which makes him stuck in the 16th century. The only way how to get back to the future is to gather the missing parts and repair the time machine. However, the world in 16th century is not a safe place. There are tons of hostile beings that try to ruin Andrea's plan. She needs to utilize all of his skills and effort in order to survive. There are also some rummors that Andrea's friend hologram Rune crashed nearby as well. Maybe they can join forces and help each other to succeed.

## Game mode
The game is fairly straightforward. The basic goal is to stay alive as long
as possible and get as high score as possible. One game round runs as long
as all involved players are alive. Each player has three lives but also health
attribute which means one hit from an enemy might or might not lead to one
life subtraction (depends on the enemy strength). Moreover, there are also
defense and damage attributes that define player’s resistance against attacks
and damage applied to enemies. Players attempt to kill enemies by shooting
at them.

## Controlling the player
In the Android version of the game, player is controlled by UI buttons. The
left part of the screen contains buttons for moving left and right and the
right part has buttons for shooting and jumping. The desktop version uses
arrow keys for movement and jumping and space key for shooting.

## Levels
The game contains two medieval levels. The goal is always to reach the end
of the current level. When all players finish the last level the game starts
again with the first level (endless level loop).

## Score
Players get points for killing enemies (5 points) and collecting time machine
parts (10 or 20 points). Scores of individual players are counted separately.
The more points the better. After finishing one game round, scores are sent
to online High score ladder managed by Google Play Game services.

## Collectables
### Time machine parts (aka coins)
The game has two different coin types. One is worth 10 points (copper wheel) and the second one 20 points (gold wheel).

### Power-ups (aka enahncements)
In the levels you can also find three different kinds of enhancements:
* __Life enhancement__ (red) - permanently adds one extra life to the player who has collected it.
* __Defense enhancement__ (green) - increases defense of all players for a short period of time (15 s)
* __Damage enhancement__ (blue) - increases damage of all players for a short
period of time (15 s)

## Enemies
The game contains three different kinds of enemies:
* __Smeets the Knight__ - aggressive guy constantly trying to kill someone
with his well-forged sword.  There are rummors that he came from
Netherlands.
* __Tore the Ninja Boy__ - impatient being constantly jumping around and
trying avoid work. He was born in Japan but he is not welcomed there
anymore.
* __Henning the Ninja__ - a close friend of Tore. His hearing is not one of
the best and that is why it is so hard to get in touch with him. You
can often find him just running around in panic.

_Written by Matthew (aka Master Builder)_
