import java.util.List;

/**
 *
 */
public class AnimationModelImpl implements AnimationModel {
  private List<Shape> shapes;

  @Override
  public void addEllipse(String name) {

  }

  @Override
  public void addRectangle(String name) {

  }

  /**
   * Adds a new Shape that can be animated
   * @param name the shape's name
   * @param type an enum describing the shape's shape (rectangle, ellipse)
   * @throws IllegalArgumentException if the shape name is not unique
   */
  private void addShape(String name, ShapeType type) {

  }

  @Override
  public void addMotion(String shapeName, int time, int x, int y, int width, int height,
                        int red, int green, int blue) {

  }

  @Override
  public void deleteLastMovement(String shapeName) {

  }

  @Override
  public void deleteShape(String shapeName) {

  }

  @Override
  public List<Shape> getShapes() {
    return null;
  }
}
