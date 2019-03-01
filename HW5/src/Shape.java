import java.util.ArrayList;
import java.util.List;

class Shape {
  private List<Motion> motions;
  // INVARIANT: Motions are sorted in order of increasing time
  private ShapeType type;
  private String name;

  /**
   * Build a shape with no motions. The name and type must not be null.
   *
   * @param name the shape's name
   * @param type the shape's type
   */
   Shape(String name, ShapeType type) {
    if (name == null || type == null) {
      throw new IllegalArgumentException("Name and shape type must not be null.");
    }
    this.name = name;
    this.type = type;
    this.motions = new ArrayList<>();
  }

  /**
   * Add a new Motion with the given parameters to the end of the list.
   * The time must be after the time of the last existing motion already assigned to this shape.
   * RGB values must be between 0 and 255 and are corrected if they are outside those bounds.
   * Width and height must be positive nonzero integers.
   *
   * @param time the time at which the motion occurs
   * @param x the x coordinate of the shape
   * @param y the y coordinate of the shape
   * @param width the shape's width
   * @param height the shape's height
   * @param red the R component of the shape's color
   * @param green the G component of the shape's color
   * @param blue the B component of the shape's color
   * @throws IllegalArgumentException if the given time is less than or equal to the time of the
   * last existing motion, or if the width/height are less than or equal to 0
   */
   void addMotion(int time, int x, int y, int width, int height,
                        int red, int green, int blue) {
    addMotion(new Motion(time, x, y, width, height, red, green, blue));
  }

  /**
   * Adds a new Motion identical to this shape's last existing motion that is identical but
   * occurs at a later time. Functionally, this leaves the shape unchanging until the given time.
   *
   * @param time the time to which the last motion should be extended
   * @throws IllegalArgumentException if the given time is equal to or before the last motion's time
   * @throws IllegalStateException if there is no motion to extend
   */
   void extend(int time) {
    if (motions.isEmpty()) {
      throw new IllegalStateException("A shape with no motions "
              + "cannot have its last motion extended.");
    }
    addMotion(motions.get(motions.size() - 1).extend(time));
  }

  /**
   * Add the given motion to the sequence of motions.
   *
   * @param m the motion to be added
   * @throws IllegalArgumentException if the new Motion's time is less than or equal to the
   * last existing motion's time
   */
  private void addMotion(Motion m) {
    if (m == null) {
      throw new IllegalArgumentException("The motion cannot be null.");
    }
    if (!motions.isEmpty() &&  m.getTime() <= motions.get(motions.size() - 1).getTime()) {
      throw new IllegalArgumentException("Cannot add a new motion into the middle of a sequence. "
              + "New motions must occur after this shape's last existing motion.");
    }
    motions.add(m);
  }

  /**
   * Remove the last motion in the sequence.
   *
   * @throws IllegalStateException if this shape has no motions
   */
   void deleteLastMotion() {
    if (motions.isEmpty()) {
      throw new IllegalStateException("There are no motions to remove.");
    }
    motions.remove(motions.size() - 1);
  }

  /**
   * Display every motion in order in the format:
   * shape C ellipse
   * #                  start                           end
   * #        --------------------------    ----------------------------
   * #        t  x   y   w  h   r   g  b    t   x   y   w  h   r   g  b
   * motion C 6  440 70 120 60 0 0 255      20  440 70  120 60 0 0 255
   * motion C 20 440 70 120 60 0 0 255      50  440 250 120 60 0 0 255
   * motion C 50 440 250 120 60 0 0 255     70  440 370 120 60 0 170 85
   * motion C 70 440 370 120 60 0 170 85    80  440 370 120 60 0 255  0
   * motion C 80 440 370 120 60 0 255  0    100 440 370 120 60 0 255  0
   * Lines beginning with # are informative and are not included in the final string.
   *
   * @return the display string
   */
   String display() {
    List<String> lines = new ArrayList<>(motions.size());
    lines.add("shape " + name + " " + type.getType());
    if (motions.size() == 1) {
      lines.add("motion " + name + " " + motions.get(0).display()
              + "    " + motions.get(0).display());
    }
    else {
      for (int i = 0; i < motions.size() - 1; i++) {
        lines.add("motion " + name + " " + motions.get(i).display()
                + "    " + motions.get(i + 1).display());
      }
    }
    return String.join("\n", lines);
  }
}