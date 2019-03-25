package cs3500.animator.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.*;

import cs3500.animator.controller.Features;
import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

/**
 * Represents a class that can create dialog boxes to edit, add, or remove a shape's keyframes.
 */
class EditShapeDialogFactory {
  private ReadOnlyModel model;
  private final Features features;

  public EditShapeDialogFactory(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Features must not be null");
    }
    this.features = features;
  }

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
      motions = model.getMotions(shape);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException ("No such shape " + shape);
    }
    EditShapeDialog dialog = new EditShapeDialog(owner, motions, shape, features);
    dialog.addWindowListener(new WindowListener() {
      @Override
      public void windowOpened(WindowEvent e) {
        // do nothing if the window is open
      }

      @Override
      public void windowClosing(WindowEvent e) {
        // if the dialog is closed manually, reset the dialog.
        features.exitShapeEditor();
      }

      @Override
      public void windowClosed(WindowEvent e) {
        // do nothing
      }

      @Override
      public void windowIconified(WindowEvent e) {
        // do nothing
      }

      @Override
      public void windowDeiconified(WindowEvent e) {
        // do nothing
      }

      @Override
      public void windowActivated(WindowEvent e) {
        // do nothing
      }

      @Override
      public void windowDeactivated(WindowEvent e) {
        // do nothing
      }
    });
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
