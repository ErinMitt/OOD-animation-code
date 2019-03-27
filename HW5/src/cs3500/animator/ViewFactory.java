package cs3500.animator;

import cs3500.animator.view.EditorAnimationView;
import cs3500.animator.view.EditorView;
import cs3500.animator.view.EditorViewWrapper;
import cs3500.animator.view.SVGView;
import cs3500.animator.view.TextView;
import cs3500.animator.view.VisualView;

/**
 * A class that is able to create an instance of different types of animation views.
 */
public class ViewFactory {
  /**
   * Create an animation view of the type given. If the type specified is invalid or null,
   * throw an exception.
   * @param type the type of view
   * @return an instance of the specified view
   * @throws IllegalArgumentException if the given type is invalid
   */
  public static EditorAnimationView buildView(String type) {
    if (type == null) {
      throw new IllegalArgumentException("The type must not be null");
    }
    switch (type) {
      case "text":
        return new EditorViewWrapper(new TextView());
      case "svg":
        return new EditorViewWrapper(new SVGView());
      case "visual":
        return new EditorViewWrapper(new VisualView());
      case "edit":
        return new EditorView();
      default:
        throw new IllegalArgumentException("Invalid view type " + type);
    }
  }
}
