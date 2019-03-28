Editor View by Erin Mittmann and Omer Zeliger

The interface EditorAnimationView represents a view that allows a user to play and edit
   an animation described by an AnimationModel.
All of the methods added to the EditorAnimationModel interface are related to displaying
   the content on-screen. For example, the method drawCurrentTick updates the graphics
   to line up with the view's tick, while the displayErrorMessage method shows the user
   a given error message.
All of the previous views are compatible with the new AnimationController if they are wrapped in
   the EditorViewWrapper class. This class implements the EditorAnimationView interface,
   and the AnimationController takes in an EditorAnimationView and an AnimationView
   as parameters for its construction.
To use an EditorView, first call addFeatures with a Features object, then call setModel.
   This order is required, and calling setModel first will cause an IllegalStateException.
   If the view and model are passed to a controller as constructor arguments,
   the controller will automatically call these methods in the correct order.
To start an animation playing from the controller, call controller.go().
The EditorView requires a controller, but the three existing views (text, svg, visual) do not.

The Features interface defines all of the methods that the EditorAnimationView requires its
   controller to implement. These are methods that will be called when a user presses on buttons
   or otherwise interacts with the view.



How to use:
Playback control
To play and pause the animation, press the "play" button at the bottom.
To toggle looping on and off, press the "loop" button.
To rewind the animation back to the beginning, press the "begin" button.
The number at the bottom left shows the number of the current frame being displayed on screen.
To move forward one frame, press the "->" button;
   likewise, to move backwards one frame press the "<-" button.
   These buttons automatically pause the animation,
   and their behavior is affected by whether the animation loops or not.
To set the fps, write the desired number in the text field at the bottom right and press enter.
   If the user doesn't press enter or enters an invalid value,
   it will revert back to the current fps.

Shape editing
The list on the rightmost side names every shape currently in the animation.
   To select a shape, press on it.
A selected shape can be deleted with the "delete" button.
Selected shapes can also be edited by pressing the "edit" button.
   The edit button brings up a keyframe editor dialog box from which individual keyframes
   can be added, removed, or edited.
   To add a keyframe, fill in the boxes on the top layer and press "add".
   To autofill suggested new keyframe information from the shape editor screen,
      fill in the desired time (leftmost box) and press enter.
      If the time entered falls between two existing keyframes, the new keyframe
         will be extrapolated from the two keyframes surrounding it.
      If the time entered happens before or after the first or last keyframe respectively,
         the suggested new keyframe will be the same as the nearest one.
      If there are no existing keyframes, the autofill will default to a small black shape
         in the upper left corner.
      New frames cannot be added if they happen on the same tick as existing keyframes
         from the same shape.
   To edit or delete a keyframe, select the keyframe from the timeline below.
      Selecting a keyframe will autofill that keyframe's information in the second layer.
      To edit the keyframe's information, change any desired text boxes and press the "edit" button.
      To delete the selected keyframe, press the "delete" button.
To add a new shape to the animation, fill in the shape's name in the text field at the right
   and press "new ellipse" for an ellipse or "new rect" for a rectangle.
The user cannot add a new shape if the animation already had a shape of the same name.
   Shape names with no characters or with spaces are also not allowed.

Saving and loading
The user can save animations by using the top part of the screen.
To save an animation, input the save file name into the text field at the top left,
   and then press either the "text" or "svg" buttons.
   The "text" button saves the animation as a .txt (step-by-step description of the animation),
   and the "svg" button saves it as a .svg (playable in browsers).
If the animation is saved in a .txt format, it can be loaded from the text field at the top right.
   Loading an animation, unlike saving an animation, requires writing the file's full name
   (including the .txt extension).
To load the animation, press the "load" button. If successful, the new animation will
   open in the editor view as a separate window.

Error messages
Error messages are displayed at the bottom of the screen. They tell the user what went wrong
   if something they tried to do failed.
For example, if a user tries to add a keyframe but has filled the text fields with letters,
   an error message explaining the problem will pop up.



Changes from assignment 6:
We did not change our existing project very much to add the new required functionality.
For example, we slightly altered the way we set ticks in AnimationPanel (the class that extends
   JPanel and draws the animation on-screen)to give it more versatility.
The original design started with a tick of 1, and from there could only be incremented by 1.
   In this assignment, we changed this "increment" method to a setter so that the AnimationPanel
   would be easier to use in non-sequential animation (for example, rewinding to the start).
We also edited the Excellence class to include the new edit view as an option.

These sorts of changes were representative of most of the direct alterations to assignment 6 code.
To add the new EditorView, we instead extended the existing AnimationView interface
   into the EditorAnimationView interface.
Further, instead of changing the existing views to make them compatible with the new Controller,
   we used a wrapper class called EditorViewWrapper that implements EditorAnimationView
   and delegates all of its methods to the original view. This let us use the old views
   without changing their code.