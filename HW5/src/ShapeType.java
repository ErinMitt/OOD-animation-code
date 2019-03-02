/**
 * The class ShapeType represents which types of shapes can be represented in an animation.
 * This class is an enum so that new shape types (such as triangles) can be added.
 */
enum ShapeType {
  ELLIPSE("ellipse"), RECTANGLE("rectangle");

  private String type;

  ShapeType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
