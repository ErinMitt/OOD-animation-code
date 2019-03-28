import java.io.IOException;

import cs3500.animator.controller.Controller;
import cs3500.animator.controller.Features;

/**
 * Represents a simplified Controller used for testing. Methods do not do anything.
 */
public class TestingController implements Features, Controller {

  @Override
  public void go() {

  }

  @Override
  public void togglePlay() {

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
  public void editKeyframe(String shape, String time, String x, String y, String width, String height, String red, String green, String blue) {

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

  @Override
  public void load(String fileName) {

  }
}
