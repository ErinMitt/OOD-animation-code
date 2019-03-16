package cs3500.animator.view;

import cs3500.animator.model.ReadOnlyModel;

/**
 * An interface representing a format of animation.
 */
public interface AnimationView {
  /**
   * Assign the animation model that contains information about the animation to be played.
   * @param model the model to be played
   * @throws IllegalStateException if the model has already been set
   */
  void setModel(ReadOnlyModel model);

  /**
   * Produce an animation. The animation should be based on the information in a stored model.
   * @throws IllegalStateException if the model is null
   */
  void animate();
}
