package cs3500.animator.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

public class SVGView implements AnimationView {
  private ReadOnlyModel model;
  private Appendable output;
  private double speed = 1;

  /**
   * Set the speed of the animation in ticks per second.
   * @param speed ticks per second
   */
  public void setSpeed(double speed) {
    if (speed <= 0) {
      throw new IllegalArgumentException("Number of ticks per second must be a positive number, "
              + "given " + speed);
    }
    this.speed = speed;
  }

  /**
   * Set the
   * @param output
   */
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
    try {
      output.append(formatAnimation());
    } catch (IOException e) {
      throw new IllegalStateException("Could not write to the output");
    }
  }

  private String formatAnimation() {
    LinkedList<String> SVGLines = new LinkedList<>();
    SVGLines.add("<svg width=\"" + (model.getWidth() + model.getX())
            + "\" height=\"" + (model.getHeight() + model.getY())
            + "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
    for (String shape : model.getShapes()) {
      List<Motion> motions = model.getMotions(shape);
      switch (model.getShapeType(shape)) {
        case "ellipse":
          if (!motions.isEmpty()) {
            SVGLines.add(initEllipse(motions.get(0), shape));
            for (int i = 0; i < motions.size() - 1; i++) {
              SVGLines.addAll(moveEllipse(motions.get(i), motions.get(i + 1), shape));
            }
            SVGLines.add("</ellipse>");
          }
          break;
        case "rectangle":
          if (!motions.isEmpty()) {
            SVGLines.add(initRect(motions.get(0), shape));
            for (int i = 0; i < motions.size() - 1; i++) {
              SVGLines.addAll(moveRect(motions.get(i), motions.get(i + 1), shape));
            }
            SVGLines.add("</rect>");
          }
          break;
        default:
          throw new IllegalArgumentException("Invalid shape type");
      }
    }
    SVGLines.add("</svg>");
    return String.join("\n", SVGLines);
  }

  private String initRect(Motion m, String shape) {
    return "<rect id=\"" + shape
            + "\" x=\"" + (m.getX() + model.getX())
            + "\" y=\"" + (m.getY() + model.getY())
            + "\" width=\"" + m.getWidth()
            + "\" height=\"" + m.getHeight()
            + "\" fill=\"rgb(" + m.getRed()
            + "," + m.getRed()
            + "," + m.getRed()
            + ")\" visibility=\"visible\" >";
  }

  private String initEllipse(Motion m, String shape) {
    return "<ellipse id=\"" + shape
            + "\" cx=\"" + (m.getX() + model.getX())
            + "\" cy=\"" + (m.getY() + model.getY())
            + "\" rx=\"" + m.getWidth()
            + "\" ry=\"" + m.getHeight()
            + "\" fill=\"rgb(" + m.getRed()
            + "," + m.getRed()
            + "," + m.getRed()
            + ")\" visibility=\"visible\" >";
  }

  private List<String> moveEllipse(Motion start, Motion end, String shape) {
    return moveShape(start, end, shape, "cx", "cy", "rx", "ry");
  }

  private List<String> moveRect(Motion start, Motion end, String shape) {
    return moveShape(start, end, shape, "x", "y", "width", "height");
  }

  private List<String> moveShape(Motion start, Motion end, String shape,
                                 String x, String y, String width, String height) {
    ArrayList<String> motions = new ArrayList<>(5);
    // order of inputs: start (ms), duration (ms), attribute type, from, to
    String template = "<animate attributeType=\"xml\" begin=\"%sms\" dur=\"%sms\" "
            + "attributeName=\"%s\" from=\"%s\" to=\"%s\" />";
    int startTime = (int) Math.round((start.getTime() - 1) * 1000 / speed);
    int duration = (int) Math.round((end.getTime() - start.getTime()) * 1000 / speed);

    if (start.getX() - end.getX() != 0) {
      motions.add(String.format(template, startTime, duration, x,
              start.getX() + model.getX(), end.getX() + model.getX()));
    }
    if (start.getY() - end.getY() != 0) {
      motions.add(String.format(template, startTime, duration, y,
              start.getY() + model.getY(), end.getY() + model.getY()));
    }
    if (start.getWidth() - end.getWidth() != 0) {
      motions.add(String.format(template, startTime, duration,
              width, start.getWidth(), end.getWidth()));
    }
    if (start.getHeight() - end.getHeight() != 0) {
      motions.add(String.format(template, startTime, duration,
              height, start.getHeight(), end.getHeight()));
    }
    if (start.getRed() - end.getRed() != 0
            || start.getGreen() - end.getGreen() != 0
            || start.getBlue() - end.getBlue() != 0) {
      motions.add(String.format(template, startTime, duration, "fill",
              String.format("rgb(%s,%s,%s)", start.getRed(), start.getGreen(), start.getBlue()),
              String.format("rgb(%s,%s,%s)", end.getRed(), end.getGreen(), end.getBlue())));
    }
    return motions;
  }
}
