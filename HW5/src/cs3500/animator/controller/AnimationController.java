package cs3500.animator.controller;

import java.util.List;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Motion;
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
      // TODO: automatically suggest edit/add based on current frame?
      view.pause();
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

  /**
   * @throws IllegalArgumentException if any inputs are null
   */
  @Override
  public void addKeyframe(String shape, String time, String x, String y,
                          String width, String height, String red, String green, String blue) {
    if (shape == null || time == null || x == null || y == null || width == null || height == null
            || red == null || green == null || blue == null) {
      throw new IllegalArgumentException("Inputs must not be null");
    }
    try {
      model.addMotion(shape, Integer.parseInt(time), Integer.parseInt(x), Integer.parseInt(y),
              Integer.parseInt(width), Integer.parseInt(height),
              Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
      view.exitShapeEditor();
      view.updateMaxTick();
      view.drawCurrentTick();
    } catch (NumberFormatException e) {
      view.displayErrorMessage("Keyframe parameters must be numbers");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage("Couldn't add keyframe: " + e.getMessage());
    }
  }

  /**
   * @throws IllegalArgumentException if any of the inputs are null
   */
  @Override
  public void editKeyframe(String shape, String time, String x, String y,
                           String width, String height, String red, String green, String blue) {
    if (time == null) {
      view.displayErrorMessage("No keyframe selected");
      return;
    }
    if (shape == null || x == null || y == null || width == null || height == null ||
            red == null || green == null || blue == null) {
      throw new IllegalArgumentException("Inputs must not be null");
    }
    try {
      model.editMotion(shape, Integer.parseInt(time), Integer.parseInt(x), Integer.parseInt(y),
              Integer.parseInt(width), Integer.parseInt(height),
              Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
      view.exitShapeEditor();
      view.drawCurrentTick();
    } catch (NumberFormatException e) {
      view.displayErrorMessage("Keyframe parameters must be numbers");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage("Couldn't edit keyframe: " + e.getMessage());
    }
  }

  /**
   * @throws IllegalArgumentException if the shape is null
   */
  @Override
  public void removeKeyframe(String shape, String time) {
    if (time == null) {
      view.displayErrorMessage("No keyframe selected");
      return;
    }
    if (shape == null) {
      throw new IllegalArgumentException("Shape must not be null");
    }
    try {
      model.deleteMotion(shape, Integer.parseInt(time));
      view.exitShapeEditor();
      view.updateMaxTick();
      view.drawCurrentTick();
    } catch (NumberFormatException e) {
      view.displayErrorMessage("Keyframe parameters must be numbers");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage("Couldn't delete keyframe: " + e.getMessage());
    }
  }

  @Override
  public void suggestNewKeyframe(String shape, String time) {
    if (shape == null) {
      throw new IllegalArgumentException("Shape must not be null");
    }
    if (time == null) {
      throw new IllegalArgumentException("Time must not be null");
    }
    int t;
    try {
      t = Integer.parseInt(time);
    } catch (NumberFormatException e) {
      view.displayErrorMessage("Tick number must be an integer");
      return;
    }
    if (! model.getShapes().contains(shape)) {
      throw new IllegalArgumentException("No such shape " + shape);
    }
    Motion m;
    try { // if the new keyframe happens during the existing animation
      m = model.getTransformationAt(shape, t).getStateAt(t);
    } catch (IllegalArgumentException e) {
      // if the new keyframe happens before or after the animation
      List<Motion> motions = model.getMotions(shape);
      if (motions.isEmpty()) { // if there are no reference motions, choose a random one
        m = new Motion(Motion.START_TICK, 0, 0, 10, 10, 0, 0, 0);
      }
      else if (t < motions.get(0).getTime()) {
        m = motions.get(0);
      }
      else {
        m = motions.get(motions.size() - 1);
      }
    }
    if (t < Motion.START_TICK) {
      view.displayErrorMessage("Cannot add a frame at tick " + t);
      return;
    }
    try {
      view.setNewFrameText(m.extend(t));
    } catch (IllegalStateException e) {
      view.displayErrorMessage("Can't add a new frame when not in shape editor mode");
    }
  }

  @Override
  public void suggestEditKeyframe(String shape, String time) {
    if (shape == null) {
      throw new IllegalArgumentException("Shape must not be null");
    }
    if (time == null) {
      view.displayErrorMessage("Must select a time to edit keyframes");
      return;
    }
    int t;
    try {
      t = Integer.parseInt(time);
    } catch (NumberFormatException e) {
      view.displayErrorMessage("Tick number must be an integer");
      return;
    }
    if (! model.getShapes().contains(shape)) {
      throw new IllegalArgumentException("No such shape " + shape);
    }
    for (Motion m : model.getMotions(shape)) {
      if (m.getTime() == t) {
        try {
          view.setEditFrameText(m);
          return;
        } catch (IllegalStateException e) {
          view.displayErrorMessage("Can't edit a frame when not in shape editor mode");
        }
      }
      if (t < m.getTime()) {
        break;
      }
    }
    view.displayErrorMessage("The shape " + shape + " has no keyframes at tick " + time);
  }
}
