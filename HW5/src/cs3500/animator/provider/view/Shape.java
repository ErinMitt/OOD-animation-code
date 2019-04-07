package cs3500.animator.provider.view;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Motion;

/**
 * A class that mimics a "Shape" class in the provider code.
 */
public class Shape {
  private final String name;
  private final AnimationModel model;

  /**
   * Creates a "Shape" adapter.
   * @param name the shape's name
   * @param model the model to which the shape belongs
   */
  public Shape(String name, AnimationModel model) {
    this.name = name;
    this.model = model;
  }

  /**
   * Find which type of shape this shape is.
   * @return the ShapeType enum of this shape
   */
  public ShapeType getShapeType() {
    switch (model.getShapeType(name)) {
      case "ellipse":
        return ShapeType.ELLIPSE;
      case "rectangle":
        return ShapeType.RECTANGLE;
      default:
        throw new IllegalArgumentException("Illegal shape type " + model.getShapeType(name));
    }
  }

  /**
   * Return a MoveList that gives access to the model's motions for the given shape.
   */
  public MoveList getMoves() {
    return new MoveList(name, model);
  }

  /**
   * A class that mimics a TreeMap. It can mutate the model held by the shape class.
   * It has to extend TreeMap so that the compiler will like us,
   * but any methods that are actually used in the view are overridden.
   *
   * The original code returns the Shape's mutable Map of motions.
   * To interact with the model, the provider's view mutates the Map
   * rather than calling methods on Shape.
   * This class lets us pass any instructions made to the "Map" of motions on to the model.
   *
   * It's not quite an adapter, but it's pretty close - it doesn't adapt our list of motions
   * to imitate the providers' list of motions, but it does transfer any changes made
   * to the providers' list of motions to our AnimationModel.
   */
  public class MoveList extends TreeMap<Integer, int[]> {
    private final String name;
    private final AnimationModel model;

    private MoveList(String name, AnimationModel model) {
      this.name = name;
      this.model = model;
    }

    /**
     * Does this shape have a Motion at the given time?
     * @param key an Integer representing the time of motion
     * @return true if there is a motion at the given time
     */
    @Override
    public boolean containsKey(Object key) {
      for (Motion m : model.getMotions(name)) {
        if (m.getTime() == (int) key) {
          return true;
        }
      }
      return false;
    }

    /**
     * Get the Motion information as an Array in the format [x, y, width, height, r, g, b].
     * Assumes that containsKey has already been called.
     * @param key an Integer representing the time of the motion
     * @return the Motion information
     */
    @Override
    public int[] get(Object key) {
      Motion m = model.getTransformationAt(name, (int) key).getStateAt((int) key);
      return new int[]{m.getX(), m.getY(), m.getWidth(), m.getHeight(),
              m.getRed(), m.getGreen(), m.getBlue()};
    }

    /**
     * Add a Motion to the shape at the given time and parameters.
     * If there is already a Motion there, override it.
     * @param key the time
     * @param value the parameters (x, y, width, height, r, g, b)
     * @return null
     */
    @Override
    public int[] put(Integer key, int[] value) {
      try {
        model.addMotion(name, key, value[0], value[1], value[2],
                value[3], value[4], value[5], value[6]);
      } catch (IllegalArgumentException e) {
        model.editMotion(name, key, value[0], value[1], value[2],
                value[3], value[4], value[5], value[6]);
      }
      return null;
    }

    /**
     * Build an entrySet of all of the shape's Motions mapped to the time they happen at.
     * @return the set
     */
    @Override
    public Set<Map.Entry<Integer, int[]>> entrySet() {
      TreeMap<Integer, int[]> map = new TreeMap<>();
      for (Motion m : model.getMotions(name)) {
        map.put(m.getTime(), new int[]{m.getX(), m.getY(), m.getWidth(), m.getHeight(),
                m.getRed(), m.getGreen(), m.getBlue()});
      }
      return map.entrySet();
    }

    /**
     * Does this shape have no motions?
     * @return whether the shape's list of motions is empty
     */
    @Override
    public boolean isEmpty() {
      return model.getMotions(name).isEmpty();
    }

    /**
     * Find the time of the shape's first motion.
     * @return the time of the first Motion
     * @throws java.util.NoSuchElementException if there are no motions
     */
    @Override
    public Integer firstKey() {
      if (isEmpty()) {
        throw new NoSuchElementException("The shape " + name + " has no motions");
      }
      return model.getMotions(name).get(0).getTime();
    }

    /**
     * Find the time of the shape's last motion.
     * @return the time of the last Motion
     * @throws java.util.NoSuchElementException if there are no motions
     */
    @Override
    public Integer lastKey() {
      if (isEmpty()) {
        throw new NoSuchElementException("The shape " + name + " has no motions");
      }
      return model.getMotions(name).get(model.getMotions(name).size() - 1).getTime();
    }

    /**
     * Remove the motion at the given time and return the motion's parameters.
     * @param key the Integer representing the time
     * @return the parameters is the form [x, y, width, height, r, g, b]
     *     or null is there is no motion at that time
     */
    @Override
    public int[] remove(Object key) {
      if (! containsKey(key)) {
        return null;
      }
      int[] m = get(key);
      model.deleteMotion(name, (int) key);
      return m;
    }
  }
}
