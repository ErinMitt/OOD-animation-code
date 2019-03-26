import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.model.Motion;
import cs3500.animator.model.Transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the class AnimationController.
 */
public class ControllerTests {

  @Test
  public void testConstructor() {

  }

  @Test
  public void testGo() {

  }

  @Test
  public void testSetSpeedToUserInput() {
    // use mocks, test error messages
  }

  @Test
  public void testEnterShapeEditor() {

  }

  @Test
  public void testExitShapeEditor() {

  }

  @Test
  public void testAddKeyframe() {

  }

  @Test
  public void testEditKeyframe() {

  }

  @Test
  public void testRemoveKeyframe() {

  }

  @Test
  public void testSuggestNewKeyframe() {

  }

  @Test
  public void testSuggestEditKeyframe() {

  }

  @Test
  public void testAddShape() {

  }

  @Test
  public void testDeleteShape() {

  }
}

class ViewMock implements AnimationModel {
  private Appendable output;

  public ViewMock(Appendable output) {
    this.output = output;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    append("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height + "\n");
  }

  @Override
  public void addEllipse(String name) {

  }

  @Override
  public void addRectangle(String name) {

  }

  @Override
  public void deleteShape(String shapeName) {

  }

  @Override
  public void addMotion(String shapeName, int time, int x, int y, int width, int height, int red, int green, int blue) {

  }

  @Override
  public void extend(String shape, int time) {

  }

  @Override
  public void editMotion(String shape, int time, int x, int y, int width, int height, int red, int green, int blue) {

  }

  @Override
  public void deleteMotion(String shape, int time) {

  }

  @Override
  public void deleteLastMotion(String shapeName) {

  }

  @Override
  public List<String> getShapes() {
    return null;
  }

  @Override
  public String displayAnimation() {
    return null;
  }

  @Override
  public int getX() {
    return 0;
  }

  @Override
  public int getY() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public Transformation getTransformationAt(String shapeName, int tick) {
    return null;
  }

  @Override
  public List<Motion> getMotions(String shapeName) {
    return null;
  }

  @Override
  public String getShapeType(String shapeName) {
    return null;
  }

  private void append(String message) {
    try {
      this.output.append(message);
    } catch (IOException e) {
      // don't do anything, this is a mock
    }
  }
}
