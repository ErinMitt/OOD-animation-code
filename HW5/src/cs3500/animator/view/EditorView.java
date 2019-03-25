package cs3500.animator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.*;


import cs3500.animator.controller.Features;
import cs3500.animator.model.Motion;
import cs3500.animator.model.ReadOnlyModel;

/**
 * Class representing a view that allows editing of an animation model.
 */
public class EditorView extends JFrame implements EditorAnimationView {
  private static String CAPTION = "Animation editor";

  private AnimationPanel animationPanel;
  private double speed;
  private boolean playing;
  private Timer timer;
  // INVARIANT: the delay equals 1000 / speed
  private int tick;
  // INVARIANT: if animationPanel exists, it displays the current tick
  private boolean looping;
  private int maxTick;
  // INVARIANT: equals the time of the last keyframe in the model.

  // save location
  private Appendable output;

  // buttons
  private final JToggleButton play;
  // INVARIANT: if toggled, playing = true. If not toggled, playing = false
  private final JButton begin;
  private final JTextField fps;
  private final JToggleButton loop;
  // INVARIANT: if toggled, looping = true. If not toggled, looping = false
  private final JButton forward;
  private final JButton back;
  private final JList<String> shapes = new JList<>();
  private final JButton editShape;
  private final JButton addRectangle;
  private final JButton addEllipse;
  private final JTextField addShapeName;
  private final JLabel tickLabel;
  private final JButton deleteShape;
  // INVARIANT: text is equal to the current tick

  private EditShapeDialogFactory editFactory;
  private EditShapeDialog editDialog; // if there is no dialog, this will be null.
  // the dialog is stored as a field to allow the view to call methods on it.

  public EditorView() {
    super(CAPTION);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    speed = 1;
    tick = Motion.START_TICK;
    looping = false;
    playing = false;

    play = new JToggleButton("play");
    begin = new JButton("begin");
    fps = new JTextField(3);
    loop = new JToggleButton("loop");
    forward = new JButton("->");
    back = new JButton("<-");
    editShape = new JButton("edit");
    addRectangle = new JButton("new rect");
    addEllipse = new JButton("new ellipse");
    addShapeName = new JTextField();
    tickLabel = new JLabel(Integer.toString(tick));
    deleteShape = new JButton("delete shape");

    timer = new Timer((int) Math.round((1000 / speed)), (ActionEvent e) -> {
      if (playing) {
        incrementTick();
      }
    });

    setLayout(new BorderLayout());

    JPanel playbackControlPanel = new JPanel();
    playbackControlPanel.setLayout(new FlowLayout());
    playbackControlPanel.add(tickLabel);
    playbackControlPanel.add(begin);
    playbackControlPanel.add(play);
    playbackControlPanel.add(loop);
    playbackControlPanel.add(back);
    playbackControlPanel.add(forward);
    playbackControlPanel.add(fps);

    add(playbackControlPanel, BorderLayout.PAGE_END);

    JPanel shapeEditor = new JPanel();
    shapeEditor.setLayout(new BoxLayout(shapeEditor, BoxLayout.Y_AXIS));
    shapeEditor.add(new JScrollPane(shapes));
    shapeEditor.add(editShape);
    shapeEditor.add(addShapeName);
    shapeEditor.add(addEllipse);
    shapeEditor.add(addRectangle);
    shapeEditor.add(deleteShape);
    shapes.setLayoutOrientation(JList.VERTICAL);

    add(shapeEditor, BorderLayout.EAST);
  }


  @Override
  public void setModel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    if (animationPanel != null) {
      throw new IllegalStateException("This view already has a model");
    }
    this.animationPanel = new AnimationPanel(model);
    add(animationPanel, BorderLayout.CENTER);
    updateMaxTick();
    drawCurrentTick();
    editFactory.setModel(model);

