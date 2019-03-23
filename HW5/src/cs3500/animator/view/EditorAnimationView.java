package cs3500.animator.view;

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
   * Save this animation to the output specified in setOutput.
   * @throws IllegalStateException if the output has not been set.
   */
  void save() throws IllegalStateException;

  /**
   * Tie the commands given by the Controller to the buttons/control scheme of this view.
   * @param features the interface containing all the necessary commands
   */
  void addFeatures(Features features);
}
