package cs3500.animator;

import cs3500.animator.view.AnimationView;
import cs3500.animator.view.SVGView;
import cs3500.animator.view.TextView;
import cs3500.animator.view.VisualView;

/**
 * A class that is able to create an instance of different types of animation views.
 */
class ViewFactory {
  /**
   * Create an animation view of the type given. If the type specified is invalid or null,
   * throw an exception.
   * @param type the type of view
   * @return an instance of the specified view
   * @throws IllegalArgumentException if the given type is invalid
   */
  public static AnimationView buildView(String type) {
    if (type == null) {
      throw new IllegalArgumentException("The type must not be null");
    }
    switch (type) {
      case "text":
        return new TextView();
      case "svg":
        return new SVGView();
      case "visual":
        return new VisualView();
      default:
        throw new IllegalArgumentException("Invalid view type " + type);
    }
  }
}
