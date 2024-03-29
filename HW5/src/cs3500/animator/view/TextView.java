package cs3500.animator.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

/**
 * The class TextView represents a view that can produce a text description of the animation
 * described in an AnimationModel.
 */
public class TextView implements AnimationView {
  private Appendable output;
  private ReadOnlyModel model;

  /**
   * setSpeed is unsupported in this view.
   * @param speed ticks per second
   * @throws UnsupportedOperationException if called
   */
  @Override
  public void setSpeed(double speed) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("TextView has no speed");
  }

  @Override
  public void setOutput(Appendable output) {
    if (output == null) {
      throw new IllegalArgumentException("Output Appendable must not be null");
    }
    this.output = output;
  }

  @Override
  public void setModel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    if (this.model != null) {
      throw new IllegalStateException("This view already has a model");
    }
    this.model = model;
  }

  /**
   * Export the animation as a text file in the format:
   * canvas 200 70 360 360
   * shape R rectangle
   * motion R 1 200 200 50 100 255 0 0 1 200 200 50 100 255 0 0
   * motion R 1 200 200 50 100 255 0 0 10 200 200 50 100 255 0 0
   * motion R 10 200 200 50 100 255 0 0 50 300 300 50 100 255 0 0
   * motion R 50 300 300 50 100 255 0 0 51 300 300 50 100 255 0 0
   * motion R 51 300 300 50 100 255 0 0 70 300 300 25 100 255 0 0
   * motion R 70 300 300 25 100 255 0 0 100 200 200 25 100 255 0 0
   * shape C ellipse
   * motion C 20 440 70 120 60 0 0 255 20 440 70 120 60 0 0 255
   * motion C 6 440 70 120 60 0 0 255 20 440 70 120 60 0 0 255
   * motion C 20 440 70 120 60 0 0 255 50 440 250 120 60 0 0 255
   * motion C 50 440 250 120 60 0 0 255 70 440 370 120 60 0 170 85
   * motion C 70 440 370 120 60 0 170 85 80 440 370 120 60 0 255 0
   * motion C 80 440 370 120 60 0 255 0 100 440 370 120 60 0 255 0
   * On the first line for every shape, display a motion repeating the first keyframe.
   * @throws IllegalStateException if the output cannot be written to the appendable
   */
  @Override
  public void animate() {
    if (output == null) {
      throw new IllegalStateException("The output Appendable has not been set");
    }
    if (model == null) {
      throw new IllegalStateException("The model has not been set");
    }
    try {
      output.append(createTextDisplay());
    } catch (IOException e) {
      throw new IllegalStateException("Could not write to the output");
    }
  }

  /**
   * Format the output of the animation in the format described in animate().
   * @return the text animation
   * @throws IllegalStateException if the model has not been set
   */
  private String createTextDisplay() {
    if (model == null) {
      throw new IllegalStateException("Cannot animate a null model");
    }
    List<String> layersText = new ArrayList<>(model.getLayers().size() + 1);
    layersText.add(joinWithSpaces("canvas",
            Integer.toString(model.getX()),
            Integer.toString(model.getY()),
            Integer.toString(model.getWidth()),
            Integer.toString(model.getHeight())));
    for (String layer : model.getLayers()) {
      List<String> shapesText = new ArrayList<>(model.getShapes(layer).size() + 1);
      shapesText.add("layer " + layer);
      for (String shape : model.getShapes(layer)) {
        List<Motion> motions = model.getMotions(layer, shape);
        List<String> motionLines = new ArrayList<>(2 * (motions.size() + 1));
        motionLines.add(joinWithSpaces("shape", shape, model.getShapeType(layer, shape)));
        if (motions.size() >= 1) {
          motionLines.add("rotation " + motions.get(0).getRotation());
          motionLines.add(joinWithSpaces("motion", shape,
                  motions.get(0).display(), motions.get(0).display()));
        }
        for (int i = 0; i < motions.size() - 1; i++) {
          motionLines.add("rotation " + motions.get(i + 1).getRotation());
          motionLines.add(joinWithSpaces("motion", shape,
                  motions.get(i).display(), motions.get(i + 1).display()));
        }
        shapesText.add(String.join("\n", motionLines));
      }
      layersText.add(String.join("\n", shapesText));
    }
    return String.join("\n", layersText);
  }

  /**
   * Join the given words with spaces in between them and return the result as a String.
   * @param words the words to join
   * @return a single string made up of the words separated by a single space each
   */
  private String joinWithSpaces(String... words) {
    return String.join(" ", words);
  }
}
