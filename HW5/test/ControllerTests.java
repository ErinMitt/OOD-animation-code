import org.junit.Before;
import org.junit.Test;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.model.AnimationModelImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the class AnimationController.
 */
public class ControllerTests {
  AnimationController controller;
  Appendable modelOutput;
  Appendable viewOutput;
  WritingMockModel mockModel;
  WritingMockView mockView;

  @Before
  public void init() {
    modelOutput = new StringBuilder();
    viewOutput = new StringBuilder();
    mockModel = new WritingMockModel(modelOutput);
    mockView = new WritingMockView(viewOutput);
    controller = new AnimationController(mockModel, mockView);
  }

  /**
   * Clear the output so that new lines can be written.
   */
  private void clear() {
    viewOutput = new StringBuilder();
    modelOutput = new StringBuilder();
    mockView.setMockOutput(viewOutput);
    mockModel.setMockOutput(modelOutput);
  }

  @Test
  public void testConstructor() {
    // test for null
    try {
      new AnimationController(null, new WritingMockView(new StringBuilder()));
      fail("made a controller with a null model");
    } catch (IllegalArgumentException e) {
      assertEquals("Model and view must not be null", e.getMessage());
    }

    try {
      new AnimationController(new AnimationModelImpl(), null);
      fail("made a controller with a null view");
    } catch (IllegalArgumentException e) {
      assertEquals("Model and view must not be null", e.getMessage());
    }

    // test that the right methods were called in the right order
    assertEquals("", modelOutput.toString());
    assertEquals("addFeatures called with " + controller  + "\n"
            + "setModel called with " + mockModel + "\n",
             viewOutput.toString());
  }

  @Test
  public void testGo() {
    clear();
    controller.gogo();
    assertEquals("", modelOutput.toString());
    assertEquals("animate called\n", viewOutput.toString());
  }

  @Test
  public void testSetSpeedToUserInput() {
    clear();
    controller.setSpeedToUserInput("test");
    assertEquals("Error: Speed must be a number\n" +
            "resetFocus called\n", viewOutput.toString());

    clear();
    controller.setSpeedToUserInput("-5");
    assertEquals("setSpeed called with -5.0\n" +
            "resetFocus called\n", viewOutput.toString());

    clear();
    controller.setSpeedToUserInput("");
    assertEquals("Error: Speed must be a number\n" +
            "resetFocus called\n", viewOutput.toString());

    clear();
    controller.setSpeedToUserInput("5");
    assertEquals("setSpeed called with 5.0\n" +
            "resetFocus called\n", viewOutput.toString());
  }

  @Test
  public void testEnterShapeEditor() {
    clear();
    controller.enterShapeEditor("Shape!");
    assertEquals("Entered shape editor: Shape!\n" +
            "paused\n", viewOutput.toString());
  }

  @Test
  public void testExitShapeEditor() {
    clear();
    controller.exitShapeEditor();
    assertEquals("Exited shape editor\n", viewOutput.toString());
  }

