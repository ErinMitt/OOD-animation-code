package cs3500.animator.view;

import java.util.List;

import cs3500.animator.controller.Features;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

/**
 * Represents a class that "wraps" an AnimationView to implement an EditorAnimationView.
 * Any methods that do not exist in the AnimationView are unsupported;
 * all the rest are delegated.
 */
public class EditorViewWrapper implements EditorAnimationView {
  private static final String ERROR_MESSAGE
          = "The view wrapper does not support EditorAnimationView-exclusive methods";
  private final AnimationView view;

  public EditorViewWrapper(AnimationView view) {
    this.view = view;
  }

  // supported methods
  @Override
  public void setModel(ReadOnlyModel model) {
    view.setModel(model);
  }

  @Override
  public void animate() {
    view.animate();
  }

  @Override
  public void setSpeed(double speed) {
    view.setSpeed(speed);
  }

  @Override
  public void setOutput(Appendable output) {
    view.setOutput(output);
  }

  // unsupported methods
  @Override
  public void drawCurrentTick() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void togglePlay() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void pause() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void rewind() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void incrementTick() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void decrementTick() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void toggleLoop() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void updateMaxTick() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void setShapeList(List<String> shapes) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void save(String type, AnimationModel model) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void addFeatures(Features features) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void displayErrorMessage(String message) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void resetFocus() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void resetTextFields() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void enterShapeEditor(String shape) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void exitShapeEditor() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void setNewFrameText(Motion m) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void setEditFrameText(Motion m) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }
}
