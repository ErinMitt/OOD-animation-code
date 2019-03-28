import org.junit.Before;
import org.junit.Test;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.view.EditorAnimationView;
import cs3500.animator.view.EditorViewWrapper;

import static org.junit.Assert.assertEquals;

/**
 * A class that tests the functionality of EditorViewWrappers.
 */
public class EditorAnimationWrapperTests {
  Appendable modelOutput;
  EditorAnimationView outerView;
  WritingMockView innerView;

  @Before
  public void init() {
    modelOutput = new StringBuilder();
    innerView = new WritingMockView(modelOutput);
    outerView = new EditorViewWrapper(innerView);
  }

  // tests that the wrapper delegates all methods to the model
  @Test
  public void testSetModel() {
    assertEquals("", modelOutput.toString());
    AnimationModel m = new AnimationModelImpl();
    outerView.setModel(m);
    assertEquals("setModel called with " + m + "\n", modelOutput.toString());
  }

  @Test
  public void testSetSpeed() {
    assertEquals("", modelOutput.toString());
    outerView.setSpeed(5);
    assertEquals("setSpeed called with 5.0\n", modelOutput.toString());
  }

  @Test
  public void testSetOutput() {
    assertEquals("", modelOutput.toString());
    outerView.setOutput(new StringBuilder());
    assertEquals("setOutput called\n", modelOutput.toString());
  }

  @Test
  public void testAnimate() {
    assertEquals("", modelOutput.toString());
    outerView.animate();
    assertEquals("animate called\n", modelOutput.toString());
  }
}
