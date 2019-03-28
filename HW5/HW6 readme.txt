Animation View by Erin Mittmann and Omer Zeliger

The interface AnimationView represents a tool that can create various sorts of animations,
and its subclasses TextView, VisualView, and SVGView represent different formats the animation
can take.
Create the animation using the method animate() on AnimationView.
Each of the subclasses needs to take an AnimationModel as a source for the animation.

TextView writes a description of the animation to an Appendable,
and must take an Appendable as a location for output before it can animate.

SVGView generates an SVG-formatted text of the animation that can be played in a browser.
Like TextView, it must take an Appendable.
It can also take in a speed at which to animate, which determines the number of ticks per second.
The default speed is one tick per second.

VisualView plays an animation on-screen using Swing. It does not require an Appendable to write to.
Like SVGView, it also takes in a speed. If no speed is given, it defaults to one.

The program can be run from a text file using the main method in the class Excellence.
It takes command line arguments in the format:
 -in [input file], -out [output file], -speed [ticks per second], -view [type of view]
The arguments can be given in any order.
Only in and view are required; out will default to System.out and speed to 1
if not otherwise specified.
If any argument is invalid, the program will not run.

~~~

Changes to AnimationModelImpl from HW5:
The main change from HW5 was adding more getters - specifically, getters for individual shapes
and their motions.

We added a new class Transformation that represents a movement from one Motion to another.
Transformation is able to determine the state of a shape at any time between its start and end time.
We wrote getters for AnimationModelImpl that can return the transformation of a shape
at a given tick.

We also changed some of the data structures used in AnimationModelImpl and Shape.
For AnimationModelImpl, we changed the Map used to access Shapes from a HashMap to a LinkedHashMap
so we could preserve the order that shapes were added.
For Shape, we changed the data structure holding the Motions from a List to a TreeSet sorted by
the Motion's time.
This let us access Motions taking place at a certain time more efficiently than a List would have.

Lastly, we added the interface ReadOnlyModel (containing getters only) for use as a field in
AnimationView that would help us avoid accidentally mutating the AnimationModel through the view.