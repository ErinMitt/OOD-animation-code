package cs3500.animator.view;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

/**
 * Represents a class that can create dialog boxes to edit, add, or remove a shape's keyframes.
 */
abstract class EditShapeDialogFactory {
  private ReadOnlyModel model;

  /**
   * A method that adds a keyframe to an AnimationModel. Must be overridden.
   */
  public abstract void addKeyframe();

  /**
   * A method that can edit an existing keyframe in an AnimationModel. Must be overridden.
   */
  public abstract void editKeyframe();

  /**
   * A method that can remove a keyframe from an AnimationModel. Must be overridden.
   */
  public abstract void removeKeyframe();

  /**
   * Build a dialog box that allows editing of a shape's Motions.
   * @param shape the shape to be edited
   * @return the dialog box
   */
  public EditShapeDialog getDialog(String shape, JFrame owner) {
    if (shape == null) {
      throw new IllegalArgumentException("Shape must not be null");
    }
    List<Motion> motions;
    try {
      motions = model.getMotions(shape); // TODO: come back to this
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException ("No such shape " + shape);
    }
    EditShapeDialog dialog = new EditShapeDialog(owner, "Edit " + shape, this);
    // TODO: more setup stuff?
    return dialog;
  }

  /**
   * Set the model to be edited.
   * @param model the model
   */
  public void setModel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    if (this.model != null) {
      throw new IllegalStateException("Model has already been set");
    }
    this.model = model;
  }
}
