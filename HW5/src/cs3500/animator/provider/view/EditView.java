package cs3500.animator.provider.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The view that allows user to edit the animation. All the design details are described in README
 * file attached.
 */

public class EditView implements ActionListener, IView, ItemListener {

  private VisualView innerView;
  private boolean pause = false;
  private JButton addFrameButton;
  private Map<String, Shape> shapes;
  protected int time = 0;
  private List<Boolean> activeShapes;
  private List<String> shapeNames;
  private int[] details = null;
  private boolean restart = false;
  private boolean firstT = true;
  private boolean firstC = true;
  private int tempo;

  /**
   * Initializes the view with the width, height, speed of the animation and the list of strings.
   *
   * @param w      Width of the canvas
   * @param h      Height of the canvas
   * @param tempo1 the speed of the animation in ticks per second
   * @param s      list of shapes stored as a map of Strings and Shapes where String is the shape
   *               name
   */
  public EditView(int w, int h, int tempo1, Map<String, Shape> s) {
    this.tempo = tempo1;
    activeShapes = new ArrayList<>();
    shapeNames = new ArrayList<>();
    this.shapes = s;
    innerView = new VisualView(w, h, s);
    JButton pauseButton = new JButton("Pause");
    pauseButton.setMnemonic(KeyEvent.VK_P);
    pauseButton.setActionCommand("pause");
    pauseButton.addActionListener(this);
    JButton playButton = new JButton("Play");
    playButton.setMnemonic(KeyEvent.VK_D);
    playButton.setActionCommand("play");
    playButton.addActionListener(this);
    JButton restartButton = new JButton("Restart");
    restartButton.setMnemonic(KeyEvent.VK_R);
    restartButton.setActionCommand("restart");
    restartButton.addActionListener(this);
    addFrameButton = new JButton("Add Frame");
    addFrameButton.setMnemonic(KeyEvent.VK_A);
    addFrameButton.setActionCommand("add");
    addFrameButton.addActionListener(this);
    addFrameButton.setEnabled(false);
    JTextField text = new JTextField(20);
    JPanel textPanel1 = new JPanel();
    text.setActionCommand("tempo");
    text.addActionListener(this);
    textPanel1.add(text);
    JButton deleteButton = new JButton("Delete last");
    deleteButton.setMnemonic(KeyEvent.VK_D);
    deleteButton.setActionCommand("delete");
    deleteButton.addActionListener(this);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(0, 7));
    buttonPanel.add(deleteButton);
    buttonPanel.add(restartButton);
    buttonPanel.add(pauseButton);
    buttonPanel.add(playButton);
    buttonPanel.add(addFrameButton);
    buttonPanel.add(text);
    JLabel label = new JLabel("Set Tempo");
    buttonPanel.add(label);
    innerView.add(buttonPanel, BorderLayout.NORTH);
    JPanel checkPanel = new JPanel(new GridLayout(shapes.size(), 1));
    int i = 0;
    for (Entry<String, Shape> pair : shapes.entrySet()) {
      activeShapes.add(false);
      shapeNames.add(pair.getKey());
      if (shapes.size() <= 20) {
        JCheckBox shapeCheckBox = new JCheckBox(pair.getKey());
        shapeCheckBox.setMnemonic(KeyEvent.VK_S);
        shapeCheckBox.setSelected(false);
        shapeCheckBox.addItemListener(this);
        shapeCheckBox.setName("" + i);
        checkPanel.add(shapeCheckBox);
      }
      i++;
    }
    if (shapes.size() <= 20) {
      JCheckBox shapeCheckBox = new JCheckBox("Select all");
      shapeCheckBox.setMnemonic(KeyEvent.VK_A);
      shapeCheckBox.setSelected(false);
      shapeCheckBox.addItemListener(this);
      shapeCheckBox.setName("21");
      checkPanel.add(shapeCheckBox);
    }

