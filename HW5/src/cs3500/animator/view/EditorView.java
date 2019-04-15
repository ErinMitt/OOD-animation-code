package cs3500.animator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

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
  private final JButton deleteShape;
  private final JLabel tickLabel;
  // INVARIANT: text is equal to the current tick
  private final JTextField saveFileName;
  private final JButton saveSVG;
  private final JButton saveText;
  private final JButton load;
  private final JTextField loadInfo;
  private final JLabel errorDisplay;

  private EditShapeDialogFactory editFactory;
  private EditShapeDialog editDialog; // if there is no dialog, this will be null.
  // the dialog is stored as a field to allow the view to call methods on it.

  /**
   * Create an EditorView.
   */
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
    deleteShape = new JButton("delete shape");
    tickLabel = new JLabel(Integer.toString(tick));
    saveFileName = new JTextField(10);
    saveSVG = new JButton("save to SVG");
    saveText = new JButton("save to text");
    load = new JButton("load");
    loadInfo = new JTextField(10);
    errorDisplay = new JLabel("No errors yet :)");

    timer = new Timer((int) Math.round((1000 / speed)), (ActionEvent e) -> {
      if (playing) {
        incrementTick();
      }
    });

    setLayout(new BorderLayout());

    JPanel bottomScreen = new JPanel();
    bottomScreen.setLayout(new BoxLayout(bottomScreen, BoxLayout.Y_AXIS));

    JPanel playbackControlPanel = new JPanel();
    playbackControlPanel.setLayout(new FlowLayout());
    playbackControlPanel.add(tickLabel);
    playbackControlPanel.add(begin);
    playbackControlPanel.add(play);
    playbackControlPanel.add(loop);
    playbackControlPanel.add(back);
    playbackControlPanel.add(forward);
    playbackControlPanel.add(fps);

    bottomScreen.add(playbackControlPanel);
    bottomScreen.add(errorDisplay);

    add(bottomScreen, BorderLayout.PAGE_END);

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

    JPanel saveBar = new JPanel();
    saveBar.setLayout(new FlowLayout());
    saveBar.add(saveFileName);
    saveBar.add(saveSVG);
    saveBar.add(saveText);
    saveBar.add(loadInfo);
    saveBar.add(load);

    add(saveBar, BorderLayout.PAGE_START);
  }

  /**
   * Set the animation model to be animated.
   * This method requires a previous call to addFeatures.
   */
  @Override
  public void setModel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    if (animationPanel != null) {
      throw new IllegalStateException("This view already has a model");
    }
    if (editFactory == null) {
      throw new IllegalStateException("Must set features before setting the model");
    }
    this.animationPanel = new AnimationPanel(model);
    add(animationPanel, BorderLayout.CENTER);
    updateMaxTick();
    drawCurrentTick();
    editFactory.setModel(model);
    setShapeList(model.getShapes());
  }

  @Override
  public void animate() {
    if (animationPanel == null) {
      throw new IllegalStateException("The model has not been set");
    }
    resetTextFields();
    pack();

    // set the size to a constant so the buttons don't move with the changing label size
    tickLabel.setPreferredSize(new Dimension(5 * (int) tickLabel.getSize().getWidth(),
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
    if (output == null) {
      throw new IllegalArgumentException("Output must not be null");
    }
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
    editDialog.makeVisible();
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

  @Override
  public void save(String text) {
    if (text == null) {
      throw new IllegalStateException("Output cannot be null");
    }
    try {
      output.append(text);
    } catch (IOException e) {
      throw new IllegalStateException("Could not write to output");
    }
  }

  @Override
  public void displayErrorMessage(String message) {
    errorDisplay.setText(message);
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

    // saving controls
    saveSVG.addActionListener(evt -> features.save("svg", saveFileName.getText()));
    saveText.addActionListener(evt -> features.save("text", saveFileName.getText()));
    load.addActionListener(evt -> features.load(loadInfo.getText()));

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
