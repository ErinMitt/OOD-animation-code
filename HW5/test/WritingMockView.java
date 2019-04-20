import java.io.IOException;
import java.util.List;

import cs3500.animator.controller.Features;
import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;
import cs3500.animator.view.EditorAnimationView;

/**
 * A class for testing whether an EditorAnimationView's methods were called
 * and recording their inputs.
 */
public class WritingMockView implements EditorAnimationView {
  private Appendable output;

  public WritingMockView(Appendable output) {
    this.output = output;
  }

  /**
   * Set a new Appendable as the output for the mock.
   * @param output the new output location
   */
  public void setMockOutput(Appendable output) {
    this.output = output;
  }

  @Override
  public void drawCurrentTick() {
    append("drawCurrentTick called");
  }

  @Override
  public void togglePlay() {
    append("togglePlay called");
  }

  @Override
  public void pause() {
    append("paused");
  }

  @Override
  public void setTick(int tick) {
    append("setTick called with " + tick);
  }

  @Override
  public void rewind() {
    append("rewind called");
  }

  @Override
  public void incrementTick() {
    append("incrementTick called");
  }

  @Override
  public void decrementTick() {
    append("decrementTick called");
  }

  @Override
  public void toggleLoop() {
    append("toggleLoop called");
  }

  @Override
  public void updateMaxTick() {
    append("updateMaxTick called");
  }

  @Override
  public void setShapeList(List<String> shapes) {
    append("setShapeList called with " + shapes);
  }

  @Override
  public void save(String text) {
    append("save called");
  }

  @Override
  public void addFeatures(Features features) {
    append("addFeatures called with " + features);
  }

  @Override
  public void displayErrorMessage(String message) {
    append("Error: " + message);
  }

  @Override
  public void resetFocus() {
    append("resetFocus called");
  }

  @Override
  public void resetTextFields() {
    append("resetFocus called");
  }

  @Override
  public void enterShapeEditor(String shape) {
    append("Entered shape editor: " + shape);
  }

  @Override
  public void exitShapeEditor() {
    append("Exited shape editor");
  }

  @Override
  public void setNewFrameText(Motion m) {
    append("setNewFrameText called with motion " + m.display());
  }

  @Override
  public void setEditFrameText(Motion m) {
    append("setEditFrameText called with motion " + m.display());
  }

  @Override
  public void setModel(ReadOnlyModel model) {
    append("setModel called with " + model);
  }

  @Override
  public void animate() {
    append("animate called");
  }

  @Override
  public void setSpeed(double speed) {
    append("setSpeed called with " + speed);
  }

  @Override
  public void setOutput(Appendable output) {
    append("setOutput called");
  }

  /**
   * Append the given message onto the output appendable.
   * @param message the message to be written
   */
  private void append(String message) {
    try {
      this.output.append(message + "\n");
    } catch (IOException e) {
      // don't do anything, this is a mock
    }
  }
}
