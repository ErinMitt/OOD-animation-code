package cs3500.animator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing a single layer in an animation.
 */
class Layer {
  /**
   * A list representing every shape that can appear in the animation.
   * Shapes can be accessed through their unique names.
   */
  private final Map<String, Shape> shapes;
  // INVARIANT: each Shape's key is the same String as its name.
  private String name;

  /**
   * Build a Layer with no shapes.
   *
   * @param name the layer name
   */
  Layer(String name) {
    this.shapes = new LinkedHashMap<>();
    this.name = name;
  }

  /**
   * Get the shape name.
   * @return the shape name
   */
  public String getName() {
    return name;
  }

  /**
   * Does this layer contain a shape by the given name.
   * @param shapeName the shape name
   * @return true if the shape exists
   */
  public boolean hasShape(String shapeName) {
    return shapes.containsKey(shapeName);
  }

  /**
   * Add the given shape to this layer's list of shapes.
   * @param shape the shape to be added
   * @throw IllegalArgumentException if there is already a shape by the given name in this layer
   */
  public void addShape(Shape shape) {
    if (hasShape(shape.getName())) {
      throw new IllegalArgumentException("Every shape must have a unique name. The shape "
              + shape.getName() + " already exists in the layer " + name);
    }
    shapes.put(shape.getName(), shape);
  }

  /**
   * Delete the given shape and all of its motions from this layer.
   * @param shapeName the name of the shape to be deleted
   */
  public void deleteShape(String shapeName) {
    checkShapeExists(shapeName);
    shapes.remove(shapeName);
  }

  /**
   * Add a motion of the given parameters to the given shape.
   * @param shapeName the shape's name
   * @param time the time of the new motion
   * @param x the new motion's x coordinate
   * @param y the new motion's y coordinate
   * @param width the new motion's width
   * @param height the new motion's height
   * @param red the new motion's r component of rgb
   * @param green the new motion's g component of rgb
   * @param blue the new motion's b component of rgb
   * @param rotation the shape's rotation
   */
  public void addMotion(String shapeName, int time, int x, int y, int width, int height,
                        int red, int green, int blue, int rotation) {
    checkShapeExists(shapeName);
    // may throw an IAE if the time is incorrect
    shapes.get(shapeName).addMotion(time, x, y, width, height, red, green, blue, rotation);
  }

  /**
   * Replace the motion sof the given shape at the given time with a new motion of the given params.
   * @param shapeName the shape's name
   * @param time the motion's time
   * @param x the new motion's x coordinate
   * @param y the new motion's y coordinate
   * @param width the new motion's width
   * @param height the new motion's height
   * @param red the new motion's r component of rgb
   * @param green the new motion's g component of rgb
   * @param blue the new motion's b component of rgb
   * @param rotation the shape's rotation
   */
  public void editMotion(String shapeName, int time, int x, int y, int width, int height,
                         int red, int green, int blue, int rotation) {
    checkShapeExists(shapeName);
    // Motion constructor check for validity of inputs, throw IAE if invalid
    Motion m = new Motion(time, x, y, width, height, red, green, blue, rotation);
    Shape shape = shapes.get(shapeName);
    shape.deleteMotionAt(time); // throws IAE if there is no motion at the given time
    shape.addMotion(m);
  }

  /**
   * Delete the motion of the given shape at the given time.
   * @param shapeName the shape to remove a motion form
   * @param time the time of the motion to be removed
   */
  public void deleteMotion(String shapeName, int time) {
    checkShapeExists(shapeName);
    shapes.get(shapeName).deleteMotionAt(time); // throws IAE if there is no motion at that time
  }

  /**
   * Delete the last motion of the given shape.
   * @param shapeName the name of the shape from which to delete a motion.
   */
  public void deleteLastMotion(String shapeName) {
    checkShapeExists(shapeName);
    shapes.get(shapeName).deleteLastMotion();
  }

  /**
   * Return a list of all shape names in this layer.
   * @return an ArrayList of shape names.
   */
  public List<String> getShapes() {
    return new ArrayList<>(shapes.keySet());
  }

  /**
   * Concatenate the list of motions of every shape in the layer
   * under a header naming this layer.
   * @return a String describing the movements of all shapes in this layer.
   */
  public String display() {
    List<String> shapeDisplays = new ArrayList<>(shapes.size() + 1);
    shapeDisplays.add("layer " + name);
    for (Shape s : shapes.values()) {
      shapeDisplays.add(s.display());
    }
    return String.join("\n", shapeDisplays);
  }

  /**
   * Return the Transformation of the given shape at the given time.
   * @param shapeName the name of the desired shape
   * @param tick the time of the transformation
   * @return the transformation currently happening
   */
  public Transformation getTransformationAt(String shapeName, int tick) {
    checkShapeExists(shapeName);
    return shapes.get(shapeName).getTransformationAt(tick);
  }

  /**
   * return a list of every motion of the given shape.
   * @param shapeName the name of the shape
   * @return all of the shape's motions
   */
  public List<Motion> getMotions(String shapeName) {
    checkShapeExists(shapeName);
    return shapes.get(shapeName).getMotions();
  }

  /**
   * Return a string representing what type of shape the given shape is.
   * The string must be one of: rectangle, ellipse.
   * @param shapeName the shape name
   * @return the shape type
   */
  public String getShapeType(String shapeName) {
    checkShapeExists(shapeName);
    return shapes.get(shapeName).getShapeType();
  }

  /**
   * Check whether the shape with the given name exists. If not, throw an IAE.
   * @param shapeName the shape whose existence is to be confirmed
   * @throws IllegalArgumentException if the shape does not exist
   */
  private void checkShapeExists(String shapeName) {
    if (! hasShape(shapeName)) {
      throw new IllegalArgumentException("No shape with the name " + shapeName +
              " exists in the layer " + name + ".");
    }
  }
}
