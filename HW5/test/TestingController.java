import cs3500.animator.controller.Controller;
import cs3500.animator.controller.Features;

/**
 * Represents a simplified Controller used for testing. Methods do not do anything.
 */
public class TestingController implements Features, Controller {

  @Override
  public void gogo() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void togglePlay() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void toggleLoop() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void rewind() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void stepForward() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void stepBackwards() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void setSpeedToUserInput(String input) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void resetTextFields() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void enterShapeEditor(String shape) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void exitShapeEditor() {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void addKeyframe(String shape, String time, String x, String y,
                          String width, String height, String red, String green, String blue) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void editKeyframe(String shape, String time, String x, String y,
                           String width, String height, String red, String green, String blue) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void removeKeyframe(String shape, String time) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void suggestNewKeyframe(String shape, String time) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void suggestEditKeyframe(String shape, String time) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void addShape(String name, String type) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void deleteShape(String name) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void save(String type, String fileName) {
    //this controller exists for testing only and doesn't do anything
  }

  @Override
  public void load(String fileName) {
    //this controller exists for testing only and doesn't do anything
  }
}
