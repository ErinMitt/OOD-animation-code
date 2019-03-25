package cs3500.animator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cs3500.animator.util.AnimationBuilder;

/**
 * A class representing an animation. It contains information about the various shapes
 * that can move, and the possible movements that the shapes make.
 */
public class AnimationModelImpl implements AnimationModel {
  /**
   * A list representing every shape that can appear in the animation.
   * Shapes can be accessed through their unique names.
   */
  private final Map<String, Shape> shapes;
  // INVARIANT: each Shape's key is the same String as its name.
  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Build an AnimationModel.
   */
  public AnimationModelImpl() {
    this.shapes = new LinkedHashMap<>();
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
  public void addEllipse(String name) {
    addShape(name, ShapeType.ELLIPSE);
  }

  @Override
  public void addRectangle(String name) {
    addShape(name, ShapeType.RECTANGLE);
  }

  /**
   * Add a new Shape that can be animated.
   *
   * @param name the shape's name
   * @param type an enum describing the shape's shape (rectangle, ellipse)
   * @throws IllegalArgumentException if the shape name is not unique
   */
  private void addShape(String name, ShapeType type) {
    if (shapes.containsKey(name)) {
      throw new IllegalArgumentException("Every shape must have a unique name. The shape " + name
              + " already exists.");
    }
    shapes.put(name, new Shape(name, type));
  }

  @Override
  public void deleteShape(String shapeName) {
    checkShapeExists(shapeName);
    shapes.remove(shapeName);
  }

  @Override
  public void addMotion(String shapeName, int time, int x, int y, int width, int height,
                        int red, int green, int blue) {
    checkShapeExists(shapeName);
    // may throw an IAE if the time is incorrect
    shapes.get(shapeName).addMotion(time, x, y, width, height, red, green, blue);
  }

  @Override
  public void extend(String shapeName, int time) {
    checkShapeExists(shapeName);
    shapes.get(shapeName).extend(time);
  }

  @Override
  public void editMotion(String shapeName, int time, int x, int y, int width, int height,
                         int red, int green, int blue) {
    checkShapeExists(shapeName);
    // Motion constructor check for validity of inputs, throw IAE if invalid
    Motion m = new Motion(time, x, y, width, height, red, green, blue);
    Shape shape = shapes.get(shapeName);
    shape.deleteMotionAt(time); // throws IAE if there is no motion at the given time
    shape.addMotion(m);
  }

  @Override
  public void deleteMotion(String shapeName, int time) {
    checkShapeExists(shapeName);
    shapes.get(shapeName).deleteMotionAt(time); // throws IAE if there is not motion at that time
  }

  @Override
  public void deleteLastMotion(String shapeName) {
    checkShapeExists(shapeName);
    shapes.get(shapeName).deleteLastMotion();
  }

  @Override
  public List<String> getShapes() {
    return new ArrayList<>(shapes.keySet());
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
  public Transformation getTransformationAt(String shapeName, int tick) {
    checkShapeExists(shapeName);
    return shapes.get(shapeName).getTransformationAt(tick);
  }

  @Override
  public List<Motion> getMotions(String shapeName) {
    checkShapeExists(shapeName);
    return shapes.get(shapeName).getMotions();
  }

  @Override
  public String getShapeType(String shapeName) {
    checkShapeExists(shapeName);
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
   * Check whether the shape with the given name exists. If not, throw an IAE.
   * @param shapeName the shape whose existence is to be confirmed
   * @throws IllegalArgumentException if the shape does not exist
   */
  private void checkShapeExists(String shapeName) {
    if (! shapes.containsKey(shapeName)) {
      throw new IllegalArgumentException("No shape with the name " + shapeName + " exists.");
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
