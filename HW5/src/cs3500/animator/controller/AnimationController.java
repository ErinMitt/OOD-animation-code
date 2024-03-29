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
  public void showShapeList(String layer) {
    if (layer == null) {
      view.displayErrorMessage("No shape selected");
      return;
    }
    try {
      view.setShapeList(model.getShapes(layer));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("There is no layer by the name " + layer);
    }
  }

  @Override
  public void enterShapeEditor(String layer, String shape) {
    try {
      view.enterShapeEditor(layer, shape);
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
  public void addKeyframe(String layer, String shape, String time, String x, String y,
                          String width, String height,
                          String red, String green, String blue, String rotation) {
    if (layer == null || shape == null || time == null || x == null || y == null
            || width == null || height == null
            || red == null || green == null || blue == null || rotation == null) {
      throw new IllegalArgumentException("Inputs must not be null");
    }
    try {
      model.addMotion(layer, shape, Integer.parseInt(time),
              Integer.parseInt(x), Integer.parseInt(y),
              Integer.parseInt(width), Integer.parseInt(height),
              Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue),
              Integer.parseInt(rotation));
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
  public void editKeyframe(String layer, String shape, String time, String x, String y,
                           String width, String height,
                           String red, String green, String blue, String rotation) {
    if (time == null) {
      view.displayErrorMessage("No keyframe selected");
      return;
    }
    if (layer == null || shape == null || x == null || y == null
            || width == null || height == null
            || red == null || green == null || blue == null || rotation == null) {
      throw new IllegalArgumentException("Inputs must not be null");
    }
    try {
      model.editMotion(layer, shape, Integer.parseInt(time),
              Integer.parseInt(x), Integer.parseInt(y),
              Integer.parseInt(width), Integer.parseInt(height),
              Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue),
              Integer.parseInt(rotation));
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
  public void removeKeyframe(String layer, String shape, String time) {
    if (time == null) {
      view.displayErrorMessage("No keyframe selected");
      return;
    }
    if (layer == null || shape == null) {
      throw new IllegalArgumentException("Shape and layer must not be null");
    }
    try {
      model.deleteMotion(layer, shape, Integer.parseInt(time));
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
  public void suggestNewKeyframe(String layer, String shape, String time) {
    if (layer == null || shape == null) {
      throw new IllegalArgumentException("Shape and layer must not be null");
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
    if (! model.getShapes(layer).contains(shape)) {
      throw new IllegalArgumentException("No such shape " + shape);
    }
    Motion m;
    if (model.getMotions(layer, shape).isEmpty()) {
      m = new Motion(Motion.START_TICK, 0, 0, 10, 10, 0, 0, 0);
    }
    else {
      try { // if the new keyframe happens during the existing animation
        m = model.getTransformationAt(layer, shape, t).getStateAt(t);
      } catch (IllegalArgumentException e) {
        // if the new keyframe happens before or after the animation
        List<Motion> motions = model.getMotions(layer, shape);
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
  public void suggestEditKeyframe(String layer, String shape, String time) {
    if (layer == null || shape == null) {
      throw new IllegalArgumentException("Shape and layer must not be null");
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
    if (! model.getShapes(layer).contains(shape)) {
      throw new IllegalArgumentException("No such shape " + shape);
    }
    for (Motion m : model.getMotions(layer, shape)) {
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
  public void addLayer(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Layer name must not be null");
    }
    if (name.equals("")) {
      view.displayErrorMessage("Names must have at least one character");
      return;
    }
    if (name.contains(" ")) {
      view.displayErrorMessage("Names cannot have spaces");
      return;
    }
    if (model.getLayers().contains(name)) {
      view.displayErrorMessage("There is already a layer by the name " + name);
      return;
    }
    model.addLayer(name);
    view.setLayerList(model.getLayers());
  }

  @Override
  public void moveLayer(String layer, int position) {
    if (layer == null) {
      view.displayErrorMessage("No layer selected");
      return;
    }
    if (position < 0 || position >= model.getLayers().size()) {
      view.displayErrorMessage("Cannot move layer to position " + (position + 1));
      return;
    }
    model.moveLayer(layer, position);
    view.setLayerList(model.getLayers());
    view.drawCurrentTick();
  }

  @Override
  public void addShape(String layer, String shapeName, String type) {
    if (shapeName == null || type == null) {
      throw new IllegalArgumentException("Shape name and type must not be null");
    }
    if (layer == null) {
      view.displayErrorMessage("No layer selected");
      return;
    }
    if (shapeName.equals("")) {
      view.displayErrorMessage("Names must have at least one character");
      return;
    }
    if (shapeName.contains(" ")) {
      view.displayErrorMessage("Names cannot have spaces");
      return;
    }
    if (model.getShapes(layer).contains(shapeName)) {
      view.displayErrorMessage("There is already a shape by the name " + shapeName
              + " in the layer " + layer);
      return;
    }
    switch (type) {
      case "ellipse":
        model.addEllipse(layer, shapeName);
        break;
      case "rectangle":
        model.addRectangle(layer, shapeName);
        break;
      default:
        throw new IllegalArgumentException("There is no shape type " + type);
    }
    view.setShapeList(model.getShapes(layer));
  }

  @Override
  public void deleteShape(String layer, String name) {
    if (layer == null) {
      view.displayErrorMessage("There is no layer selected");
      return;
    }
    if (name == null) {
      view.displayErrorMessage("There is no shape selected");
      return;
    }

    if (!model.getShapes(layer).contains(name)) {
      view.displayErrorMessage("There is no shape by this name");
      return;
    }
    model.deleteShape(layer, name);
    view.setShapeList(model.getShapes(layer));
    view.drawCurrentTick();
  }

  @Override
  public void deleteLayer(String name) {
    if (name == null) {
      view.displayErrorMessage("No layer selected");
    }
    if (! model.getLayers().contains(name)) {
      view.displayErrorMessage("There is no layer by the name " + name);
      return;
    }
    model.deleteLayer(name);
    view.setLayerList(model.getLayers());
    view.drawCurrentTick();
  }

  @Override
  public void save(double speed, String type, String fileName) {
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
      try {
        saveView.setSpeed(speed);
      } catch (UnsupportedOperationException e) {
        // It's expected for some view types not to support speed. Fail quietly.
      }
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
