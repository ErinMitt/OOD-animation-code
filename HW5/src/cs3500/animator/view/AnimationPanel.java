package cs3500.animator.view;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

public class AnimationPanel extends JPanel {
  private final ReadOnlyModel model;
  private int tick;

  /**
   * Build a JPanel that draws the current state of an animation model.
   * @param model the model that is drawn
   */
  public AnimationPanel(ReadOnlyModel model) {
    this.model = model;
    this.tick = 0;
    setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
  }

  /**
   * Add one to the current time.
   */
  public void incrementTick() {
    tick += 1;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    AffineTransform originalTransform = g2.getTransform();
    g2.translate(model.getX(), model.getY());

    // fill in animation stuff here
    for (String shape : model.getShapes()) {
      try {
        Motion state = model.getTransformationAt(shape, tick).getStateAt(tick);
        g2.setColor(new Color(state.getRed(), state.getGreen(), state.getBlue()));
        switch (model.getShapeType(shape)) {
          case "ellipse":
            g2.fillOval(state.getX(), state.getY(), state.getWidth(), state.getHeight());
            break;
          case "rectangle":
            g2.fillRect(state.getX(), state.getY(), state.getWidth(), state.getHeight());
            break;
          default:
            throw new IllegalStateException("Invalid shape type");
        }
      } catch (IllegalArgumentException e) {
        // if the shape has no motion at the current tick, do not draw it.
      }
    }

    g2.setTransform(originalTransform);
  }
}
