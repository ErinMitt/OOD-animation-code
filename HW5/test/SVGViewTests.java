import org.junit.Before;
import org.junit.Test;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.SVGView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the class SVGView.
 */
public class SVGViewTests {
  private AnimationModel model;
  private AnimationView view;
  private StringBuilder output;
  private final String easyOutput = "<svg width=\"1\" height=\"1\" version=\"1.1\" " +
          "xmlns=\"http://www.w3.org/2000/svg\">\n" +
          "<rect id=\"R\" x=\"1\" y=\"1\" width=\"1\" height=\"1\" fill=\"rgb(1,1,1)\" " +
          "visibility=\"visible\" >\n" +
          "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" attributeName=\"width\" " +
          "from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
          "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" attributeName=\"height\" " +
          "from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
          "</rect>\n" +
          "</svg>";

  @Before
  public void init() {
    model = new AnimationModelImpl();
    view = new SVGView();
    output = new StringBuilder();

    view.setModel(model);
    view.setOutput(output);
  }

  @Test
  public void testSetSpeed() {
    // test that a tick lasts 1000ms
    model.addRectangle("R");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("R", 2, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertNotEquals(-1, output.toString().indexOf("dur=\"1000ms\""));

    // test that a tick lasts 500ms
    output = new StringBuilder();
    view.setOutput(output);
    view.setSpeed(2);
    view.animate();
    assertNotEquals(-1, output.toString().indexOf("dur=\"500ms\""));

    // test that incorrect speeds cause exceptions
    try {
      view.setSpeed(0);
      fail("Set speed to 0");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of ticks per second must be a positive number, given 0.0",
              e.getMessage());
    }

    try {
      view.setSpeed(-1);
      fail("Set speed to a negative number");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of ticks per second must be a positive number, given -1.0",
              e.getMessage());
    }
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
    model2.addRectangle("R");
    model2.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model2.addMotion("R", 2, 1, 1, 2, 2, 1, 1, 1);

    AnimationView view2 = new SVGView();
    StringBuilder output2 = new StringBuilder();
    view2.setModel(model2);
    view2.setOutput(output2);
    view2.animate();

    assertEquals(easyOutput,
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
      new SVGView().setModel(null);
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
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "</svg>",
            output.toString());

