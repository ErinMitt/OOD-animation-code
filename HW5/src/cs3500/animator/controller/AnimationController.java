package cs3500.animator.controller;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.view.EditorAnimationView;

/**
 * A class representing a controller that allows communication
 * between an AnimationModel and an EditorView.
 */
public class AnimationController implements Features {
  private final AnimationModel model;
  private final EditorAnimationView view;

  public AnimationController(AnimationModel model, EditorAnimationView view) {
    this.model = model;
    this.view = view; // TODO: add model to view here!
    view.addFeatures(this);
  }


  @Override
  public void togglePlay() {
    view.togglePlay();
  }

  @Override
  public void toggleLoop() {
    view.toggleLoop();
  }

  @Override
  public void rewind() {
    view.rewind();
  }

  @Override
  public void stepForward() {
    view.pause();
    view.incrementTick();
  }

  @Override
  public void stepBackwards() {
    view.pause();
    view.decrementTick();
  }

  @Override
  public void setSpeedToUserInput(String input) {
    try {
      view.setSpeed(Double.parseDouble(input));
    } catch (NumberFormatException e) {
      view.displayErrorMessage("Speed must be a number");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    } finally {
      view.resetFocus();
    }
  }

  @Override
  public void resetTextFields() {
    view.resetTextFields();
  }

  @Override
  public void enterShapeEditor(String shape) {
    try {
      view.enterShapeEditor(shape);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage("No shape selected");
    } catch (IllegalStateException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  @Override
  public void exitShapeEditor() {
    try {
      view.exitShapeEditor();
    } catch (IllegalStateException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }
}
