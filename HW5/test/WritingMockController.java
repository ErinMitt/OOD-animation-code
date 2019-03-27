import java.io.IOException;

import cs3500.animator.controller.Controller;
import cs3500.animator.controller.Features;

public class WritingMockController implements Features, Controller {
  private Appendable output;

  public WritingMockController(Appendable output) {
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
  public void go() {
    append("go called");
  }

  @Override
  public void togglePlay() {
    append("togglePlay called");
  }

  @Override
  public void toggleLoop() {

  }

  @Override
  public void rewind() {

  }

  @Override
  public void stepForward() {

  }

  @Override
  public void stepBackwards() {

  }

  @Override
  public void setSpeedToUserInput(String input) {

  }

  @Override
  public void resetTextFields() {

  }

  @Override
  public void enterShapeEditor(String shape) {

  }

  @Override
  public void exitShapeEditor() {

  }

  @Override
  public void addKeyframe(String shape, String time, String x, String y, String width, String height, String red, String green, String blue) {

  }

  @Override
  public void editKeyframe(String shape, String time, String x, String y,
                           String width, String height, String red, String green, String blue) {

  }

  @Override
  public void removeKeyframe(String shape, String time) {

  }

  @Override
  public void suggestNewKeyframe(String shape, String time) {

  }

  @Override
  public void suggestEditKeyframe(String shape, String time) {

  }

  @Override
  public void addShape(String name, String type) {

  }

  @Override
  public void deleteShape(String name) {

  }

  @Override
  public void save(String type, String fileName) {

  }

  /**
   * Append the given message onto the output appendable
   * @param message
   */
  private void append(String message) {
    try {
      this.output.append(message + "\n");
    } catch (IOException e) {
      // don't do anything, this is a mock
    }
  }
}
