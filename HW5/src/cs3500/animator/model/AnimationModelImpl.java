package cs3500.animator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cs3500.animator.util.AnimationBuilder;

/**
 * A class representing an animation. It contains information about the various shapes
 * that can move, and the possible movements that the shapes make.
 */
public class AnimationModelImpl implements AnimationModel {
  private final List<Layer> layers; // earlier layers are drawn beneath later layers
  private final HashMap<String, Layer> layerMap;
  // INVARIANT: layerMap maps the layer's name to the layer.
  // INVARIANT: layers and layerMap have the same Layers stored.
  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Build an AnimationModel.
   */
  public AnimationModelImpl() {
    this.layers = new LinkedList<>();
    this.layerMap = new HashMap<>();
    // top left defaults to (0, 0)
    // width and height default to 1
    this.x = 0;
    this.y = 0;
    this.width = 1;
    this.height = 1;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and height must be positive integers.");
    }
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void addLayer(String layerName) {
    if (layerMap.containsKey(layerName)) {
      throw new IllegalArgumentException("There is already a layer by the name " + layerName);
    }
    Layer l = new Layer(layerName);
    layerMap.put(layerName, l);
    layers.add(l);
  }

  @Override
  public void deleteLayer(String layerName) {
    checkLayerExists(layerName);
    layers.remove(layerMap.remove(layerName)); // remove the layer from both the list and the map
  }

  @Override
  public void moveLayer(String layerName, int position) {
    checkLayerExists(layerName);
    if (position < 0 || position >= layerMap.size()) {
      throw new IllegalArgumentException("A layer's new position must fall within "
              + "the existing list size");
    }
    layers.remove(layerMap.get(layerName));
    layers.add(position, layerMap.get(layerName));
  }

  @Override
  public void addEllipse(String layer, String name) {
    addShape(layer, name, ShapeType.ELLIPSE);
  }

  @Override
  public void addRectangle(String layer, String name) {
    addShape(layer, name, ShapeType.RECTANGLE);
  }

  /**
   * Add a new Shape that can be animated to the given layer.
   *
   * @param shapeName the shape's name
   * @param layer the layer name
   * @param type an enum describing the shape's shape (rectangle, ellipse)
   * @throws IllegalArgumentException if the shape name is not unique
   */
  private void addShape(String layer, String shapeName, ShapeType type) {
    checkLayerExists(layer);
    layerMap.get(layer).addShape(new Shape(shapeName, type));
  }

  @Override
  public void deleteShape(String layer, String shapeName) {
    checkLayerExists(layer);
    layerMap.get(layer).deleteShape(shapeName);
  }

  @Override
  public void addMotion(String layer, String shapeName, int time, int x, int y,
                        int width, int height, int red, int green, int blue) {
    addMotion(layer, shapeName, time, x, y, width, height, red, green, blue, 0);
  }

  @Override
  public void addMotion(String layer, String shapeName, int time, int x, int y,
                        int width, int height,
                        int red, int green, int blue, int rotation) {
    checkLayerExists(layer);
    layerMap.get(layer).addMotion(shapeName, time, x, y, width, height, red, green, blue, rotation);
  }

  @Override
  public void editMotion(String layer, String shapeName, int time, int x, int y,
                        int width, int height, int red, int green, int blue) {
    editMotion(layer, shapeName, time, x, y, width, height, red, green, blue, 0);
  }

  @Override
  public void editMotion(String layer, String shapeName, int time, int x, int y,
                         int width, int height,
                         int red, int green, int blue, int rotation) {
    checkLayerExists(layer);
    layerMap.get(layer).editMotion(shapeName, time, x, y, width, height, red, green, blue, rotation);
  }

  @Override
  public void deleteMotion(String layer, String shapeName, int time) {
    checkLayerExists(layer);
    layerMap.get(layer).deleteMotion(shapeName, time);
  }

