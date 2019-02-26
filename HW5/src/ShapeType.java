public enum ShapeType {
  ELLIPSE("ellipse"), RECTANGLE("rectangle");

  private String type;

  ShapeType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
