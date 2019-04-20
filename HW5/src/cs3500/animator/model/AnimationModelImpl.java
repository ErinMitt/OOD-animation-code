package cs3500.animator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs3500.animator.util.AnimationBuilder;

/**
 * A class representing an animation. It contains information about the various shapes
 * that can move, and the possible movements that the shapes make.
 */
public class AnimationModelImpl implements AnimationModel {
  private final List<Layer> layers;
  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Build an AnimationModel.
   */
  public AnimationModelImpl() {
    this.layers = new LinkedList<>();
    // top left defaults to (0, 0)
    // width and height default to 1
    this.x = 0;
    this.y = 0;
    this.width = 1;
    this.height = 1;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and height must be positive integers.");
    }
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void addEllipse(int layer, String name) {
    addShape(layer, name, ShapeType.ELLIPSE);
  }

  @Override
  public void addRectangle(int layer, String name) {
    addShape(layer, name, ShapeType.RECTANGLE);
  }

  /**
   * Add a new Shape that can be animated to the given layer.
   *
   * @param shapeName the shape's name
   * @param layer the layer number
   * @param type an enum describing the shape's shape (rectangle, ellipse)
   * @throws IllegalArgumentException if the shape name is not unique
   */
  private void addShape(int layer, String shapeName, ShapeType type) {
    if (layer < 0 || layer >= layers.size()) {
      throw new IllegalArgumentException("There is no layer at position " + layer);
    }
    layers.get(layer).addShape(new Shape(shapeName, type));
  }

  @Override
  public void deleteShape(int layer, String shapeName) {
    checkShapeExists(layer, shapeName);
    layers.get(layer).remove(shapeName);
  }

  @Override
  public void addMotion(int layer, String shapeName, int time, int x, int y, int width, int height,
                        int red, int green, int blue) {
    checkShapeExists(layer, shapeName);
    // may throw an IAE if the time is incorrect
    shapes.get(shapeName).addMotion(time, x, y, width, height, red, green, blue);
  }

  @Override
  public void extend(int layer, String shapeName, int time) {
    checkShapeExists(layer, shapeName);
    shapes.get(shapeName).extend(time);
  }

  @Override
  public void editMotion(int layer, String shapeName, int time, int x, int y, int width, int height,
                         int red, int green, int blue) {
    checkShapeExists(layer, shapeName);
    // Motion constructor check for validity of inputs, throw IAE if invalid
    Motion m = new Motion(time, x, y, width, height, red, green, blue);
    Shape shape = shapes.get(shapeName);
    shape.deleteMotionAt(time); // throws IAE if there is no motion at the given time
    shape.addMotion(m);
  }

  @Override
  public void deleteMotion(int layer, String shapeName, int time) {
    checkShapeExists(layer, shapeName);
    shapes.get(shapeName).deleteMotionAt(time); // throws IAE if there is no motion at that time
  }

  @Override
  public void deleteLastMotion(int layer, String shapeName) {
    checkShapeExists(layer, shapeName);
    shapes.get(shapeName).deleteLastMotion();
  }

  @Override
  public List<List<String>> getShapes() {
    ArrayList<List<String>> shapeNames = new ArrayList<>(layers.size());
    for (Layer l : layers) {
      shapeNames.add(l.getShapes());
    }
    return shapeNames;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Transformation getTransformationAt(int layer, String shapeName, int tick) {
    checkShapeExists(layer, shapeName);
    return shapes.get(shapeName).getTransformationAt(tick);
  }

  @Override
  public List<Motion> getMotions(int layer, String shapeName) {
    checkShapeExists(layer, shapeName);
    return shapes.get(shapeName).getMotions();
  }

  @Override
  public String getShapeType(int layer, String shapeName) {
    checkShapeExists(layer, shapeName);
    return shapes.get(shapeName).getShapeType();
  }

  @Override
  public String displayAnimation() {
    List<String> shapeDisplays = new ArrayList<>(shapes.size());
    for (Shape s : shapes.values()) {
      shapeDisplays.add(s.display());
    }
    return String.join("\n\n", shapeDisplays);
  }

  /**
   * Check whether the shape with the given name exists in the given layer. If not, throw an IAE.
   * @param layer the layer number
   * @param shapeName the shape whose existence is to be confirmed
   * @throws IllegalArgumentException if the shape does not exist
   */
  private void checkShapeExists(int layer, String shapeName) {
    if (layer < 0 || layer >= layers.size()) {
      throw new IllegalArgumentException("There is no layer at position " + layer);
    }
    if (! layers.get(layer).hasShape(shapeName)) {
      throw new IllegalArgumentException("No shape with the name " + shapeName +
              " exists in the layer " + layer + ".");
    }
  }

  /**
   * A class representing a builder that can create an AnimationModel.
   * Acts as a wrapper so that AnimationReader can create an AnimationModel.
   * Each class can only be used once because the only way to reset the model is
   * through the constructor.
   */
  public static final class Builder implements AnimationBuilder<AnimationModel> {
    private final AnimationModel model;

    public Builder() {
      this.model = new AnimationModelImpl();
    }

    @Override
    public AnimationModel build() {
      return model;
    }

    @Override
    public AnimationBuilder<AnimationModel> setBounds(int x, int y, int width, int height) {
      model.setBounds(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> declareShape(String name, String type) {
      switch (type) {
        case "rectangle":
          model.addRectangle(name);
          break;
        case "ellipse":
          model.addEllipse(name);
          break;
        default:
          throw new IllegalArgumentException("Invalid shape type " + type);
      }
      return this;
    }

    /**
     * Because AnimationModelImpl uses keyframes instead of motions, only the end position of each
     * motion should be added as a keyframe to avoid repeats.
     * In the given examples the first movement of every shape consists of two keyframes
     * at the same time point with the same location, size, and color information.
     * This ensures that the first position is added correctly to the animation
     * even if only the end position of each motion is added.
     */
    @Override
    public AnimationBuilder<AnimationModel> addMotion(
            String name, int t1, int x1, int y1, int w1, int h1, int r1, int g1, int b1,
            int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2) {
      return addKeyframe(name, t2, x2, y2, w2, h2, r2, g2, b2);
    }

    @Override
    public AnimationBuilder<AnimationModel> addKeyframe(
            String name, int t, int x, int y, int w, int h, int r, int g, int b) {
      model.addMotion(name, t, x, y, w, h, r, g, b);
      return this;
    }
  }
}