    setShapeList(model.getShapes());
  }

  // animate can be successfully called if setFeatures hasn't been called yet.
  // however, button presses won't do anything, and attempting to open a shape edit dialog
  // will cause an error.
  @Override
  public void animate() {
    if (animationPanel == null) {
      throw new IllegalStateException("The model has not been set");
    }
    resetTextFields();
    pack();

    // set the size to a constant so the buttons don't move with the changing label size
    tickLabel.setPreferredSize(new Dimension(4 * (int) tickLabel.getSize().getWidth(),
            (int) tickLabel.getSize().getHeight()));

    pack();

    this.setVisible(true);
    timer.start();
  }

  @Override
  public void drawCurrentTick() {
    animationPanel.paintTick(tick);
    tickLabel.setText(Integer.toString(tick));
  }

  @Override
  public void setSpeed(double speed) {
    if (speed <= 0) {
      throw new IllegalArgumentException("Speed must be positive");
    }
    this.speed = speed;
    timer.setDelay((int) Math.round(1000 / speed));
  }

  /**
   * Sets the location to which this animation should save.
   * @param output the Appendable that receives the save information.
   */
  @Override
  public void setOutput(Appendable output) {
    this.output = output;
  }

  @Override
  public void togglePlay() {
    playing = !playing;
    play.setSelected(playing);
  }

  @Override
  public void pause() {
    playing = false;
    play.setSelected(playing);
  }

  @Override
  public void rewind() {
    setTick(Motion.START_TICK);
  }

  @Override
  public void incrementTick() {
    int t = tick + 1;
    if (looping) {
      setTick(normalizeTick(t));
    }
    else {
      setTick(Math.min(t, maxTick));
    }
  }

  @Override
  public void decrementTick() {
    int t = tick - 1;
    if (looping) {
      setTick(normalizeTick(t));
    }
    else {
      setTick(Math.max(t, Motion.START_TICK));
    }
  }

  /**
   * Set the current tick to the given tick. Call this method whenever changing the tick.
   * This method enforces display invariants on the current tick:
   * it calls drawCurrentTick to update any relevant display components.
   * @param tick the new tick.
   */
  private void setTick(int tick) {
    this.tick = tick;
    drawCurrentTick();
  }

  /**
   * Return an out-of-bounds tick to within-range using modulus.
   * For example, if a tick is 1 before the minimum tick, it is corrected to the maximum tick.
   * If a tick is one above the maximum tick, it is corrected to the minimum tick.
   * If a tick is between the minimum and maximum ticks, it does not change.
   * @param tick the tick to be returned within-bounds
   * @return the corrected tick
   */
  private int normalizeTick(int tick) {
    int range = maxTick - Motion.START_TICK + 1;
    return ((((tick - Motion.START_TICK) % range) + range) % range) + Motion.START_TICK;
  }

  @Override
  public void toggleLoop() {
    looping = !looping;
    loop.setSelected(looping);
  }

  @Override
  public void updateMaxTick() {
    maxTick = animationPanel.getMaxTick();
  }

  @Override
  public void setShapeList(List<String> shapes) {
    this.shapes.setListData(shapes.toArray(new String[0]));
  }

  @Override
  public void enterShapeEditor(String shape) {
    if (shape == null) {
      throw new IllegalArgumentException("Shape cannot be null");
    }
    if (editDialog != null) {
      throw new IllegalStateException("The shape editor dialog is already open");
    }
    if (editFactory == null) {
      throw new IllegalStateException("The editor factory has not been set");
    }
    editDialog = editFactory.getDialog(shape, this);
    editDialog.setVisible(true);
  }

  @Override
  public void exitShapeEditor() {
    if (editDialog == null) {
      throw new IllegalStateException("The shape editor is already closed");
    }
    editDialog.dispose();
    editDialog = null;
  }

  @Override
  public void setNewFrameText(Motion m) {
    if (m == null) {
      throw new IllegalArgumentException("Motion must not be null");
    }
    if (editDialog == null) {
      throw new IllegalStateException("Shape editing dialog must be running to suggest keyframes");
    }
    editDialog.setNewFrameText(m);
  }

  @Override
  public void setEditFrameText(Motion m) {
    if (m == null) {
      throw new IllegalArgumentException("Motion must not be null");
    }
    if (editDialog == null) {
      throw new IllegalStateException("Shape editing dialog must be running to suggest keyframes");
    }
    editDialog.setEditFrameText(m);
  }

  /**
   * TODO: should we pass an Appendable as an argument, or should we use the output?
   */
  @Override
  public void save() {
    // TODO: implement this!
  }

  @Override
  public void displayErrorMessage(String message) {
    System.out.println(message);
    // TODO: this
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void resetTextFields() {
    fps.setText(Double.toString(speed));
  }

  @Override
  public void addFeatures(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Features must not be null");
    }
    // playback buttons
    play.addActionListener(evt -> features.togglePlay());
    loop.addActionListener(evt -> features.toggleLoop());
    begin.addActionListener(evt -> features.rewind());
    forward.addActionListener(evt -> features.stepForward());
    back.addActionListener(evt -> features.stepBackwards());

    // shape editing controls
    addRectangle.addActionListener(evt -> features.addShape(addShapeName.getText(), "rectangle"));
    addEllipse.addActionListener(evt -> features.addShape(addShapeName.getText(), "ellipse"));
    deleteShape.addActionListener(evt -> features.deleteShape(shapes.getSelectedValue()));

    // text fields
    fps.addActionListener(evt -> features.setSpeedToUserInput(fps.getText()));
    fps.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        // do nothing if the focus is gained
      }

      @Override
      public void focusLost(FocusEvent e) {
        features.resetTextFields();
      }
    });

    // shape editing buttons
    editShape.addActionListener(evt -> features.enterShapeEditor(shapes.getSelectedValue()));

    // Creating the EditShapeDialogFactory
    this.editFactory = new EditShapeDialogFactory(features);
  }
}
