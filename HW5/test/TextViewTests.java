import org.junit.Before;
import org.junit.Test;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.TextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the class TextView.
 */
public class TextViewTests {
  private AnimationModel model;
  private AnimationView view;
  private StringBuilder output;

  @Before
  public void init() {
    model = new AnimationModelImpl();
    view = new TextView();
    output = new StringBuilder();

    view.setModel(model);
    view.setOutput(output);
  }

  @Test
  public void testSetSpeed() {
    // test that nothing happens
  }

  @Test
  public void testSetOutput() {
    // test that output is set and old output is removed
    StringBuilder output2 = new StringBuilder();
    view.setOutput(output2);
    view.animate();
    assertNotEquals("", output2.toString());
    assertEquals("", output.toString());

    // test that null outputs cause exceptions
    try {
      view.setOutput(null);
      fail("Set output to null");
    } catch (IllegalArgumentException e) {
      assertEquals("Output Appendable must not be null",
              e.getMessage());
    }
  }

  @Test
  public void testSetModel() {
    // test that the correct model is set
    AnimationModel model2 = new AnimationModelImpl();
    model2.addRectangle("R");
    model2.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model2.addMotion("R", 2, 1, 1, 2, 2, 1, 1, 1);

    AnimationView view2 = new TextView();
    StringBuilder output2 = new StringBuilder();
    view2.setModel(model2);
    view2.setOutput(output2);
    view2.animate();

    assertEquals("canvas 0 0 0 0\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 2 1 1 2 2 1 1 1",
            output2.toString());

    // test that a second model cannot be added
    try {
      view2.setModel(model);
      fail("Set a model for a view that already had one");
    } catch (IllegalStateException e) {
      assertEquals("This view already has a model",
              e.getMessage());
    }

    // test that a null model cannot be added
    try {
      new TextView().setModel(null);
      fail("Set a null model");
    } catch (IllegalArgumentException e) {
      assertEquals("Model must not be null",
              e.getMessage());
    }
  }

  @Test
  public void testAnimate() {
    // no shapes
    view.animate();
    assertEquals("canvas 0 0 0 0",
            output.toString());

    // a single rectangle with no movements
    init();
    model.addRectangle("R");
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape R rectangle",
            output.toString());

    // one keyframe for a rectangle
    init();
    model.addRectangle("R");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1",
            output.toString());

    // a single movement for a rectangle
    init();
    model.addRectangle("R");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("R", 2, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 2 1 1 2 2 1 1 1",
            output.toString());

    init();
    // two movements for a rectangle (all parameters change in motion 1)
    model.addRectangle("R");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("R", 3, 2, 2, 2, 2, 5, 5, 5);
    model.addMotion("R", 6, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 3 2 2 2 2 5 5 5\n" +
                    "motion R 3 2 2 2 2 5 5 5 6 1 1 2 2 1 1 1",
            output.toString());

    // a single ellipse with no movements
    init();
    model.addEllipse("E");
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape E ellipse",
            output.toString());

    // one keyframe for a ellipse
    init();
    model.addEllipse("E");
    model.addMotion("E", 1, 1, 1, 1, 1, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape E ellipse\n" +
                    "motion E 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1",
            output.toString());

    // a single movement for a ellipse
    init();
    model.addEllipse("E");
    model.addMotion("E", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("E", 2, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape E ellipse\n" +
                    "motion E 1 1 1 1 1 1 1 1 2 1 1 2 2 1 1 1",
            output.toString());

    // two movements for a ellipse (all parameters change in motion 1)
    init();
    model.addEllipse("E");
    model.addMotion("E", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("E", 3, 2, 2, 2, 2, 5, 5, 5);
    model.addMotion("E", 6, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape E ellipse\n" +
                    "motion E 1 1 1 1 1 1 1 1 3 2 2 2 2 5 5 5\n" +
                    "motion E 3 2 2 2 2 5 5 5 6 1 1 2 2 1 1 1",
            output.toString());

    // a rectangle and an ellipse
    init();
    model.addRectangle("R");
    model.addEllipse("E");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("R", 2, 2, 2, 2, 2, 2, 2, 2);
    model.addMotion("E", 1, 3, 3, 3, 3, 3, 3, 3);
    model.addMotion("E", 4, 4, 4, 4, 4, 4, 4, 4);
    view.animate();
    assertEquals("canvas 0 0 0 0\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2\n" +
                    "shape E ellipse\n" +
                    "motion E 1 3 3 3 3 3 3 3 4 4 4 4 4 4 4 4",
            output.toString());

    // exception if model is null
    AnimationView view2 = new TextView();
    view2.setOutput(output);
    try {
      view2.animate();
      fail("Animated with no model");
    } catch (IllegalStateException e) {
      assertEquals("The model has not been set",
              e.getMessage());
    }

    // exception if output is null
    view2 = new TextView();
    view2.setModel(model);
    try {
      view2.animate();
      fail("Animated with no view");
    } catch (IllegalStateException e) {
      assertEquals("The output Appendable has not been set",
              e.getMessage());
    }

    // test dealing with IOExceptions
    view2 = new TextView();
    view2.setModel(model);
    view2.setOutput(new EvilAppendable());
    try {
      view2.animate();
      fail("Animated to an Appendable that can't be written to");
    } catch (IllegalStateException e) {
      assertEquals("Could not write to the output",
              e.getMessage());
    }
  }
}
