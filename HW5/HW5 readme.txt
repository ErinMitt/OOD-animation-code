Animation Model by Erin Mittmann and Omer Zeliger

The interface AnimationModel and the class AnimationModelImpl represent a modifiable animation.

AnimationModel lets a user create an animation by controlling the movements of shapes.
Adding shapes:
The user can add a new shape, either an ellipse or a rectangle, by calling addEllipse
   or addRectangle on the AnimationModel.
These methods take in a single argument: the new shape's name.
Each shape must have a unique name - if the AnimationModel already has a shape with the given name,
   then addShape and addEllipse will throw an IllegalArgumentException.

Keeping track of shapes:
The method getShapes will return a list with every shape name currently in the model.
getShapes is there for convenience, because the method displayAnimation contains every shape name
as well (but in a format that makes picking out individual shape names harder).

Removing shapes:
Shapes can be deleted by calling the method deleteShape and passing the shape's name as an argument.
The given shape (along with all of its motions) will be removed from the animation.
If no shape with the given name exists, the AnimationModel will throw an IllegalArgumentException.

Adding motions:
Motions can be added to any shape in the animation by calling the method addMotion.
addMotion takes the shape's name, the time, x and y coordinates, width, height, and RGB colors.
Motions are added in chronological order, so each new motion added will happen after the shape's
   last existing motion.
Therefore, the new motion's time must come after the shape's last motion's time.
Additionally, the time starts at one, so any shape's first motion must come at time 1 or after.

If the shape with the given name does not exist, the AnimationModel throws an IAE.
If the time is less than 1 or comes before the previous motion's time, AnimationModel throws an IAE.

R, G, and B each need to be between 0 and 255. If R, G, or B is outside of those bounds,
   it will be corrected to the nearest valid one.
For example, if R is given at 260, it will be saved as 255.
If G is given as -42, it will be saved as 0.

A shape's last motion may be extended by calling the method extend and passing it the
   shape's name and the desired time.
An extended motion will have all of the same characteristics as the shape's last motion
   except for a different, later time.
Therefore, extending a motion means the shape will stay in place until the given time.
extend throws the same exceptions as addMotion, and will additionally throw an
   IllegalStateException if the given shape has no motions to extend.

Removing motions:
Any shape's last motion can be deleted by calling deleteLastMotion and passing the
   shape's name as the argument.
If the given shape does not exist, deleteLastMotion will throw an IAE.
If the given shape exists but had no motions to delete, the AnimationModel will throw
   an IllegalStateException.

The output:
The method displayAnimation will return a String representing every shape and motion
   that occurs in the animation.
This String will be in the format:
shape [shape 1 name] [shape 1 type]
motion [shape 1 name] [motion 1 start t x y w h r g b]    [motion 1 end t x y w h r g b]
motion [shape 1 name] [motion 2 start t x y w h r g b]    [motion 2 end t x y w h r g b]
[...]

shape [shape 2 name] [shape 2 type]
motion [shape 2 name] [motion 1 start t x y w h r g b]    [motion 1 end t x y w h r g b]
motion [shape 2 name] [motion 2 start t x y w h r g b]    [motion 2 end t x y w h r g b]
[...]

For example, if an animation has a single blue ellipse with a width of 120 and height of 60
   that stays still at the coordinates (440, 70) from time = 6 to time = 20,
   display animation will return the following:
shape C ellipse
motion C 6 440 70 120 60 0 0 255    20 440 70 120 60 0 0 255

If a shape has only a single keyframe, display animation will treat that keyframe as
   the start and end of movement 1.



Design notes:
AnimationModelImpl contains a hashMap of Shapes (mapped by name), each of which has
a ShapeType enum (to define whether the shape is a rectangle or ellipse), a name,
and an ordered list of Motions.
Each Motion stores time (tick number), an x and y coordinate, a width and height, and an RGB value.
Once the view is written, then the shape will transition smoothly between Motions in the list.
Thus, every Motion represents an animation keyframe for that shape.