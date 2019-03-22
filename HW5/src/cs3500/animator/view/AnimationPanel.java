package cs3500.animator.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

class AnimationPanel extends JPanel {
  private final ReadOnlyModel model;
  private int tick; // the moment in time of the model that the panel displays

  /**
   * Build a JPanel that draws the current state of an animation model.
   * @param model the model that is drawn
   */
  public AnimationPanel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    this.model = model;
    setPreferredSize(new Dimension(model.getWidth() + model.getX(),
            model.getHeight() + model.getY()));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    AffineTransform originalTransform = g2.getTransform();
    g2.translate(model.getX(), model.getY());

    // draw all the shapes
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

  /**
   * Draw the scene of the animation described by the model at the given tick.
   * @param tick the point in time to draw.
   */
  public void paintTick(int tick) {
    this.tick = tick;
    repaint();
  }
}
