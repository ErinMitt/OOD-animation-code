package cs3500.animator.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

/**
 * A class representing a view that can produce SVG-formatted texts of an animation
 * represented by an AnimationModel.
 */
public class SVGView implements AnimationView {
  private ReadOnlyModel model;
  private Appendable output;
  private double speed = 1;

  @Override
  public void setSpeed(double speed) {
    if (speed <= 0) {
      throw new IllegalArgumentException("Number of ticks per second must be a positive number, "
              + "given " + speed);
    }
    this.speed = speed;
  }

  @Override
  public void setOutput(Appendable output) {
    if (output == null) {
      throw new IllegalArgumentException("Output Appendable must not be null");
    }
    this.output = output;
  }

  @Override
  public void setModel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    if (this.model != null) {
      throw new IllegalStateException("This view already has a model");
    }
    this.model = model;
  }

  /**
   * Creates a text formatted for SVG graphics based on the animation described in the model.
   */
  @Override
  public void animate() {
    if (output == null) {
      throw new IllegalStateException("The output Appendable has not been set");
    }
    if (model == null) {
      throw new IllegalStateException("The model has not been set");
    }
    try {
      output.append(formatAnimation());
    } catch (IOException e) {
      throw new IllegalStateException("Could not write to the output");
    }
  }

  /**
   * Creates an SVG-formatted text representing the animation described in the model.
   * @return the SVG text
   */
  private String formatAnimation() {
    LinkedList<String> svgLines = new LinkedList<>();
    svgLines.add("<svg width=\"" + (model.getWidth() + model.getX())
            + "\" height=\"" + (model.getHeight() + model.getY())
            + "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
    for (String layer : model.getLayers()) {
      for (String shape : model.getShapes(layer)) {
        List<Motion> motions = model.getMotions(layer, shape);
        switch (model.getShapeType(layer, shape)) {
          case "ellipse":
            if (!motions.isEmpty()) {
              svgLines.add(initEllipse(motions.get(0), shape));
              for (int i = 0; i < motions.size() - 1; i++) {
                svgLines.addAll(moveEllipse(motions.get(i), motions.get(i + 1), shape));
              }
              svgLines.add("</ellipse>");
            }
            break;
          case "rectangle":
            if (!motions.isEmpty()) {
              svgLines.add(initRect(motions.get(0), shape));
              for (int i = 0; i < motions.size() - 1; i++) {
                svgLines.addAll(moveRect(motions.get(i), motions.get(i + 1), shape));
              }
              svgLines.add("</rect>");
            }
            break;
          default:
            throw new IllegalArgumentException("Invalid shape type "
                    + model.getShapeType(layer, shape));
        }
      }
    }
    svgLines.add("</svg>");
    return String.join("\n", svgLines);
  }

  /**
   * Create the opening line for a rectangle at the initial position described in the given motion.
   * @param m the rectangle's initial position
   * @param shape the shape name
   * @return the starting text for a rectangle
   */
  private String initRect(Motion m, String shape) {
    return "<rect id=\"" + shape
            + "\" x=\"" + (m.getX())
            + "\" y=\"" + (m.getY())
            + "\" width=\"" + m.getWidth()
            + "\" height=\"" + m.getHeight()
            + "\" fill=\"rgb(" + m.getRed()
            + "," + m.getGreen()
            + "," + m.getBlue()
            + ")\" visibility=\"visible\" >";
  }

  /**
   * Create the opening line for an ellipse at the initial position described in the given motion.
   * @param m the ellipse's original position
   * @param shape the ellipse's name
   * @return the starting text for an ellipse
   */
  private String initEllipse(Motion m, String shape) {
    return "<ellipse id=\"" + shape
            + "\" cx=\"" + (m.getX())
            + "\" cy=\"" + (m.getY())
            + "\" rx=\"" + (m.getWidth() / 2)
            + "\" ry=\"" + (m.getHeight() / 2)
            + "\" fill=\"rgb(" + m.getRed()
            + "," + m.getGreen()
            + "," + m.getBlue()
            + ")\" visibility=\"visible\" >";
  }

  /**
   * Write an animation formatted in SVG for a rectangle.
   * @param start the start position
   * @param end the end position
   * @param shape the rectangle's name
   * @return the animation line
   */
  private List<String> moveEllipse(Motion start, Motion end, String shape) {
    return moveShape(start, end, shape, "cx", "cy", "rx", "ry", 2);
  }

  /**
   * Write an animation formatted in SVG for an ellipse.
   * @param start the start position
   * @param end the end position
   * @param shape the ellipse's name
   * @return the animation line
   */
  private List<String> moveRect(Motion start, Motion end, String shape) {
    return moveShape(start, end, shape, "x", "y", "width", "height", 1);
  }

  /**
   * Write an SVG-formatted animation for any shape. Don't write animation lines
   * for components (eg width, height, color) that do not change between the start and end.
   * @param start the starting position
   * @param end the ending position
   * @param shape the shape name
   * @param x the x component's name
   * @param y the y component's name
   * @param width the width component's name
   * @param height the height component's name
   * @param widthModifier the number by which to divide the shape's width and height
   * @return the animation line
   */
  private List<String> moveShape(Motion start, Motion end, String shape,
                                 String x, String y, String width, String height,
                                 int widthModifier) {
    ArrayList<String> motions = new ArrayList<>(6);
    // order of inputs: start (ms), duration (ms), attribute type, from, to
    String template = "<animate attributeType=\"xml\" begin=\"%sms\" dur=\"%sms\" "
            + "attributeName=\"%s\" from=\"%s\" to=\"%s\" fill=\"freeze\" />";
    int startTime = (int) Math.round((start.getTime() - 1) * 1000 / speed);
    int duration = (int) Math.round((end.getTime() - start.getTime()) * 1000 / speed);

    if (start.getX() - end.getX() != 0) {
      motions.add(String.format(template, startTime, duration, x,
              start.getX(), end.getX()));
    }
    if (start.getY() - end.getY() != 0) {
      motions.add(String.format(template, startTime, duration, y,
              start.getY(), end.getY()));
    }
    if (start.getWidth() - end.getWidth() != 0) {
      motions.add(String.format(template, startTime, duration,
              width, (start.getWidth() / widthModifier), (end.getWidth() / widthModifier)));
    }
    if (start.getHeight() - end.getHeight() != 0) {
      motions.add(String.format(template, startTime, duration,
              height, (start.getHeight() / widthModifier), (end.getHeight() / widthModifier)));
    }
    if (start.getRed() - end.getRed() != 0
            || start.getGreen() - end.getGreen() != 0
            || start.getBlue() - end.getBlue() != 0) {
      motions.add(String.format(template, startTime, duration, "fill",
              String.format("rgb(%s,%s,%s)", start.getRed(), start.getGreen(), start.getBlue()),
              String.format("rgb(%s,%s,%s)", end.getRed(), end.getGreen(), end.getBlue())));
    }
    if (start.getRotation() - end.getRotation() != 0) {
      motions.add(String.format("<animateTransform attributeType=\"xml\" "
                      + "attributeName=\"transform\" begin=\"%sms\" "
                      + "type=\"rotate\" from=\"%s %s %s\" to=\"%s %s %s\" dur=\"%sms\" />",
              startTime,
              start.getRotation(),
              start.getX() + start.getWidth() - (widthModifier * start.getWidth() / 2),
              start.getY() + start.getHeight() - (widthModifier * start.getHeight() / 2),
              end.getRotation(),
              end.getX() + end.getWidth() - (widthModifier * end.getWidth() / 2),
              end.getY() + end.getHeight() - (widthModifier * end.getHeight() / 2),
              duration));
    }
    return motions;
  }
}
