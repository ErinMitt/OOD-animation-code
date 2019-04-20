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
   * Return a list of all shape names in this layer.
   * @return an ArrayList of shape names.
   */
  public List<String> getShapes() {
    return new ArrayList<>(shapes.keySet());
  }
}
