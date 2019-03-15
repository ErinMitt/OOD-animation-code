package cs3500.animator.view;

import javax.swing.JFrame;

import cs3500.animator.model.ReadOnlyModel;

public class VisualView  extends JFrame implements AnimationView {
  private AnimationPanel animationPanel;

  @Override
  public void setModel(ReadOnlyModel model) {
    this.animationPanel = new AnimationPanel(model);
  }

  @Override
  public void animate() {
    if (animationPanel == null) {
      throw new IllegalStateException("There is no model to animate");
    }

  }

  /**
   * Update the animation to the next tick.
   */
  private void tick() {
    animationPanel.incrementTick();
    animationPanel.repaint();
  }
}
