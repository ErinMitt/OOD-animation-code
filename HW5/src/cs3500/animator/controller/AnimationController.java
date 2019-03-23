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
}