    innerView.add(checkPanel, BorderLayout.EAST);
    innerView.setVisible(true);
  }

  /**
   * does nothings here.
   *
   * @param output output file
   * @throws UnsupportedOperationException because the method here is redundant
   */
  @Override
  public void buildView(String output) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Can't do that!");
  }

  /**
   * Checks the looping status of the animation.
   *
   * @return if the animation is looped.
   */
  @Override
  public boolean[] getLooping() {
    boolean[] status = new boolean[2];
    status[0] = innerView.getLooping()[0];
    status[1] = pause;
    return status;
  }

  /**
   * does nothings here.
   *
   * @param s Shapes
   * @throws UnsupportedOperationException because the method here is redundant
   */
  @Override
  public void updateShapes(Map<String, Shape> s) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Can't do that!");
  }

  /**
   * Updates the view.
   *
   * @param t          time
   * @param firstTime  checks if this is the first keyframe
   * @param firstCycle checks if this is the first cycle of the animation
   * @return if this is the first time.
   */
  @Override
  public Boolean update(int t, boolean firstTime, boolean firstCycle) {
    firstC = firstCycle;
    if (restart) {
      restart = false;
      return null;
    }
    innerView.updateShapes(shapes);
    time = t;
    firstT = innerView.update(time, firstTime, firstCycle);
    return firstT;
  }

  /**
   * does nothing here.
   *
   * @param time      time
   * @param changeMap list of keyframes
   * @return dummy array
   */
  @Override
  public int[] byChangeType(int time, SortedMap<Integer, int[]> changeMap) {
    return new int[0];
  }

  @Override
  public int getTempo() {
    return tempo;
  }

  /**
   * Extended method from actionlistener.
   *
   * @param e event
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    JPanel textPanel = null;
    switch (e.getActionCommand()) {
      case "pause":
        pause = true;
        addFrameButton.setEnabled(true);
        break;
      case "play":
        pause = false;
        addFrameButton.setEnabled(false);
        break;
      case "add": {
        String name;
        for (int i = 0; i < activeShapes.size(); i++) {
          if (activeShapes.get(i)) {
            name = shapeNames.get(i);
            if (shapes.get(name).getMoves().containsKey(time)) {
              details = shapes.get(name).getMoves().get(time);
            } else {
              details = innerView.byChangeType(time, shapes.get(name).getMoves());
            }
            shapes.get(name).getMoves().put(time, details);
          }
        }
        JTextField text = new JTextField(20);
        textPanel = new JPanel();
        text.setActionCommand("data");
        text.addActionListener(this);
        JLabel label = new JLabel("Format(enter x for variable to not change): x y w h r g b");
        textPanel.add(label);
        textPanel.add(text);
        innerView.add(textPanel, BorderLayout.SOUTH);
        innerView.setVisible(true);
        break;
      }
      case "data":
        JTextField textHolder = (JTextField) e.getSource();
        String newDetailStr = textHolder.getText();
        String[] numbers = newDetailStr.split(" ");
        int[] newDetails = new int[numbers.length];
        int counter = 0;
        for (Entry<String, Shape> pair : shapes.entrySet()) {
          if (activeShapes.get(counter)) {
            String name = pair.getKey();
            for (int c = 0; c < numbers.length; c++) {
              if (numbers[c].equals("x")) {
                newDetails[c] = details[c];
              } else {
                newDetails[c] = Integer.parseInt(numbers[c]);
              }
            }
            for (Entry<Integer, int[]> pair1 : shapes.get(name).getMoves().entrySet()) {
              if (pair1.getKey() == time) {
                pair1.setValue(newDetails);
              }
            }
          }
          counter++;
        }
        innerView.remove(textPanel);
        innerView.setVisible(true);
        break;
      case "restart":
        restart = true;
        break;
      case "tempo":
        JTextField textHolder1 = (JTextField) e.getSource();
        tempo = Integer.parseInt(textHolder1.getText());
        break;
      case "delete": {
        String name;
        int lastTime = time;
        boolean notMatch;
        for (int i = 0; i < activeShapes.size(); i++) {
          name = shapeNames.get(i);
          notMatch = true;
          if (!shapes.get(name).getMoves().isEmpty()) {
            if (activeShapes.get(i) && time >= shapes.get(name).getMoves().firstKey()) {
              while (notMatch) {
                if (shapes.get(name).getMoves().containsKey(lastTime)) {
                  shapes.get(name).getMoves().remove(lastTime);
                  notMatch = false;
                } else {
                  lastTime--;
                }
              }
            }
          }
        }
        innerView.updateShapes(shapes);
        break;
      }
    }
  }

  /**
   * Changes the checkboxes in the window.
   *
   * @param e ItemEvent.
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    JCheckBox holder = (JCheckBox) e.getSource();
    int index = Integer.parseInt(holder.getName());
    if (index != 21) {
      if (e.getStateChange() == ItemEvent.DESELECTED) {
        activeShapes.set(index, false);
      }
      if (e.getStateChange() == ItemEvent.SELECTED) {
        activeShapes.set(index, true);
      }
    } else if (e.getStateChange() == ItemEvent.SELECTED) {
      for (int i = 0; i < activeShapes.size(); i++) {
        activeShapes.set(i, true);
      }
    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
      for (int i = 0; i < activeShapes.size(); i++) {
        activeShapes.set(i, false);
      }
    }
    innerView.setActiveShapes(activeShapes);
    innerView.update(time, firstT, firstC);
  }
}
