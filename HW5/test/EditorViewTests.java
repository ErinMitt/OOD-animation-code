import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;
import cs3500.animator.model.Transformation;
import cs3500.animator.view.EditorView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the class EditorView.
 */
public class EditorViewTests {
  EditorView original;

  @Before
  public void init() {
    original = new EditorView();
  }

  @Test
  public void testAddFeatures() {
    // use controller mock
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
    ReadOnlyModel model =null;
    original.setModel(model);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetModelExists() {
  }

  @Test
  public void testSetModelWorks() {

  }

  @Test
  public void testAnimate() {

  }

  @Test
  public void testSetSpeed() {

  }

  @Test
  public void testSave() {

  }

  @Test
  public void testSetOutput() {

  }
}