    // a single rectangle with no movements
    init();
    model.addRectangle("R");
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n</svg>",
            output.toString());

    // one keyframe for a rectangle
    init();
    model.addRectangle("R");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "<rect id=\"R\" x=\"1\" y=\"1\" width=\"1\" height=\"1\" fill=\"rgb(1,1,1)\" " +
                    "visibility=\"visible\" >\n</rect>\n" +
                    "</svg>",
            output.toString());

    // a single movement for a rectangle
    init();
    model.addRectangle("R");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("R", 2, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals(easyOutput, output.toString());

    init();
    // two movements for a rectangle (all parameters change in motion 1)
    model.addRectangle("R");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 2, 3);
    model.addMotion("R", 3, 2, 2, 2, 2, 5, 5, 5);
    model.addMotion("R", 6, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "<rect id=\"R\" x=\"1\" y=\"1\" width=\"1\" height=\"1\" fill=\"rgb(1,2,3)\" " +
                    "visibility=\"visible\" >\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" " +
                    "attributeName=\"x\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" " +
                    "attributeName=\"y\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" " +
                    "attributeName=\"width\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" " +
                    "attributeName=\"height\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(1,2,3)\" to=\"rgb(5,5,5)\" " +
                    "fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"2000ms\" dur=\"3000ms\" " +
                    "attributeName=\"x\" from=\"2\" to=\"1\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"2000ms\" dur=\"3000ms\" " +
                    "attributeName=\"y\" from=\"2\" to=\"1\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"2000ms\" dur=\"3000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(5,5,5)\" to=\"rgb(1,1,1)\" " +
                    "fill=\"freeze\" />\n" +
                    "</rect>\n" +
                    "</svg>",
            output.toString());

    // a single ellipse with no movements
    init();
    model.addEllipse("E");
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n</svg>",
            output.toString());

    // one keyframe for a ellipse
    init();
    model.addEllipse("E");
    model.addMotion("E", 1, 1, 1, 2, 2, 1, 1, 1);
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "<ellipse id=\"E\" cx=\"1\" cy=\"1\" rx=\"1\" ry=\"1\" " +
                    "fill=\"rgb(1,1,1)\" visibility=\"visible\" >\n" +
                    "</ellipse>\n" +
                    "</svg>",
            output.toString());

    // a single movement for a ellipse
    init();
    model.addEllipse("E");
    model.addMotion("E", 1, 1, 1, 2, 2, 1, 1, 1);
    model.addMotion("E", 2, 1, 1, 4, 4, 1, 1, 1);
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "<ellipse id=\"E\" cx=\"1\" cy=\"1\" rx=\"1\" ry=\"1\" " +
                    "fill=\"rgb(1,1,1)\" visibility=\"visible\" >\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" " +
                    "attributeName=\"rx\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" " +
                    "attributeName=\"ry\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "</ellipse>\n" +
                    "</svg>",
            output.toString());

    // two movements for a ellipse (all parameters change in motion 1)
    init();
    model.addEllipse("E");
    model.addMotion("E", 1, 1, 1, 2, 2, 1, 1, 1);
    model.addMotion("E", 3, 2, 2, 4, 4, 5, 5, 5);
    model.addMotion("E", 6, 1, 1, 4, 4, 1, 1, 1);
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
            "xmlns=\"http://www.w3.org/2000/svg\">\n" +
            "<ellipse id=\"E\" cx=\"1\" cy=\"1\" rx=\"1\" ry=\"1\" fill=\"rgb(1,1,1)\" " +
            "visibility=\"visible\" >\n" +
            "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" attributeName=\"cx\" " +
            "from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
            "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" attributeName=\"cy\" " +
            "from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
            "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" attributeName=\"rx\" " +
            "from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
            "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" attributeName=\"ry\" " +
            "from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
            "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" attributeName=\"fill\" " +
            "from=\"rgb(1,1,1)\" to=\"rgb(5,5,5)\" fill=\"freeze\" />\n" +
            "<animate attributeType=\"xml\" begin=\"2000ms\" dur=\"3000ms\" attributeName=\"cx\" " +
            "from=\"2\" to=\"1\" fill=\"freeze\" />\n" +
            "<animate attributeType=\"xml\" begin=\"2000ms\" dur=\"3000ms\" attributeName=\"cy\" " +
            "from=\"2\" to=\"1\" fill=\"freeze\" />\n" +
            "<animate attributeType=\"xml\" begin=\"2000ms\" dur=\"3000ms\" " +
            "attributeName=\"fill\" from=\"rgb(5,5,5)\" to=\"rgb(1,1,1)\" fill=\"freeze\" />\n" +
            "</ellipse>\n" +
            "</svg>",
            output.toString());

    // a rectangle and an ellipse
    init();
    model.addRectangle("R");
    model.addEllipse("E");
    model.addMotion("R", 1, 1, 1, 1, 1, 1, 1, 1);
    model.addMotion("R", 2, 2, 2, 2, 2, 2, 2, 2);
    model.addMotion("E", 1, 3, 3, 6, 6, 3, 3, 3);
    model.addMotion("E", 4, 4, 4, 8, 8, 4, 4, 4);
    view.animate();
    assertEquals("<svg width=\"1\" height=\"1\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "<rect id=\"R\" x=\"1\" y=\"1\" width=\"1\" height=\"1\" " +
                    "fill=\"rgb(1,1,1)\" visibility=\"visible\" >\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" " +
                    "attributeName=\"x\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" " +
                    "attributeName=\"y\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" " +
                    "attributeName=\"width\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" " +
                    "attributeName=\"height\" from=\"1\" to=\"2\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(1,1,1)\" to=\"rgb(2,2,2)\" " +
                    "fill=\"freeze\" />\n" +
                    "</rect>\n" +
                    "<ellipse id=\"E\" cx=\"3\" cy=\"3\" rx=\"3\" ry=\"3\" " +
                    "fill=\"rgb(3,3,3)\" visibility=\"visible\" >\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"3000ms\" " +
                    "attributeName=\"cx\" from=\"3\" to=\"4\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"3000ms\" " +
                    "attributeName=\"cy\" from=\"3\" to=\"4\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"3000ms\" " +
                    "attributeName=\"rx\" from=\"3\" to=\"4\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"3000ms\" " +
                    "attributeName=\"ry\" from=\"3\" to=\"4\" fill=\"freeze\" />\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"3000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(3,3,3)\" to=\"rgb(4,4,4)\" " +
                    "fill=\"freeze\" />\n" +
                    "</ellipse>\n" +
                    "</svg>",
            output.toString());

    // exception if model is null
    AnimationView view2 = new SVGView();
    view2.setOutput(output);
    try {
      view2.animate();
      fail("Animated with no model");
    } catch (IllegalStateException e) {
      assertEquals("The model has not been set",
              e.getMessage());
    }

    // exception if modelOutput is null
    view2 = new SVGView();
    view2.setModel(model);
    try {
      view2.animate();
      fail("Animated with no view");
    } catch (IllegalStateException e) {
      assertEquals("The output Appendable has not been set",
              e.getMessage());
    }

    // test dealing with IOExceptions
    view2 = new SVGView();
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
