package cs3500.animator.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import cs3500.animator.model.ReadOnlyModel;

public class AnimationPanel extends JPanel {
  private final ReadOnlyModel model;
  private int tick;

  /**
   * Build a model
   * @param model
   */
  public AnimationPanel(ReadOnlyModel model) {
    this.model = model;
    this.tick = 0;
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
  }
}
