package cs3500.animator.provider.view;

import java.util.Map;
import java.util.TreeMap;

import cs3500.animator.model.AnimationModel;

/**
 * A class that adapts an AnimationModel for use as an IModel.
 */
public class ModelAdapter implements IModel {
  private final AnimationModel model;

  public ModelAdapter(AnimationModel model) {
    this.model = model;
  }

  @Override
  public void addShape(String name, ShapeType type) {
    // this method is never used.
    throw new UnsupportedOperationException();
  }

  @Override
  public void makeChange(String name, int t1, int x1, int y1, int w1, int h1,
                         int r1, int g1, int b1,
                         int t2, int x2, int y2, int w2, int h2,
                         int r2, int g2, int b2) {
    // this method is never used
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, Shape> getShapes() {
    Map<String, Shape> shapes = new TreeMap<>();
    for (String layer : model.getLayers()) {
      for (String shape : model.getShapes(layer)) {
        shapes.put(shape, new Shape(layer, shape, model));
      }
    }
    return shapes;
  }

  @Override
  public int[] getScreenDimensions() {
    // this method is never used
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCanvas(int[] dim) {
    // this method is never used
    throw new UnsupportedOperationException();
  }
}
