import org.junit.Before;
import org.junit.Test;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.view.EditorAnimationView;
import cs3500.animator.view.EditorView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the class EditorView.
 */
public class EditorViewTests {
  EditorAnimationView original;
  Appendable controllerOutput;
  TestingController controller;

  @Before
  public void init() {
    original = new EditorView();
    controllerOutput = new StringBuilder();
    controller = new TestingController();
  }

  @Test
  public void testAddFeatures() {
    try {
      original.addFeatures(null);
      fail("Added a null Features to the view");
    } catch (IllegalArgumentException e) {
      assertEquals("Features must not be null", e.getMessage());
    }
  }

  /*  @Override
    public void setModel(ReadOnlyModel model) {
      if (model == null) {
        throw new IllegalArgumentException("Model must not be null");
      }
      if (animationPanel != null) {
        throw new IllegalStateException("This view already has a model");
      }
      this.animationPanel = new AnimationPanel(model);
      add(animationPanel, BorderLayout.CENTER);
      updateMaxTick();
      drawCurrentTick();
      editFactory.setModel(model);

      setShapeList(model.getShapes());
    }*/

  @Test (expected = IllegalArgumentException.class)
  public void testSetModelNull() {
    original.setModel(null);
  }

  @Test (expected = IllegalStateException.class)
  public void testSetModelExists() {
    original.addFeatures(controller);
    original.setModel(new AnimationModelImpl());
    original.setModel(new AnimationModelImpl());
  }

  @Test
  public void testAnimate() {
    try {
      original.animate();
      fail("animated without a model");
    } catch (IllegalStateException e) {
      assertEquals("The model has not been set", e.getMessage());
    }
  }

  @Test
  public void testSetSpeed() {
    try {
      original.setSpeed(-3);
      fail("Set speed to invalid number");
    } catch (IllegalArgumentException e) {
      assertEquals("Speed must be positive", e.getMessage());
    }

    try {
      original.setSpeed(0);
      fail("Set speed to invalid number");
    } catch (IllegalArgumentException e) {
      assertEquals("Speed must be positive", e.getMessage());
    }

    try {
      original.setSpeed(3);
    } catch (IllegalArgumentException e) {
      fail("forbade a legal speed");
    }
  }

  @Test
  public void testSave() {
    StringBuilder output = new StringBuilder();
    original.setOutput(output);
    original.save("something");
    assertEquals("something", output.toString());

    init();
    original.setOutput(new EvilAppendable());
    try {
      original.save("text");
      fail("Failed to signal unwritable Appendable");
    } catch (IllegalStateException e) {
      assertEquals("Could not write to the output", e.getMessage());
    }
  }

  @Test
  public void testSetOutput() {
    try {
      original.setOutput(null);
      fail("Set output to null");
    } catch (IllegalArgumentException e) {
      assertEquals("Output must not be null", e.getMessage());
    }
  }
}