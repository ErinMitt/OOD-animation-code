import java.util.List;

public interface Shape {
  List<Motion> getMotions();
  void addMotion(int time, int x, int y, int width, int height,
                 int red, int green, int blue);
  void deleteLastMotion();
  boolean sameShape(String name);
}
