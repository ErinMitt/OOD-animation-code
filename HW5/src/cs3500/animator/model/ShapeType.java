package cs3500.animator.model;

/**
 * The class ShapeType represents which types of shapes can be represented in an animation.
 * This class is an enum so that new shape types (such as triangles) can be added.
 */
enum ShapeType {
  ELLIPSE("ellipse"), RECTANGLE("rectangle");

  private String type;

  /**
   * Build an enum representing a shape type.
   * @param type the type of shape
   */
  ShapeType(String type) {
    this.type = type;
  }

  /**
   * Return a string representing the shape type's name.
   * @return the type of shape
   */
  public String getType() {
    return type;
  }
}
