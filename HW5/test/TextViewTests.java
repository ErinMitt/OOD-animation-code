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
    model.addLayer("1");
    view = new TextView();
    output = new StringBuilder();

    view.setModel(model);
    view.setOutput(output);
  }

  @Test
  public void testSetSpeed() {
    try {
      view.setSpeed(1);
      fail("Set speed for a text view");
    } catch (UnsupportedOperationException e) {
      assertEquals("TextView has no speed", e.getMessage());
    }
    view.animate();
    assertEquals("canvas 0 0 1 1", output.toString());
  }

  @Test
  public void testSetOutput() {
    // test that modelOutput is set and old modelOutput is removed
    StringBuilder output2 = new StringBuilder();
    view.setOutput(output2);
    view.animate();
    assertNotEquals("", output2.toString());
    assertEquals("", output.toString());

    // test that null outputs cause exceptions
    try {
      view.setOutput(null);
      fail("Set modelOutput to null");
    } catch (IllegalArgumentException e) {
      assertEquals("Output Appendable must not be null",
              e.getMessage());
    }
  }

  @Test
  public void testSetModel() {
    // test that the correct model is set
    AnimationModel model2 = new AnimationModelImpl();
    model2.addLayer("1");
    model2.addRectangle("1", "R");
    model2.addMotion("1", "R", 1, 1, 1, 1, 1, 1, 1, 1);
    model2.addMotion("1", "R", 2, 1, 1, 2, 2, 1, 1, 1);

    AnimationView view2 = new TextView();
    StringBuilder output2 = new StringBuilder();
    view2.setModel(model2);
    view2.setOutput(output2);
    view2.animate();

    assertEquals("canvas 0 0 1 1\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1\n" +
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
    assertEquals("canvas 0 0 1 1",
            output.toString());

    // a single rectangle with no movements
    init();
    model.addRectangle("1", "R");
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape R rectangle",
            output.toString());

    // one keyframe for a rectangle
    init();
    model.addRectangle("1", "R");
    model.addMotion("1", "R", 1, 1, 1, 1, 1, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1",
            output.toString());

    // a single movement for a rectangle
    init();
    model.addRectangle("1", "R");
    model.addMotion("1", "R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("1", "R", 2, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1\n" +
                    "motion R 1 1 1 1 1 1 1 1 2 1 1 2 2 1 1 1",
            output.toString());

    init();
    // two movements for a rectangle (all parameters change in motion 1)
    model.addRectangle("1", "R");
    model.addMotion("1", "R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("1", "R", 3, 2, 2, 2, 2, 5, 5, 5);
    model.addMotion("1", "R", 6, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1\n" +
                    "motion R 1 1 1 1 1 1 1 1 3 2 2 2 2 5 5 5\n" +
                    "motion R 3 2 2 2 2 5 5 5 6 1 1 2 2 1 1 1",
            output.toString());

    // a single ellipse with no movements
    init();
    model.addEllipse("1", "E");
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape E ellipse",
            output.toString());

    // one keyframe for a ellipse
    init();
    model.addEllipse("1", "E");
    model.addMotion("1", "E", 1, 1, 1, 1, 1, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape E ellipse\n" +
                    "motion E 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1",
            output.toString());

    // a single movement for a ellipse
    init();
    model.addEllipse("1", "E");
    model.addMotion("1", "E", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("1", "E", 2, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape E ellipse\n" +
                    "motion E 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1\n" +
                    "motion E 1 1 1 1 1 1 1 1 2 1 1 2 2 1 1 1",
            output.toString());

    // two movements for a ellipse (all parameters change in motion 1)
    init();
    model.addEllipse("1", "E");
    model.addMotion("1", "E", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("1", "E", 3, 2, 2, 2, 2, 5, 5, 5);
    model.addMotion("1", "E", 6, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape E ellipse\n" +
                    "motion E 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1\n" +
                    "motion E 1 1 1 1 1 1 1 1 3 2 2 2 2 5 5 5\n" +
                    "motion E 3 2 2 2 2 5 5 5 6 1 1 2 2 1 1 1",
            output.toString());

    // a rectangle and an ellipse
    init();
    model.addRectangle("1", "R");
    model.addEllipse("1", "E");
    model.addMotion("1", "R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("1", "R", 2, 2, 2, 2, 2, 2, 2, 2);
    model.addMotion("1", "E", 1, 3, 3, 3, 3, 3, 3, 3);
    model.addMotion("1", "E", 4, 4, 4, 4, 4, 4, 4, 4);
    view.animate();
    assertEquals("canvas 0 0 1 1\n" +
                    "shape R rectangle\n" +
                    "motion R 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1\n" +
                    "motion R 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2\n" +
                    "shape E ellipse\n" +
                    "motion E 1 3 3 3 3 3 3 3 1 3 3 3 3 3 3 3\n" +
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

    // exception if modelOutput is null
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
