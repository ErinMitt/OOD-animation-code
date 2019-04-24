import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Motion;
import cs3500.animator.model.Transformation;

/**
 * A class for testing whether methods were called to an AnimationModel and recording their inputs.
 */
public class WritingMockModel implements AnimationModel {
  private Appendable output;

  public WritingMockModel(Appendable output) {
    this.output = output;
  }

  /**
   * Set a new Appendable as the output for the mock.
   * @param output the new output location
   */
  public void setMockOutput(Appendable output) {
    this.output = output;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    append("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
  }

  @Override
  public void addLayer(String layerName) {
    append("addLayer called with " + layerName);
  }

  @Override
  public void deleteLayer(String layerName) {
    append("deleteLayer called with " + layerName);
  }

  @Override
  public void moveLayer(String layerName, int position) {
    append("moveLayer called with " + layerName + ", " + position);
  }

  @Override
  public void addEllipse(String layer, String name) {
    append("addEllipse called with " + name);
  }

  @Override
  public void addRectangle(String layer, String name) {
    append("addRectangle called with " + name);
  }

  @Override
  public void deleteShape(String layer, String shapeName) {
    append("deleteShape called with " + shapeName);
  }

  @Override
  public void addMotion(String layer, String shapeName,
                        int time, int x, int y, int width, int height,
                        int red, int green, int blue) {
    append(String.join(" ", "addMotion called with", shapeName,
            Integer.toString(time), Integer.toString(x), Integer.toString(y),
            Integer.toString(width), Integer.toString(height),
            Integer.toString(red), Integer.toString(green), Integer.toString(blue)));
  }

  @Override
  public void addMotion(String layer, String shapeName,
                        int time, int x, int y, int width, int height,
                        int red, int green, int blue, int position) {
    append(String.join(" ", "addMotion called with", shapeName,
            Integer.toString(time), Integer.toString(x), Integer.toString(y),
            Integer.toString(width), Integer.toString(height),
            Integer.toString(red), Integer.toString(green), Integer.toString(blue),
            Integer.toString(position)));
  }

  @Override
  public void editMotion(String layer, String shape, int time, int x, int y, int width, int height,
                         int red, int green, int blue) {
    append(String.join(" ", "editMotion called with", shape,
            Integer.toString(time), Integer.toString(x), Integer.toString(y),
            Integer.toString(width), Integer.toString(height),
            Integer.toString(red), Integer.toString(green), Integer.toString(blue)));
  }

  @Override
  public void editMotion(String layer, String shape, int time, int x, int y, int width, int height,
                         int red, int green, int blue, int position) {
    append(String.join(" ", "editMotion called with", shape,
            Integer.toString(time), Integer.toString(x), Integer.toString(y),
            Integer.toString(width), Integer.toString(height),
            Integer.toString(red), Integer.toString(green), Integer.toString(blue),
            Integer.toString(position)));
  }

  @Override
  public void deleteMotion(String layer, String shape, int time) {
    append("deleteMotion called with " + shape + " " + time);
  }

  @Override
  public void deleteLastMotion(String layer, String shapeName) {
    append("deleteLastMotion called with " + shapeName);
  }

  @Override
  public List<String> getLayers() {
    append("getLayers called");
    return new ArrayList<>(Arrays.asList("1"));
  }

  @Override
  public List<String> getShapes(String layer) {
    append("getShapes called");
    return new ArrayList<>(Arrays.asList("shape"));
  }

  @Override
  public String displayAnimation() {
    append("displayAnimation called");
    return "";
  }

  @Override
  public int getX() {
    append("getX called");
    return 0;
  }

  @Override
  public int getY() {
    append("getY called");
    return 0;
  }

  @Override
  public int getWidth() {
    append("getWidth called");
    return 1;
  }

  @Override
  public int getHeight() {
    append("getHeight called");
    return 1;
  }

  @Override
  public Transformation getTransformationAt(String layer, String shapeName, int tick) {
    append("getTransformationAt called with " + shapeName + " " + tick);
    return new Transformation(new Motion(1, 1, 1, 1, 1, 1, 1, 1),
            new Motion(3, 3, 3, 3, 3, 3, 3, 3));
  }

  @Override
  public List<Motion> getMotions(String layer, String shapeName) {
    append("getMotions called with " + shapeName);
    return new ArrayList<>();
  }

  @Override
  public String getShapeType(String layer, String shapeName) {
    append("getShapeType called with " + shapeName);
    return "ellipse";
  }

  /**
   * Append the given message onto the output appendable.
   * @param message the message to be written
   */
  private void append(String message) {
    try {
      this.output.append(message + "\n");
    } catch (IOException e) {
      // don't do anything, this is a mock
    }
  }
}
