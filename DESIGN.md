# Game design
* Cooperative platformer (like Jazz Jack Rabbit 2)
* Standard single player or
* Individual players in the same level (they both have individual score, it is enough when just one player finishes the level)
* Camera follows the player 
* Animated character (walk, run, jump, still the same gun)
* Can collect special ammunition (jumping bullets, greater impact)
* Can collect power ups (run faster, fly)
* Various kinds of collectible items (some of them are more valuable some of them less, they count towards final score)
* Several lives (I would say that 3 is the standard but extra ones may be collected in the level)
* Various enemies (stronger, weaker, faster, slower...)
* Interaction with environment (teleports, boosters, spring jumps)
* High score is based on time a level took, collected points and remaining lives
* Predefined levels with slightly randomized enemies
* Maybe boss fights? (probably not)
* Maybe respawn saves? (you always respawn at the latest save object location)

# Architectural patterns
* __MVP__ = Model-View-Presenter
* __ECS__ = Entity-Component-System

# Design patterns - already used
* __Builder__ = building named entities from individual components (we use Fluent Builder)
* __Abstract factory__ = standard abstract factory for creating components
* __Chain of responsibility__ = applying modifiers to enemies or player (e.g. power ups, bonuses...)
* __Observer__ = all various kinds of events (e.g. powerup collected, changes in model data...)
* __State__ = mainly for controlling view states
* __Proxy__ = multiplayer synchronization over network

# Design patterns - possible candidates
* __Bridge__ = decoupling abstraction from implementation (e.g. passing IRenderer to draw a sprite not one final renderer implementation)
* __Composite__ = basically handle collections the same way like you handle individual objects (note: idk how to do this in Java)
* __Decorator__ = maybe it will fit somewhere
* __Strategy__ = maybe it will fit somewhere
* __Template method__ = maybe it will fit somewhere