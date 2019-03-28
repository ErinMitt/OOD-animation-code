package cs3500.animator.view;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import cs3500.animator.controller.Features;
import cs3500.animator.model.Motion;

/**
 * Represents a dialog window that allows a user to edit a shape in an AnimationModel's Motions.
 */
class EditShapeDialog {
  private final JDialog dialog;
  private static final int TEXT_FIELD_WIDTH = 3;

  // new keyframe controls
  //private final JButton addButton;
  private final JTextField addTime;
  private final JTextField addX;
  private final JTextField addY;
  private final JTextField addWidth;
  private final JTextField addHeight;
  private final JTextField addRed;
  private final JTextField addGreen;
  private final JTextField addBlue;

  // edit keyframe controls
  //private final JButton editButton;
  private final JTextField editX;
  private final JTextField editY;
  private final JTextField editWidth;
  private final JTextField editHeight;
  private final JTextField editRed;
  private final JTextField editGreen;
  private final JTextField editBlue;

  // delete keyframe controls
  //private final JButton deleteButton;

  // keyframe timeline
  private final JList<String> keyframes;

  public EditShapeDialog(JFrame owner, List<Motion> frames, String shape,
                         Features features) {
    dialog = new JDialog(owner, "Edit " + shape);

    // create the timeline of keyframes
    keyframes = new JList<>();
    keyframes.setLayoutOrientation(JList.VERTICAL_WRAP);
    List<String> keyframeNumbers = frames.stream()
            .map(m -> Integer.toString(m.getTime()))
            .collect(Collectors.toList());
    keyframes.setListData(keyframeNumbers.toArray(new String[0]));
    keyframes.setVisibleRowCount(1);

    // create all buttons and text fields for adding frames
    JButton addButton = new JButton("add frame");
    addTime = new JTextField();
    addX = new JTextField();
    addY = new JTextField();
    addWidth = new JTextField();
    addHeight = new JTextField();
    addRed = new JTextField();
    addGreen = new JTextField();
    addBlue = new JTextField();
    addButton.addActionListener(evt -> features.addKeyframe(shape, addTime.getText(),
            addX.getText(), addY.getText(), addWidth.getText(), addHeight.getText(),
            addRed.getText(), addGreen.getText(), addBlue.getText()));
    addTime.addActionListener(evt -> features.suggestNewKeyframe(shape, addTime.getText()));

    addTime.setColumns(TEXT_FIELD_WIDTH);
    addX.setColumns(TEXT_FIELD_WIDTH);
    addY.setColumns(TEXT_FIELD_WIDTH);
    addWidth.setColumns(TEXT_FIELD_WIDTH);
    addHeight.setColumns(TEXT_FIELD_WIDTH);
    addRed.setColumns(TEXT_FIELD_WIDTH);
    addGreen.setColumns(TEXT_FIELD_WIDTH);
    addBlue.setColumns(TEXT_FIELD_WIDTH);

    // create all buttons and text fields for editing frames
    JButton editButton = new JButton("edit");
    editX = new JTextField();
    editY = new JTextField();
    editWidth = new JTextField();
    editHeight = new JTextField();
    editRed = new JTextField();
    editGreen = new JTextField();
    editBlue = new JTextField();
    editButton.addActionListener(evt -> features.editKeyframe(shape, keyframes.getSelectedValue(),
        editX.getText(), editY.getText(), editWidth.getText(), editHeight.getText(),
        editRed.getText(), editGreen.getText(), editBlue.getText()));
    keyframes.addListSelectionListener(
        evt -> features.suggestEditKeyframe(shape, keyframes.getSelectedValue()));

    editX.setColumns(TEXT_FIELD_WIDTH);
    editY.setColumns(TEXT_FIELD_WIDTH);
    editWidth.setColumns(TEXT_FIELD_WIDTH);
    editHeight.setColumns(TEXT_FIELD_WIDTH);
    editRed.setColumns(TEXT_FIELD_WIDTH);
    editGreen.setColumns(TEXT_FIELD_WIDTH);
    editBlue.setColumns(TEXT_FIELD_WIDTH);

    // create the button for deleting a keyframe
    JButton deleteButton = new JButton("delete");
    deleteButton.addActionListener(
        evt -> features.removeKeyframe(shape, keyframes.getSelectedValue()));




    // add all buttons and text fields to the dialog
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    dialog.add(container);

    // add stuff to "add keyframe" panel
    JPanel addPanel = new JPanel();
    addPanel.setLayout(new FlowLayout());
    container.add(addPanel);

    addPanel.add(addButton);
    addPanel.add(new JLabel("time:"));
    addPanel.add(addTime);
    addPanel.add(new JLabel("X"));
    addPanel.add(addX);
    addPanel.add(new JLabel("Y"));
    addPanel.add(addY);
    addPanel.add(new JLabel("width"));
    addPanel.add(addWidth);
    addPanel.add(new JLabel("height"));
    addPanel.add(addHeight);
    addPanel.add(new JLabel("red"));
    addPanel.add(addRed);
    addPanel.add(new JLabel("green"));
    addPanel.add(addGreen);
    addPanel.add(new JLabel("blue"));
    addPanel.add(addBlue);

    // add stuff to "edit keyframe" panel
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new FlowLayout());
    container.add(editPanel);

    editPanel.add(editButton);
    editPanel.add(deleteButton);
    editPanel.add(new JLabel("X"));
    editPanel.add(editX);
    editPanel.add(new JLabel("Y"));
    editPanel.add(editY);
    editPanel.add(new JLabel("width"));
    editPanel.add(editWidth);
    editPanel.add(new JLabel("height"));
    editPanel.add(editHeight);
    editPanel.add(new JLabel("red"));
    editPanel.add(editRed);
    editPanel.add(new JLabel("green"));
    editPanel.add(editGreen);
    editPanel.add(new JLabel("blue"));
    editPanel.add(editBlue);

    // add the keyframe timeline
    JScrollPane scroll = new JScrollPane(keyframes);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    container.add(scroll);
    scroll.setSize(new Dimension());

    dialog.pack();
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

  /**
   * Get the shape-editing dialog box.
   * @return the dialog
   */
  public JDialog getDialog() {
    return dialog;
  }

  /**
   * Call the method setVisible on the JDialog.
   */
  public void makeVisible() {
    dialog.setVisible(true);
  }

  /**
   * Call the method addWindowListener on the JDialog.
   */
  public void addWindowListener(WindowListener listener) {
    dialog.addWindowListener(listener);
  }

  /**
   * Call the method dispose on the JDialog.
   */
  public void dispose() {
    dialog.dispose();
  }
}
