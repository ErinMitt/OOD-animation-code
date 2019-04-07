package cs3500.animator.provider.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * Renders the animation inside the window using JPanel.
 */

class AnimationPanel extends JPanel implements ItemListener {

  List<int[]> detailList;
  List<ShapeType> shapeTypeList;
  boolean looping = true;

  /**
   * Constructor initializes list of details and list of shape types.
   *
   * @param detailL list of array of ints of details
   * @param shapeT  list of shape types
   */
  AnimationPanel(List<int[]> detailL, List<ShapeType> shapeT) {
    super();
    detailList = detailL;
    shapeTypeList = shapeT;

    JCheckBox loopCheckBox = new JCheckBox("Loop");
    loopCheckBox.setMnemonic(KeyEvent.VK_L);
    loopCheckBox.setSelected(true);
    loopCheckBox.addItemListener(this);
    JPanel checkPanel = new JPanel(new GridLayout(0, 1));
    checkPanel.add(loopCheckBox);
    add(checkPanel, BorderLayout.LINE_START);
  }

  /**
   * Iterates through the list of shapes of the view, and paints each of them, currently supports
   * only Ellipses and Rectangles.
   *
   * @param g graphics
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    int[] details;
    for (int i = 0; i < detailList.size(); i++) {
      details = detailList.get(i);
      Color color = new Color(details[4], details[5], details[6]);
      g.setColor(color);
      switch (shapeTypeList.get(i)) {
        case RECTANGLE:
          if (details.length == 7) {
            g.fillRect(details[0], details[1], details[2], details[3]);
          } else {
            g.drawRect(details[0], details[1], details[2], details[3]);
          }
          break;
        case ELLIPSE:
          if (details.length == 7) {
            g.fillOval(details[0], details[1], details[2], details[3]);
          } else {
            g.drawOval(details[0], details[1], details[2], details[3]);
          }
          break;
        default:
          //more cases could be made for more shapes.
          break;
      }

    }
  }


  /**
   * Changes the state of the item. Deals with the checkboxes.
   *
   * @param e item event that is changed
   */
  @Override
  public void itemStateChanged(ItemEvent e) {

    if (e.getStateChange() == ItemEvent.DESELECTED) {
      looping = false;
    }
    if (e.getStateChange() == ItemEvent.SELECTED) {
      looping = true;
    }
  }
}