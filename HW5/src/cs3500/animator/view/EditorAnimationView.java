package cs3500.animator.view;

import java.util.List;

import cs3500.animator.controller.Features;

public interface EditorAnimationView extends AnimationView {
  /**
   * Change whether the animation is currently playing or not.
   */
  void togglePlay();

  /**
   * Set the animation to not playing.
   */
  void pause();

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
   * Displays an interactive list of shapes in the order that they are stored in the model.
   */
  void setShapeList(List<String> shapes);

  /**
   * Save this animation to the output specified in setOutput.
   * @throws IllegalStateException if the output has not been set.
   */
  void save() throws IllegalStateException;

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
}
