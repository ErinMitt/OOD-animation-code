import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AnimationModelImpl implements AnimationModel {
  /**
   * A list representing every shape that can appear in the animation.
   * Shapes can be accessed through their unique names.
   */
  private Map<String, Shape> shapes;

  public AnimationModelImpl() {
    this.shapes = new HashMap<>();
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
   * Adds a new Shape that can be animated
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
  public void deleteLastMotion(String shapeName) {
    checkShapeExists(shapeName);
    shapes.get(shapeName).deleteLastMotion();
  }

  @Override
  public List<String> getShapes() {
    return new ArrayList<>(shapes.keySet());
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
}
