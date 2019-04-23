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
    controller.enterShapeEditor("layer", "Shape!");
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
      controller.addKeyframe("layer", "shape", "10", null, "", "", "", "", "", "", "");
      fail("A null input didn't trigger an IAE");
    } catch (IllegalArgumentException e) {
      assertEquals("Inputs must not be null", e.getMessage());
    }

    clear();
    controller.addKeyframe("layer", "shape", "hi", "", "", "", "", "", "", "", "");
    assertEquals("Error: Keyframe parameters must be numbers\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.addKeyframe("layer", "shape", "4", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Exited shape editor\n" +
            "updateMaxTick called\n" +
            "drawCurrentTick called\n", viewOutput.toString());
    assertEquals("addMotion called with shape 4 1 1 1 1 1 1 1 0\n", modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    clear();
    c.addKeyframe("layer", "shape", "3", "1", "1", "1", "1", "1", "1", "1", "0");
      assertEquals("Error: Couldn't add keyframe: There is no layer named layer\n",
              viewOutput.toString());

    clear();
    m.addEllipse("layer", "E");
    c.addKeyframe("layer", "E", "-3", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Error: Couldn't add keyframe: Time must be a positive integer, given -3\n",
            viewOutput.toString());

    clear();
    c.addKeyframe("layer", "E", "3", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Exited shape editor\n" +
            "updateMaxTick called\n" +
            "drawCurrentTick called\n", viewOutput.toString());
    assertEquals(1, m.getMotions("layer", "E").size());

    clear();
    c.addKeyframe("layer", "E", "3", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Error: Couldn't add keyframe: This shape already has a motion at time 3\n",
            viewOutput.toString());

  }

  @Test
  public void testEditKeyframe() {
    clear();
    try {
      controller.editKeyframe("layer", "shape", "10", "", null, "", "", "", "", "", "");
      fail("A null input didn't trigger an IAE");
    } catch (IllegalArgumentException e) {
      assertEquals("Inputs must not be null", e.getMessage());
    }

    clear();
    controller.editKeyframe("layer", "shape", "hi", "", "", "", "", "", "", "", "");
    assertEquals("Error: Keyframe parameters must be numbers\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.editKeyframe("layer", "shape", "4", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Exited shape editor\n" +
            "drawCurrentTick called\n",
            viewOutput.toString());
    assertEquals("editMotion called with shape 4 1 1 1 1 1 1 1 0\n", modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    clear();
    c.editKeyframe("layer", "shape", "3", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Error: Couldn't edit keyframe: No shape with the name shape exists.\n",
            viewOutput.toString());

    clear();
    m.addEllipse("layer", "E");
    c.editKeyframe("layer", "E", "-3", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Error: Couldn't edit keyframe: Time must be a positive integer, given -3\n",
            viewOutput.toString());

    clear();
    c.editKeyframe("layer", "E", "3", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Error: Couldn't edit keyframe: No motion at time 3 for the shape E\n",
            viewOutput.toString());

    m.addMotion("layer", "E", 3, 1, 1, 1, 1, 1, 1, 1);
    clear();
    c.editKeyframe("layer", "E", "3", "1", "1", "1", "1", "1", "1", "1", "0");
    assertEquals("Exited shape editor\n" +
                    "drawCurrentTick called\n",
            viewOutput.toString());
  }

  @Test
  public void testRemoveKeyframe() {
    clear();
    try {
      controller.removeKeyframe("layer", null, "10");
      fail("A null input didn't trigger an IAE");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape must not be null", e.getMessage());
    }

    clear();
    controller.removeKeyframe("layer", "shape", null);
    assertEquals("Error: No keyframe selected\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.removeKeyframe("layer", "shape", "time");
    assertEquals("Error: Keyframe parameters must be numbers\n", viewOutput.toString());
    assertEquals("", modelOutput.toString());

    clear();
    controller.removeKeyframe("layer", "shape", "10");
    assertEquals("Exited shape editor\n" +
            "updateMaxTick called\n" +
            "drawCurrentTick called\n",
            viewOutput.toString());
    assertEquals("deleteMotion called with shape 10\n", modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    clear();
    c.removeKeyframe("layer", "shape", "10");
    assertEquals("Error: Couldn't delete keyframe: No shape with the name shape exists.\n",
            viewOutput.toString());

    clear();
    m.addRectangle("layer", "R");
    c.removeKeyframe("layer", "R", "10");
    assertEquals("Error: Couldn't delete keyframe: No motion at time 10 for the shape R\n",
            viewOutput.toString());

    clear();
    m.addMotion("layer", "R", 3, 3, 3, 3, 3, 3, 3, 3);
    c.removeKeyframe("layer", "R", "3");
    assertEquals("Exited shape editor\n" +
                    "updateMaxTick called\n" +
                    "drawCurrentTick called\n",
            viewOutput.toString());
  }

  @Test
  public void testSuggestNewKeyframe() {
    try {
      controller.suggestNewKeyframe("layer", "shp", null);
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Time must not be null", e.getMessage());
    }

    try {
      controller.suggestNewKeyframe("layer", null, "time");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape must not be null", e.getMessage());
    }

    clear();
    try {
      controller.suggestNewKeyframe("layer", "shp", "10");
      fail("Allowed recommending a keyframe for a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No such shape shp", e.getMessage());
      assertEquals("getShapes called\n",
              modelOutput.toString());
    }

    clear();
    controller.suggestNewKeyframe("layer", "shape", "ten");
    assertEquals("Error: Tick number must be an integer\n", viewOutput.toString());
    assertEquals("",
            modelOutput.toString());

    clear();
    controller.suggestNewKeyframe("layer", "shape", "10");
    assertEquals("setNewFrameText called with motion 10 0 0 10 10 0 0 0\n",
            viewOutput.toString());
    assertEquals("getShapes called\n" +
            "getMotions called with shape\n",
            modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    m.addRectangle("layer", "R");
    m.addMotion("layer", "R", 10, 10, 10, 10, 10, 10, 10, 10);
    clear();
    c.suggestNewKeyframe("layer", "R", "10");
    assertEquals("setNewFrameText called with motion 10 10 10 10 10 10 10 10\n",
            viewOutput.toString());
  }

  @Test
  public void testSuggestEditKeyframe() {
    clear();
    controller.suggestEditKeyframe("layer", "shp", null);
    assertEquals("Error: Must select a time to edit keyframes\n", viewOutput.toString());

    try {
      controller.suggestEditKeyframe("layer", null, "time");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape must not be null", e.getMessage());
    }

    clear();
    try {
      controller.suggestEditKeyframe("layer", "shp", "10");
      fail("Allowed recommending a keyframe for a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No such shape shp", e.getMessage());
      assertEquals("getShapes called\n",
              modelOutput.toString());
    }

    clear();
    controller.suggestEditKeyframe("layer", "shape", "ten");
    assertEquals("Error: Tick number must be an integer\n", viewOutput.toString());
    assertEquals("",
            modelOutput.toString());

    clear();
    controller.suggestEditKeyframe("layer", "shape", "10");
    assertEquals("Error: The shape shape has no keyframes at tick 10\n",
            viewOutput.toString());
    assertEquals("getShapes called\n" +
                    "getMotions called with shape\n",
            modelOutput.toString());

    AnimationModelImpl m = new AnimationModelImpl();
    AnimationController c = new AnimationController(m, mockView);
    m.addRectangle("layer", "R");
    m.addMotion("layer", "R", 10, 10, 10, 10, 10, 10, 10, 10);
    clear();
    c.suggestEditKeyframe("layer", "R", "10");
    assertEquals("setEditFrameText called with motion 10 10 10 10 10 10 10 10\n",
            viewOutput.toString());
  }

  @Test
  public void testAddShape() {
    try {
      controller.addShape("layer", "shp", null);
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape name and type must not be null", e.getMessage());
    }

    try {
      controller.addShape("layer", null, "ellipse");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Shape name and type must not be null", e.getMessage());
    }

    clear();
    controller.addShape("layer", "", "ellipse");
    assertEquals("Error: Names must have at least one character\n", viewOutput.toString());

    clear();
    controller.addShape("layer", "name with spaces", "ellipse");
    assertEquals("Error: Names cannot have spaces\n", viewOutput.toString());

    clear();
    controller.addShape("layer", "shape", "ellipse");
    assertEquals("Error: There is already a shape by the name shape\n", viewOutput.toString());

    try {
      controller.addShape("layer", "shp", "thing");
      fail("Allowed an invalid shape type");
    } catch (IllegalArgumentException e) {
      assertEquals("There is no shape type thing", e.getMessage());
    }

    clear();
    controller.addShape("layer", "shp", "ellipse");
    assertEquals("setShapeList called with [shape]\n", viewOutput.toString());
    assertEquals("getShapes called\n" +
            "addEllipse called with shp\n" +
            "getShapes called\n",
            modelOutput.toString());

    clear();
    controller.addShape("layer", "shp", "rectangle");
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
    controller.deleteShape("layer", null);
    assertEquals("Error: There is no shape selected\n", viewOutput.toString());

    clear();
    controller.deleteShape("layer", "shp");
    assertEquals("Error: There is no shape by this name\n", viewOutput.toString());

    clear();
    controller.deleteShape("layer", "shape");
    assertEquals("getShapes called\n" +
                    "deleteShape called with shape\n" +
                    "getShapes called\n",
            modelOutput.toString());
  }

  @Test
  public void testSave() {
    try {
      controller.save(1, null, "file");
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Arguments must not be null", e.getMessage());
    }

    try {
      controller.save(1, "svg", null);
      fail("Allowed null inputs");
    } catch (IllegalArgumentException e) {
      assertEquals("Arguments must not be null", e.getMessage());
    }

    clear();
    controller.save(1, "svg", "");
    assertEquals("Error: Output location must be set\n", viewOutput.toString());

    clear();
    controller.save(1, "svg", "file");
    assertEquals("setOutput called\n" +
            "save called\n", viewOutput.toString());

    clear();
    controller.save(1, "text", "file");
    assertEquals("setOutput called\n" +
            "save called\n", viewOutput.toString());

    try {
      controller.save(1, "none", "file");
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
