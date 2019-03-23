package cs3500.animator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
  private int maxTick; // TODO: this!
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
  private final JList<JLabel> frames = new JList<>(); // TODO: add MouseListener to labels in here
  private final JList<JLabel> shapes = new JList<>(); // TODO: add MouseListener to labels in here

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

    timer = new Timer((int) Math.round((1000 / speed)), (ActionEvent e) -> {
      if (playing) {
        incrementTick();
      }
    });

    setLayout(new BorderLayout());

    JPanel playbackControlPanel = new JPanel();
    playbackControlPanel.setLayout(new FlowLayout());
    playbackControlPanel.add(begin);
    playbackControlPanel.add(play);
    playbackControlPanel.add(loop);
    playbackControlPanel.add(back);
    playbackControlPanel.add(forward);
    playbackControlPanel.add(fps);

    add(playbackControlPanel, BorderLayout.PAGE_END);
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
    maxTick = animationPanel.getMaxTick();
    animationPanel.paintTick(tick);
  }

  @Override
  public void animate() {
    if (animationPanel == null) {
      throw new IllegalStateException("The model has not been set");
    }
    resetTextFields();
    pack();

    this.setVisible(true);
    timer.start();

    // TODO: finish some setup stuff
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
    tick = Motion.START_TICK;
    animationPanel.paintTick(tick);
  }

  // TODO: fix this to work with any start tick instead of just 0
  @Override
  public void incrementTick() {
    tick += 1;
    if (looping) {
      tick = tick % (maxTick + 1);
    }
    else {
      tick = Math.min(tick, maxTick);
    }
    animationPanel.paintTick(tick);
  }

  @Override
  public void decrementTick() {
    tick -= 1;
    if (looping) {
      tick = (((tick % (maxTick + 1)) + maxTick + 1) % (maxTick + 1));
    }
    else {
      tick = Math.max(tick, Motion.START_TICK);
    }
    animationPanel.paintTick(tick);
  }

  @Override
  public void toggleLoop() {
    looping = !looping;
    loop.setSelected(looping);
    rewind();
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
    // buttons
    play.addActionListener(evt -> features.togglePlay());
    loop.addActionListener(evt -> features.toggleLoop());
    begin.addActionListener(evt -> features.rewind());
    forward.addActionListener(evt -> features.stepForward());
    back.addActionListener(evt -> features.stepBackwards());

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
  }
}
