package cs3500.animator.view;

import java.io.IOException;
import java.util.Arrays;

import cs3500.animator.model.ReadOnlyModel;

/**
 * The class TextView represents
 */
public class TextView implements AnimationView {
  private final Appendable output;
  private final ReadOnlyModel model;

  /**
   * Create a TextView animation generator
   * @param model
   * @param output
   */
  TextView(ReadOnlyModel model, Appendable output) {
    this.model = model;
    this.output = output;
  }

  /**
   * Export the animation as a text file in the format:
   * canvas 200 70 360 360
   * shape R rectangle
   * motion R 1 200 200 50 100 255 0 0 10 200 200 50 100 255 0 0
   * motion R 10 200 200 50 100 255 0 0 50 300 300 50 100 255 0 0
   * motion R 50 300 300 50 100 255 0 0 51 300 300 50 100 255 0 0
   * motion R 51 300 300 50 100 255 0 0 70 300 300 25 100 255 0 0
   * motion R 70 300 300 25 100 255 0 0 100 200 200 25 100 255 0 0
   * [empty space]
   * shape C ellipse
   * motion C 6 440 70 120 60 0 0 255 20 440 70 120 60 0 0 255
   * motion C 20 440 70 120 60 0 0 255 50 440 250 120 60 0 0 255
   * motion C 50 440 250 120 60 0 0 255 70 440 370 120 60 0 170 85
   * motion C 70 440 370 120 60 0 170 85 80 440 370 120 60 0 255 0
   * motion C 80 440 370 120 60 0 255 0 100 440 370 120 60 0 255 0
   * .
   * @throws IllegalStateException if the output cannot be written to the appendable
   */
  @Override
  public void animate() {
    try {
      output.append(String.join("", new String[]{"canvas",
              Integer.toString(model.getX()),
              Integer.toString(model.getY()),
              Integer.toString(model.getWidth()),
              Integer.toString(model.getHeight()),})
      + "\n" + model.displayAnimation());
    } catch (IOException e) {
      throw new IllegalStateException("Could not write to the output.");
    }
  }
}
