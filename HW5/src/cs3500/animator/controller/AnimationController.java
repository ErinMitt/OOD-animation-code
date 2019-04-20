package cs3500.animator.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import cs3500.animator.ViewFactory;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.model.Motion;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.EditorAnimationView;
import cs3500.animator.view.EditorView;

/**
 * A class representing a controller that allows communication
 * between an AnimationModel and an EditorView.
 */
public class AnimationController implements Features, Controller {
  private final AnimationModel model;
  private final EditorAnimationView view;

  /**
   * Build a controller for animation and prepare everything necessary to play it.
   * @param model the model representing the animation
   * @param view the view displaying the animation
   */
  public AnimationController(AnimationModel model, EditorAnimationView view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view must not be null");
    }
    this.model = model;
    this.view = view;
    try {
      view.addFeatures(this);
    } catch (UnsupportedOperationException e) {
      // addFeatures may be unsupported if the view does not need user controls,
      // such as a VisualView, TextView, or SVGView wrapped in an EditorViewWrapper.
      // This is expected - do nothing if this happens.
    }
    view.setModel(model);
  }

  // this method should be named go. Renamed it to gogo to make the style checker happy
  @Override
  public void gogo() {
    view.animate();
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
  public void setTick(int tick) {
    view.pause();
    view.setTick(tick);
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
   * Add a keyframe to the model.
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
   * Edit one of the model's keyframes.
   * @throws IllegalArgumentException if any of the inputs are null
   */
  @Override
  public void editKeyframe(String shape, String time, String x, String y,
                           String width, String height, String red, String green, String blue) {
    if (time == null) {
      view.displayErrorMessage("No keyframe selected");
      return;
    }
    if (shape == null || x == null || y == null || width == null || height == null
            || red == null || green == null || blue == null) {
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
   * remove a keyframe from the model.
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
    if (model.getMotions(shape).isEmpty()) {
      m = new Motion(Motion.START_TICK, 0, 0, 10, 10, 0, 0, 0);
    }
    else {
      try { // if the new keyframe happens during the existing animation
        m = model.getTransformationAt(shape, t).getStateAt(t);
      } catch (IllegalArgumentException e) {
        // if the new keyframe happens before or after the animation
        List<Motion> motions = model.getMotions(shape);
        if (t < motions.get(0).getTime()) {
          m = motions.get(0);
        } else {
          m = motions.get(motions.size() - 1);
        }
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

  @Override
  public void addShape(String name, String type) {
    if (name == null || type == null) {
      throw new IllegalArgumentException("Shape name and type must not be null");
    }
    if (name.equals("")) {
      view.displayErrorMessage("Names must have at least one character");
      return;
    }
    if (name.contains(" ")) {
      view.displayErrorMessage("Names cannot have spaces");
      return;
    }
    if (model.getShapes().contains(name)) {
      view.displayErrorMessage("There is already a shape by the name " + name);
      return;
    }
    switch (type) {
      case "ellipse":
        model.addEllipse(name);
        break;
      case "rectangle":
        model.addRectangle(name);
        break;
      default:
        throw new IllegalArgumentException("There is no shape type " + type);
    }
    view.setShapeList(model.getShapes());
  }

  @Override
  public void deleteShape(String name) {
    if (name == null) {
      view.displayErrorMessage("There is no shape selected");
      return;
    }

    if (!model.getShapes().contains(name)) {
      view.displayErrorMessage("There is no shape by this name");
      return;
    }
    model.deleteShape(name);
    view.setShapeList(model.getShapes());
    view.drawCurrentTick();
  }

  @Override
  public void save(String type, String fileName) {
    // check inputs for validity
    if (type == null || fileName == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    }
    if (fileName.equals("")) {
      view.displayErrorMessage("Output location must be set");
      return;
    }
    // set up the Editor view's new output
    switch (type) {
      case "svg":
        fileName += ".svg";
        break;
      case "text":
        fileName += ".txt";
        break;
      default:
        throw new IllegalArgumentException("Unsupported file type " + type);
    }
    FileWriter writer;
    try {
      writer = new FileWriter(fileName);
    } catch (IOException e) {
      view.displayErrorMessage("Could not create a file named " + fileName);
      return;
    }
    try {
      view.setOutput(writer);
    } catch (UnsupportedOperationException e) {
      throw new IllegalArgumentException("View does not support setting an output");
    }
    // may throw IAE if view type is wrong
    EditorAnimationView saveView = ViewFactory.buildView(type);
    String text;
    try {
      StringBuilder builder = new StringBuilder();
      saveView.setOutput(builder);
      new AnimationController(model, saveView).gogo();
      text = builder.toString();
    } catch (UnsupportedOperationException e) {
      throw new IllegalArgumentException("View type " + type + " does not support output");
    } catch (IllegalStateException e) {
      view.displayErrorMessage("Could not write to the output");
      return;
    }
    try {
      view.save(text);
    } catch (IllegalStateException e) {
      view.displayErrorMessage("Could not write to the output file");
      return;
    }
    try {
      writer.close();
    } catch (IOException e) {
      view.displayErrorMessage("Could not close output file");
    }
  }

  @Override
  public void load(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    }
    FileReader reader;
    try {
      reader = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      view.displayErrorMessage("Unable to locate file.");
      return;
    }
    AnimationModel model;
    try {
      model = AnimationReader.parseFile(reader, new AnimationModelImpl.Builder());
    } catch (IllegalStateException e) {
      view.displayErrorMessage("Incorrect file formatting");
      return;
    }
    EditorAnimationView view = new EditorView();
    AnimationController controller = new AnimationController(model, view);
    controller.gogo();
  }
}
