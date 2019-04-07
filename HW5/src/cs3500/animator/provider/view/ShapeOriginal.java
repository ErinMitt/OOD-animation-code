package cs3500.animator.provider.view;

import java.util.ArrayList;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Object for all shapes. Each shape will have its own restriction list for the three types of
 * changes that casn be made to it. These lists ensure no two of the same changes can have
 * overlapping times. Each componenet of the list is a pair of ints giving the starting and
 * finishing time. The shape type will be known through the enum shapetype. Each shap has a central
 * location, a color, a width and a height. (An extra variable needs to be added for trapezoids).
 * Originally the animation model contained a map of the changes to each shape that would be used to
 * get the gamestate. Things got messy with all the shapes in the map together so I am in the
 * process of giving each shape its own map. I wanted the map to work like this the key would be an
 * integer defining the instantaneous time and the value would be a int[] comprised of all current
 * info on the shape (center, color, width, height). So at the start and end of every change to the
 * shape an entry will be made. This means that the outside code will need to recognize which
 * variables are changing to know whether it is a move, color change, or size change. For the map I
 * used a treemap so even if the changes are entered out of order with respect to time they can be
 * sorted into the correct order.
 */
public class ShapeOriginal {

  //restricts time of movements, color change and scale change
  private List<int[]> restrictions;
  //index for the type of shape
  private ShapeType shapeType;
  private int[] center = new int[2];
  private int[] color = new int[3];
  private int width;
  private int height;
  //all actions preformed by shape
  private SortedMap<Integer, int[]> shapeMoves;


  /**
   * Constructor that initializes the type of the shape, creates a new ArrayList of restrictions.
   * Creates a new TreeMap of moves of the shape.
   *
   * @param type type of shape
   */

  public ShapeOriginal(ShapeType type) {
    this.restrictions = new ArrayList<>();
    this.shapeType = type;
    this.shapeMoves = new TreeMap<>();
  }

  /**
   * Checks whether there have been any actions made on the shapes.
   *
   * @return true if there has been no action; false otherwise
   */

  boolean noActions() {

    return shapeMoves.isEmpty();
  }

  /**
   * Adds a new move the shape.
   *
   * @param t       time frame
   * @param details the parameters of the shape
   */

  void addMove(int t, int[] details) {
    int[] newDetails = {details[0], details[1], details[2], details[3], details[4], details[5],
            details[6], 1};
    if (shapeMoves.containsKey(t)) {
      shapeMoves.put(t, newDetails);
    } else {
      shapeMoves.put(t, details);
    }
  }

  /**
   * Sets time restrictions.
   *
   * @param t time frame
   */

  void setRestrictions(int[] t) {
    restrictions.add(t);
  }

  /**
   * Checks time restrictions.
   *
   * @param t array of time frames
   * @return true if restrictions are good, false otherwise
   */
  boolean checkRestriction(int[] t) {
    boolean good = true;
    for (int[] ints : restrictions) {
      if (t[0] >= ints[0] && t[0] < ints[1]) {
        good = false;
      } else if (t[1] > ints[0] && t[1] <= ints[1]) {
        good = false;
      }
    }
    return good;
  }

  /**
   * This is used to get the details of a shape.
   *
   * @return all details of the shape
   */
  public int[] getDetails() {
    int[] details = new int[7];
    details[0] = center[0];
    details[1] = center[1];
    details[2] = width;
    details[3] = height;
    details[4] = color[0];
    details[5] = color[1];
    details[6] = color[2];
    return details;
  }

  /**
   * Gets the shape type.
   *
   * @return type of the shape
   */

  public ShapeType getShapeType() {
    return shapeType;
  }

  /**
   * Gets the center.
   *
   * @return center
   */

  public int[] getCenter() {
    return center;
  }

  /**
   * Gets the color of the shapes.
   *
   * @return the color values
   */

  public int[] getColor() {
    return color;
  }

  /**
   * Gets the width of the shape.
   *
   * @return the width
   */

  public int getWidth() {
    return width;
  }

  /**
   * Gets the height of the shape.
   *
   * @return the height
   */

  public int getHeight() {
    return height;
  }

  /**
   * Sets the parameters of the shapes.
   *
   * @param details array of ints consisting of the details of the shape
   */

  public void setDetails(int[] details) {
    center[0] = details[0];
    center[1] = details[1];
    width = details[2];
    height = details[3];
    color[0] = details[4];
    color[1] = details[5];
    color[2] = details[6];
  }

  /**
   * Sorts the Map of the moves.
   *
   * @return the shape moves being sorted
   */

  public SortedMap<Integer, int[]> getMoves() {
    return shapeMoves;
  }
}