  @Override
  public void deleteLastMotion(String layer, String shapeName) {
    checkLayerExists(layer);
    layerMap.get(layer).deleteLastMotion(shapeName);
  }

  @Override
  public List<String> getLayers() {
    List<String> layerNames = new ArrayList<>(layerMap.size());
    for (Layer l : layers) {
      layerNames.add(l.getName());
    }
    return layerNames;
  }

  @Override
  public List<String> getShapes(String layer) {
    checkLayerExists(layer);
    return layerMap.get(layer).getShapes();
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Transformation getTransformationAt(String layer, String shapeName, int tick) {
    checkLayerExists(layer);
    return layerMap.get(layer).getTransformationAt(shapeName, tick);
  }

  @Override
  public List<Motion> getMotions(String layer, String shapeName) {
    checkLayerExists(layer);
    return layerMap.get(layer).getMotions(shapeName);
  }

  @Override
  public String getShapeType(String layer, String shapeName) {
    checkLayerExists(layer);
    return layerMap.get(layer).getShapeType(shapeName);
  }

  @Override
  public String displayAnimation() {
    List<String> layerDisplays = new ArrayList<>(layers.size());
    for (Layer l : layers) {
      layerDisplays.add(l.display());
    }
    return String.join("\n\n", layerDisplays);
  }

  /**
   * Check whether there is a layer with the given name. If not, throw an IAE.
   * @param layer the layer name to be confirmed
   * @throws IllegalArgumentException if the layer does not exist
   */
  private void checkLayerExists(String layer) {
    if (! layerMap.containsKey(layer)) {
      throw new IllegalArgumentException("There is no layer named " + layer);
    }
  }

  /**
   * A class representing a builder that can create an AnimationModel.
   * Acts as a wrapper so that AnimationReader can create an AnimationModel.
   * Each class can only be used once because the only way to reset the model is
   * through the constructor.
   */
  public static final class Builder implements AnimationBuilder<AnimationModel> {
    private final AnimationModel model;
    private String currentLayer;
    private int nextRotation = 0;

    public Builder() {
      this.model = new AnimationModelImpl();
    }

    @Override
    public AnimationModel build() {
      return model;
    }

    @Override
    public AnimationBuilder<AnimationModel> setBounds(int x, int y, int width, int height) {
      model.setBounds(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> declareLayer(String layerName) {
      if (layerName == null) {
        throw new IllegalArgumentException("Layer name must not be null");
      }
      if (! model.getLayers().contains(layerName)) {
        model.addLayer(layerName);
      }
      currentLayer = layerName;
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> declareRotation(int rotation) {
      this.nextRotation = rotation;
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> declareShape(String name, String type) {
      if (currentLayer == null) {
        declareLayer("layer1");
      }
      switch (type) {
        case "rectangle":
          model.addRectangle(currentLayer, name);
          break;
        case "ellipse":
          model.addEllipse(currentLayer, name);
          break;
        default:
          throw new IllegalArgumentException("Invalid shape type " + type);
      }
      return this;
    }

    /**
     * Because AnimationModelImpl uses keyframes instead of motions, only the end position of each
     * motion should be added as a keyframe to avoid repeats.
     * In the given examples the first movement of every shape consists of two keyframes
     * at the same time point with the same location, size, and color information.
     * This ensures that the first position is added correctly to the animation
     * even if only the end position of each motion is added.
     */
    @Override
    public AnimationBuilder<AnimationModel> addMotion(
            String name, int t1, int x1, int y1, int w1, int h1, int r1, int g1, int b1,
            int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2) {
      return addKeyframe(name, t2, x2, y2, w2, h2, r2, g2, b2);
    }

    @Override
    public AnimationBuilder<AnimationModel> addKeyframe(
            String name, int t, int x, int y, int w, int h, int r, int g, int b) {
      model.addMotion(currentLayer, name, t, x, y, w, h, r, g, b, nextRotation);
      return this;
    }
  }
}
