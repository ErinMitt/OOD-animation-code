package cs3500.animator.view;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

/**
 * A class representing an animation view that displays the animation detailed in
 * an AnimationModel on screen.
 */
public class VisualView  extends JFrame implements AnimationView {
  private AnimationPanel animationPanel;
  private double speed = 1;
  private int tick;

  /**
   * Create a visual view.
   */
  public VisualView() {
    super();
    this.setTitle("Animation");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.tick = Motion.START_TICK;
  }

  @Override
  public void setModel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    if (animationPanel != null) {
      throw new IllegalStateException("This view already has a model");
    }
    this.animationPanel = new AnimationPanel(model);
    this.add(new JScrollPane(animationPanel));
    this.pack();
  }

  @Override
  public void setSpeed(double speed) {
    if (speed <= 0) {
      throw new IllegalArgumentException("Number of ticks per second must be a positive number, "
              + "given " + speed);
    }
    this.speed = speed;
  }

  /**
   * This method is unsupported in VisualView. Do nothing.
   * @param output the Appendable to which output is written.
   * @throws UnsupportedOperationException if called
   */
  @Override
  public void setOutput(Appendable output) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Visual view has no output");
  }

  /**
   * Displays a non-looping visual representation of the model animation on screen.
   * Requires setModel to be called beforehand.
   */
  @Override
  public void animate() {
    if (animationPanel == null) {
      throw new IllegalStateException("There is no model to animate");
    }
    Timer timer = new Timer((int) Math.round((1000 / speed)), (ActionEvent e) -> {
      updateDisplay();
    });
    this.setVisible(true);
    timer.start();
  }

  /**
   * Update the animation to the next tick and refresh the screen.
   */
  private void updateDisplay() {
    animationPanel.paintTick(tick);
    tick += 1;
  }
}
