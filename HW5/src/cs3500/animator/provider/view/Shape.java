package cs3500.animator.provider.view;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Motion;

/**
 * A class that mimics a "Shape" class in the provider code.
 */
public class Shape {
  private final String layer;
  private final String name;
  private final AnimationModel model;

  /**
   * Creates a "Shape" adapter.
   * @param layer the layer name
   * @param name the shape's name
   * @param model the model to which the shape belongs
   */
  public Shape(String layer, String name, AnimationModel model) {
    this.layer = layer;
    this.name = name;
    this.model = model;
  }

  /**
   * Find which type of shape this shape is.
   *
   * @return the ShapeType enum of this shape
   */
  public ShapeType getShapeType() {
    switch (model.getShapeType(layer, name)) {
      case "ellipse":
        return ShapeType.ELLIPSE;
      case "rectangle":
        return ShapeType.RECTANGLE;
      default:
        throw new IllegalArgumentException("Illegal shape type " + model.getShapeType(layer, name));
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
   * It has to implement SortedMap so that the compiler will like us.
   * Any unused methods throw an UnsupportedOperationException,
   * while methods that the provider code uses defer to the AnimationModel instead of a Map.
   * The original code returns the Shape's mutable Map of motions.
   * To interact with the model, the provider's view mutates the Map
   * rather than calling methods on Shape.
   * This class lets us pass any instructions made to the "Map" of motions on to the model.
   * It's not quite an adapter, but it's pretty close - it doesn't adapt our list of motions
   * to imitate the providers' list of motions, but it does transfer any changes made
   * to the providers' list of motions to our AnimationModel.
   */
  public class MoveList implements SortedMap<Integer, int[]> {
    private final String name;
    private final AnimationModel model;

    private MoveList(String name, AnimationModel model) {
      this.name = name;
      this.model = model;
    }

    /**
     * Does this shape have a Motion at the given time?.
     * @param key an Integer representing the time of motion
     * @return true if there is a motion at the given time
     */
    @Override
    public boolean containsKey(Object key) {
      for (Motion m : model.getMotions(layer, name)) {
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
      Motion m = model.getTransformationAt(layer, name, (int) key).getStateAt((int) key);
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
        model.addMotion(layer, name, key, value[0], value[1], value[2],
                value[3], value[4], value[5], value[6]);
      } catch (IllegalArgumentException e) {
        model.editMotion(layer, name, key, value[0], value[1], value[2],
                value[3], value[4], value[5], value[6]);
      }
      return null;
    }

    /**
     * Build a Map of MoveListEntry, each of which corresponds to a keyframe in the animation.
     * A MoveListEntry is able to mutate the model by calling setValue.
     * @return the set
     */
    @Override
    public Set<Map.Entry<Integer, int[]>> entrySet() {
      TreeSet<Map.Entry<Integer, int[]>> map = new TreeSet<>();
      for (Motion m : model.getMotions(layer, name)) {
        map.add(new MoveListEntry(model, name, m.getTime()));
      }
      return map;
    }

    /**
     * Does this shape have no motions?.
     * @return whether the shape's list of motions is empty
     */
    @Override
    public boolean isEmpty() {
      return model.getMotions(layer, name).isEmpty();
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
      return model.getMotions(layer, name).get(0).getTime();
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
      return model.getMotions(layer, name).get(model.getMotions(layer, name).size() - 1).getTime();
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
      model.deleteMotion(layer, name, (int) key);
      return m;
    }

    // unused methods

    @Override
    public void putAll(Map<? extends Integer, ? extends int[]> m) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
      throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, int[]> subMap(Integer fromKey, Integer toKey) {
      throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, int[]> headMap(Integer toKey) {
      throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, int[]> tailMap(Integer fromKey) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Collection<int[]> values() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
      return model.getMotions(layer, name).size();
    }
  }

  /**
   * A class representing a single keyframe of a shape at a given time.
   * Any mutation to this class carries over to the model.
   */
  public class MoveListEntry implements Map.Entry<Integer, int[]>, Comparable<MoveListEntry> {
    private final AnimationModel model;
    private final String name;
    private final int time;
    // INVARIANT: the model contains a keyframe of the shape "name" at this time.
    private Motion motion;
    // INVARIANT: motion has the same info as the Motion of the model at the given shape and time.

    /**
     * Build a MoveListEntry.
     * @param model the model that contains the shape
     * @param name the shape name
     * @param time the time of the keyframe
     */
    private MoveListEntry(AnimationModel model, String name, int time) {
      this.model = model;
      this.name = name;
      this.time = time;
      motion = model.getTransformationAt(layer, name, time).getStateAt(time);
    }

    /**
     * Get the time of this keyframe.
     * @return this keyframe's time
     */
    @Override
    public Integer getKey() {
      return time;
    }

    /**
     * Return this keyframe's information in the format [x, y, width, height, r, g, b].
     * @return the keyframe parameters.
     */
    @Override
    public int[] getValue() {
      return new int[] {motion.getX(), motion.getY(), motion.getWidth(), motion.getHeight(),
              motion.getRed(), motion.getGreen(), motion.getBlue()};
    }

    /**
     * Mutate the model's keyframe to match the given values.
     * @param value the keyframe's new parameters in the format [x, y, width, height, r, g, b]
     * @return the previous motion
     */
    @Override
    public int[] setValue(int[] value) {
      model.editMotion(layer, name, time, value[0], value[1], value[2], value[3],
              value[4], value[5], value[6]);

      Motion m = motion;
      motion = model.getTransformationAt(layer, name, time).getStateAt(time);
      return new int[] {m.getX(), m.getY(), m.getWidth(), m.getHeight(),
              m.getRed(), m.getGreen(), m.getBlue()};
    }

    /**
     * MoveListEntry is sorted according to its time - earlier times are sorted as "before".
     * @param o the other MoveListEntry
     * @return an int representing this MoveListEntry's relationship to a given MoveListEntry
     */
    @Override
    public int compareTo(MoveListEntry o) {
      return Integer.compare(this.getKey(), o.getKey());
    }

    /**
     * This method is not overridden because it is not used in the provider code.
     * @param o the other object to be compared
     * @return nothing
     */
    @Override
    public boolean equals(Object o) {
      throw new UnsupportedOperationException();
    }

    /**
     * This method is not overridden because it is not used in the provider code.
     * @return nothing
     */
    @Override
    public int hashCode() {
      throw new UnsupportedOperationException();
    }
  }
}
