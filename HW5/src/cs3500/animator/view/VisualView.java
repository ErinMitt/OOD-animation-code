package cs3500.animator.view;

import javax.swing.JFrame;

import cs3500.animator.model.ReadOnlyModel;

public class VisualView  extends JFrame implements AnimationView {
  private final AnimationPanel animationPanel;

  public VisualView(ReadOnlyModel model) {
    this.animationPanel = new AnimationPanel(model);
  }

  @Override
  public void animate() {

  }

  /**
   * Update the animation to the next tick.
   */
  private void tick() {
    animationPanel.incrementTick();
    animationPanel.repaint();
  }
}
