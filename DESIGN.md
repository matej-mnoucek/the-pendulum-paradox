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

# Design patterns
* __Buidler__ = Setting up characters, enemies or other complex objects made from components. In the best case use of Fluent Builder: _"He".Append("ll").Append("o");_  
(e.g. HTML Builder, String builder).
* __Abstract factory__ = standard abstract factory for e.g. creating components for the final builder assembly.
* __Bridge__ = decoupling abstraction from implementation (e.g. passing IRenderer to draw a sprite not one final implementation)
* __Composite__ = basically handle collections the same way like you handle individual objects (note: idk how to do this in Java)
* __Decorator__ = maybe it will fit somewhere
* __Chain of responsibility__ = modifiers for enemies and characters (power ups, bonuses...)
* __Observer__ = all various kinds of events (e.g. powerup collected, end of level reached)
* __State__ = various states objects (e.g. game state, character state, animation state)
* __Strategy__ = maybe it will fit somewhere 