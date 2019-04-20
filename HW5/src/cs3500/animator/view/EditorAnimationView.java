package cs3500.animator.view;

import java.util.List;

import cs3500.animator.controller.Features;
import cs3500.animator.model.Motion;

/**
 * Represents a view that displays an animation and allows a user to edit it.
 */
public interface EditorAnimationView extends AnimationView {
  /**
   * Reset the display to reflect the current tick.
   */
  void drawCurrentTick();

  /**
   * Change whether the animation is currently playing or not.
   */
  void togglePlay();

  /**
   * Set the animation to not playing.
   */
  void pause();

  /**
   * Set the current tick to the given tick.
   * @param tick the new tick.
   */
  void setTick(int tick);

  /**
   * Set the current time to the earliest possible time for a tick.
   */
  void rewind();

  /**
   * Add one tick to the current time.
   */
  void incrementTick();

  /**
   * remove one tick from the current time.
   */
  void decrementTick();

  /**
   * Switch whether the animation loops or not.
   */
  void toggleLoop();

  /**
   * Recalculate the last tick in the animation.
   */
  void updateMaxTick();

  /**
   * Displays an interactive list of shapes in the order that they are stored in the model.
   */
  void setShapeList(List<String> shapes);

  /**
   * Write the representation of the given model's animation to this view's output in the format
   * determined by another view's type.
   * @param output the string to be written to the view's output.
   * @throws IllegalArgumentException if the text type is incorrect
   * @throws IllegalStateException if the output has not been set or is invalid (ie unwritable)
   */
  void save(String output);

  /**
   * Tie the commands given by the Controller to the buttons/control scheme of this view.
   * @param features the interface containing all the necessary commands
   */
  void addFeatures(Features features);

  /**
   * Shows the user a given error message.
   * @param message the message to be displayed
   */
  void displayErrorMessage(String message);

  /**
   * Reset focus onto the view instead of any of its components (like buttons or text fields).
   */
  void resetFocus();

  /**
   * Reset all text fields to their default state.
   */
  void resetTextFields();

  /**
   * Enter a screen from which a shape's Motions can be edited.
   */
  void enterShapeEditor(String shape);

  /**
   * Return to the default screen from the shape editor screen.
   */
  void exitShapeEditor();

  /**
   * Set the text boxes for creating a new frame to the given Motion.
   * @param m the motion to set the text boxes to
   */
  void setNewFrameText(Motion m);

  /**
   * Set the text boxes for editing an existing frame to the given Motion.
   * @param m the motion to set the text boxes to
   */
  void setEditFrameText(Motion m);
}