  @Test
  public void testAddKeyframe() {
    clear();
    try {
      controller.addKeyframe("shape", "10", null, "", "", "", "", "", "");
      fail("A null input didn't trigger an IAE");
    } catch (IllegalArgumentException e) {
      assertEquals("Inputs must not be null", e.getMessage());
    }

    clear();
    controller.addKeyframe("shape", "hi", "", "", "", "", "", "", "");
    assertEquals("Error: Keyframe parameters must be numbers\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.addKeyframe("shape", "4", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Exited shape editor\n" +
            "updateMaxTick called\n" +
            "drawCurrentTick called\n", viewOutput.toString());
    assertEquals("addMotion called with shape 4 1 1 1 1 1 1 1\n", modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    clear();
    c.addKeyframe("shape", "3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Error: Couldn't add keyframe: No shape with the name shape exists.\n",
            viewOutput.toString());

    clear();
    m.addEllipse("E");
    c.addKeyframe("E", "-3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Error: Couldn't add keyframe: Time must be a positive integer, given -3\n",
            viewOutput.toString());

    clear();
    c.addKeyframe("E", "3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Exited shape editor\n" +
            "updateMaxTick called\n" +
            "drawCurrentTick called\n", viewOutput.toString());
    assertEquals(1, m.getMotions("E").size());

    clear();
    c.addKeyframe("E", "3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Error: Couldn't add keyframe: This shape already has a motion at time 3\n",
            viewOutput.toString());

  }

  @Test
  public void testEditKeyframe() {
    clear();
    try {
      controller.editKeyframe("shape", "10", "", null, "", "", "", "", "");
      fail("A null input didn't trigger an IAE");
    } catch (IllegalArgumentException e) {
      assertEquals("Inputs must not be null", e.getMessage());
    }

    clear();
    controller.editKeyframe("shape", "hi", "", "", "", "", "", "", "");
    assertEquals("Error: Keyframe parameters must be numbers\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.editKeyframe("shape", "4", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Exited shape editor\n" +
            "drawCurrentTick called\n",
            viewOutput.toString());
    assertEquals("editMotion called with shape 4 1 1 1 1 1 1 1\n", modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    clear();
    c.editKeyframe("shape", "3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Error: Couldn't edit keyframe: No shape with the name shape exists.\n",
            viewOutput.toString());

    clear();
    m.addEllipse("E");
    c.editKeyframe("E", "-3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Error: Couldn't edit keyframe: Time must be a positive integer, given -3\n",
            viewOutput.toString());

    clear();
    c.editKeyframe("E", "3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Error: Couldn't edit keyframe: No motion at time 3 for the shape E\n",
            viewOutput.toString());

    m.addMotion("E", 3, 1, 1, 1, 1, 1, 1, 1);
    clear();
    c.editKeyframe("E", "3", "1", "1", "1", "1", "1", "1", "1");
    assertEquals("Exited shape editor\n" +
                    "drawCurrentTick called\n",
            viewOutput.toString());
  }

  @Test
  public void testRemoveKeyframe() {
    clear();
    try {
      controller.removeKeyframe(null, "10");
      fail("A null input didn't trigger an IAE");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape must not be null", e.getMessage());
    }

    clear();
    controller.removeKeyframe("shape", null);
    assertEquals("Error: No keyframe selected\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.removeKeyframe("shape", "time");
    assertEquals("Error: Keyframe parameters must be numbers\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.removeKeyframe("shape", "10");
    assertEquals("Exited shape editor\n" +
            "updateMaxTick called\n" +
            "drawCurrentTick called\n",
            viewOutput.toString());
    assertEquals("deleteMotion called with shape 10\n", modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    clear();
    c.removeKeyframe("shape", "10");
    assertEquals("Error: Couldn't delete keyframe: No shape with the name shape exists.\n",
            viewOutput.toString());

    clear();
    m.addRectangle("R");
    c.removeKeyframe("R", "10");
    assertEquals("Error: Couldn't delete keyframe: No motion at time 10 for the shape R\n",
            viewOutput.toString());

    clear();
    m.addMotion("R", 3, 3, 3, 3, 3, 3, 3, 3);
    c.removeKeyframe("R", "3");
    assertEquals("Exited shape editor\n" +
                    "updateMaxTick called\n" +
                    "drawCurrentTick called\n",
            viewOutput.toString());
  }

  @Test
  public void testSuggestNewKeyframe() {
    try {
      controller.suggestNewKeyframe("shp", null);
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Time must not be null", e.getMessage());
    }

    try {
      controller.suggestNewKeyframe(null, "time");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape must not be null", e.getMessage());
    }

    clear();
    try {
      controller.suggestNewKeyframe("shp", "10");
      fail("Allowed recommending a keyframe for a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No such shape shp", e.getMessage());
      assertEquals("getShapes called\n",
              modelOutput.toString());
    }

    clear();
    controller.suggestNewKeyframe("shape", "ten");
    assertEquals("Error: Tick number must be an integer\n", viewOutput.toString());
    assertEquals("",
            modelOutput.toString());

    clear();
    controller.suggestNewKeyframe("shape", "10");
    assertEquals("setNewFrameText called with motion 10 0 0 10 10 0 0 0\n",
            viewOutput.toString());
    assertEquals("getShapes called\n" +
            "getMotions called with shape\n",
            modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    m.addRectangle("R");
    m.addMotion("R", 10, 10, 10, 10, 10, 10, 10, 10);
    clear();
    c.suggestNewKeyframe("R", "10");
    assertEquals("setNewFrameText called with motion 10 10 10 10 10 10 10 10\n",
            viewOutput.toString());
  }

  @Test
  public void testSuggestEditKeyframe() {
    clear();
    controller.suggestEditKeyframe("shp", null);
    assertEquals("Error: Must select a time to edit keyframes\n", viewOutput.toString());

    try {
      controller.suggestEditKeyframe(null, "time");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape must not be null", e.getMessage());
    }

    clear();
    try {
      controller.suggestEditKeyframe("shp", "10");
      fail("Allowed recommending a keyframe for a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No such shape shp", e.getMessage());
      assertEquals("getShapes called\n",
              modelOutput.toString());
    }

    clear();
    controller.suggestEditKeyframe("shape", "ten");
    assertEquals("Error: Tick number must be an integer\n", viewOutput.toString());
    assertEquals("",
            modelOutput.toString());

    clear();
    controller.suggestEditKeyframe("shape", "10");
    assertEquals("Error: The shape shape has no keyframes at tick 10\n",
            viewOutput.toString());
    assertEquals("getShapes called\n" +
                    "getMotions called with shape\n",
            modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    m.addRectangle("R");
    m.addMotion("R", 10, 10, 10, 10, 10, 10, 10, 10);
    clear();
    c.suggestEditKeyframe("R", "10");
    assertEquals("setEditFrameText called with motion 10 10 10 10 10 10 10 10\n",
            viewOutput.toString());
  }

  @Test
  public void testAddShape() {
    try {
      controller.addShape("shp", null);
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape name and type must not be null", e.getMessage());
    }

    try {
      controller.addShape(null, "ellipse");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape name and type must not be null", e.getMessage());
    }

    clear();
    controller.addShape("", "ellipse");
    assertEquals("Error: Names must have at least one character\n", viewOutput.toString());

    clear();
    controller.addShape("name with spaces", "ellipse");
    assertEquals("Error: Names cannot have spaces\n", viewOutput.toString());

    clear();
    controller.addShape("shape", "ellipse");
    assertEquals("Error: There is already a shape by the name shape\n", viewOutput.toString());

    try {
      controller.addShape("shp", "thing");
      fail("Allowed an invalid shape type");
    } catch (IllegalArgumentException e) {
      assertEquals("There is no shape type thing", e.getMessage());
    }

    clear();
    controller.addShape("shp", "ellipse");
    assertEquals("setShapeList called with [shape]\n", viewOutput.toString());
    assertEquals("getShapes called\n" +
            "addEllipse called with shp\n" +
            "getShapes called\n",
            modelOutput.toString());

    clear();
    controller.addShape("shp", "rectangle");
    assertEquals("setShapeList called with [shape]\n",
            viewOutput.toString());
    assertEquals("getShapes called\n" +
                    "addRectangle called with shp\n" +
                    "getShapes called\n",
            modelOutput.toString());
  }

  @Test
  public void testDeleteShape() {
    clear();
    controller.deleteShape(null);
    assertEquals("Error: There is no shape selected\n", viewOutput.toString());

    clear();
    controller.deleteShape("shp");
    assertEquals("Error: There is no shape by this name\n", viewOutput.toString());

    clear();
    controller.deleteShape("shape");
    assertEquals("getShapes called\n" +
                    "deleteShape called with shape\n" +
                    "getShapes called\n",
            modelOutput.toString());
  }

  @Test
  public void testSave() {
    try {
      controller.save(null, "file");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Arguments must not be null", e.getMessage());
    }

    try {
      controller.save("svg", null);
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Arguments must not be null", e.getMessage());
    }

    clear();
    controller.save("svg", "");
    assertEquals("Error: Output location must be set\n", viewOutput.toString());

    clear();
    controller.save("svg", "file");
    assertEquals("setOutput called\n" +
            "save called\n", viewOutput.toString());

    clear();
    controller.save("text", "file");
    assertEquals("setOutput called\n" +
            "save called\n", viewOutput.toString());

    try {
      controller.save("none", "file");
      fail("saved with illegal file type");
    } catch (IllegalArgumentException e) {
      assertEquals("Unsupported file type none", e.getMessage());
    }
  }

  @Test
  public void testLoad() {
    try {
      controller.load(null);
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Arguments must not be null", e.getMessage());
    }

    clear();
    controller.load("");
    assertEquals("Error: Unable to locate file.\n", viewOutput.toString());
  }
}
