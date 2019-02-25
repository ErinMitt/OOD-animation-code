import java.util.ArrayList;
import java.util.List;

public class ShapeImpl implements Shape {
  private List<Motion> motions;
  private ShapeType type;
  private String name;

  public ShapeImpl(String name, ShapeType type) {
    this.name = name;
    this.type = type;
    this.motions = new ArrayList<Motion>();
  }

  @Override
  public List<Motion> getMotions() {
    return new ArrayList<>(motions);
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