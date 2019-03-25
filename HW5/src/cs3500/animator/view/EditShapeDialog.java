package cs3500.animator.view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import cs3500.animator.model.Motion;

/**
 * Represents a dialog window that allows a user to edit a shape in an AnimationModel's Motions.
 */
public class EditShapeDialog extends JDialog {
  private final static int TEXT_FIELD_WIDTH = 3;

  // new keyframe controls
  private final JButton addButton;
  private final JTextField addTime;
  private final JTextField addX;
  private final JTextField addY;
  private final JTextField addWidth;
  private final JTextField addHeight;
  private final JTextField addRed;
  private final JTextField addGreen;
  private final JTextField addBlue;

  // edit keyframe controls
  private final JButton editButton;
  private final JTextField editX;
  private final JTextField editY;
  private final JTextField editWidth;
  private final JTextField editHeight;
  private final JTextField editRed;
  private final JTextField editGreen;
  private final JTextField editBlue;

  // delete keyframe controls
  private final JButton delete;

  // keyframe timeline
  //private final JList keyframes;

  public EditShapeDialog(JFrame owner, String title, EditShapeDialogFactory factory) {
    super(owner, title);
    addButton = new JButton("add frame");
    addButton.addActionListener(evt -> factory.addKeyframe());
    addTime = new JTextField();
    addX = new JTextField();
    addY = new JTextField();
    addWidth = new JTextField();
    addHeight = new JTextField();
    addRed = new JTextField();
    addGreen = new JTextField();
    addBlue = new JTextField();

    addTime.setColumns(TEXT_FIELD_WIDTH);
    addX.setColumns(TEXT_FIELD_WIDTH);
    addY.setColumns(TEXT_FIELD_WIDTH);
    addWidth.setColumns(TEXT_FIELD_WIDTH);
    addHeight.setColumns(TEXT_FIELD_WIDTH);
    addRed.setColumns(TEXT_FIELD_WIDTH);
    addGreen.setColumns(TEXT_FIELD_WIDTH);
    addBlue.setColumns(TEXT_FIELD_WIDTH);

    editButton = new JButton("edit");
    editButton.addActionListener(evt -> factory.editKeyframe());
    editX = new JTextField();
    editY = new JTextField();
    editWidth = new JTextField();
    editHeight = new JTextField();
    editRed = new JTextField();
    editGreen = new JTextField();
    editBlue = new JTextField();

    delete = new JButton("delete");
    delete.addActionListener(evt -> factory.removeKeyframe());

    // add all buttons and text fields to the dialog
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    this.add(container);

    JPanel editPanel = new JPanel();
    editPanel.setLayout(new FlowLayout());
    container.add(editPanel);

    editPanel.add(addButton);
    editPanel.add(new JLabel("time:"));
    editPanel.add(addTime);
    editPanel.add(new JLabel("X"));
    editPanel.add(addX);
    editPanel.add(new JLabel("Y"));
    editPanel.add(addY);
    editPanel.add(new JLabel("width"));
    editPanel.add(addWidth);
    editPanel.add(new JLabel("height"));
    editPanel.add(addHeight);
    editPanel.add(new JLabel("red"));
    editPanel.add(addRed);
    editPanel.add(new JLabel("green"));
    editPanel.add(addGreen);
    editPanel.add(new JLabel("blue"));
    editPanel.add(addBlue);

    pack();
  }

  /**
   * Set the text boxes to create a new keyframe to match the given motion.
   * @param m the motion to be set
   */
  public void setNewFrameText(Motion m) {
    addTime.setText(Integer.toString(m.getTime()));
    addX.setText(Integer.toString(m.getX()));
    addY.setText(Integer.toString(m.getY()));
    addWidth.setText(Integer.toString(m.getWidth()));
    addHeight.setText(Integer.toString(m.getHeight()));
    addRed.setText(Integer.toString(m.getRed()));
    addGreen.setText(Integer.toString(m.getGreen()));
    addBlue.setText(Integer.toString(m.getBlue()));
  }

  /**
   * Set the text boxes to edit an existing keyframe to match the given motion.
   * @param m the motion to be set
   */
  public void setEditFrameText(Motion m) {
    editX.setText(Integer.toString(m.getX()));
    editY.setText(Integer.toString(m.getY()));
    editWidth.setText(Integer.toString(m.getWidth()));
    editHeight.setText(Integer.toString(m.getHeight()));
    editRed.setText(Integer.toString(m.getRed()));
    editGreen.setText(Integer.toString(m.getGreen()));
    editBlue.setText(Integer.toString(m.getBlue()));
  }
}
