package cs3500.animator.controller;

/**
 * This class represents all of the functionality that an EditorAnimationView requires.
 */
public interface Features {
  /**
   * Play the animation if paused, pause the animation if playing.
   */
  void togglePlay();

  /**
   * Loop the animation if it's not looping, unloop the animation if it's looping.
   */
  void toggleLoop();

  /**
   * Rewind the animation to the start.
   */
  void rewind();

  /**
   * Move one frame forward.
   */
  void stepForward();

  /**
   * Move one frame backward.
   */
  void stepBackwards();

  /**
   * Set the animation's speed to the given speed.
   * @param input A String representing the desired speed.
   */
  void setSpeedToUserInput(String input);

  void resetTextFields();

  /**
   * Enter a screen from which a shape's Motions can be edited.
   * @param shape the shape to be edited
   */
  void enterShapeEditor(String shape);

  /**
   * Return to the default screen from the shape editor screen.
   */
  void exitShapeEditor();
}
