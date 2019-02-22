import java.util.List;

public class ShapeImpl implements Shape {
  private List<Motion> motions;
  private ShapeType type;
  private String name;

  @Override
  public List<Motion> getMotions() {
    return null;
  }

  @Override
  public void addMotion(int time, int x, int y, int width, int height,
                        int red, int green, int blue) {

  }

  @Override
  public void deleteLastMotion() {

  }

  @Override
  public boolean sameShape(String name) {
    return name.equals(this.name);
  }
}