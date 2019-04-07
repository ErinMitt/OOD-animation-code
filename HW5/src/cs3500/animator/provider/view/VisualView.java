package cs3500.animator.provider.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class represents the visual view for our animation.
 */

public class VisualView extends JFrame implements IView {

  private Map<String, Shape> shapes;
  private AnimationPanel panel;
  private boolean looping = true;
  private List<Boolean> activeShapes;

  /**
   * Constructor that initializes the JPanel of the view.
   *
   * @param w width of the canvas
   * @param h height of the canvas
   * @param s Map consisting of shapes and their names
   */

  public VisualView(int w, int h, Map<String, Shape> s) {
    super("Animation View");
    this.shapes = s;
    SwingUtilities.isEventDispatchThread();
    setLayout(new BorderLayout());
    setSize(w, h);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  void setActiveShapes(List<Boolean> activeS) {
    this.activeShapes = activeS;
  }

  /**
   * Builds the visual view.
   *
   * @param output output file
   * @throws UnsupportedOperationException if the operation is unsupported
   */

  @Override
  public void buildView(String output) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Never gonna happen!");
  }

  @Override
  public boolean[] getLooping() {
    boolean[] status = new boolean[1];
    status[0] = looping;
    return status;
  }

  @Override
  public void updateShapes(Map<String, Shape> s) {
    shapes = s;
  }

  @Override
  public Boolean update(int time, boolean firstTime, boolean firstCycle) {
    if (!firstTime || !firstCycle) {
      looping = panel.looping;
    }
    List<ShapeType> types = new ArrayList<>();
    List<int[]> deets = new ArrayList<>();
    int[] details;
    int[] detHolder;
    int counter = 0;
    for (Entry<String, Shape> entry : shapes.entrySet()) {
      Shape holder = entry.getValue();
      SortedMap<Integer, int[]> actionMap = holder.getMoves();
      detHolder = byChangeType(time, actionMap);
      if (activeShapes != null) {
        if (activeShapes.get(counter)) {
          details = new int[8];
        } else {
          details = new int[7];
        }
      } else {
        details = new int[7];
      }

      if (detHolder != null) {
        System.arraycopy(detHolder, 0, details, 0, 7);
        types.add(holder.getShapeType());
        deets.add(details);
      }
      counter++;
    }
    if (deets.size() != 0 && firstTime && firstCycle) {
      panel = new AnimationPanel(deets, types);
      add(panel, BorderLayout.CENTER);
      setVisible(true);
    } else if (deets.size() != 0 && firstTime) {
      remove(panel);
      panel = new AnimationPanel(deets, types);
      add(panel, BorderLayout.CENTER);
      setVisible(true);
    } else if (deets.size() != 0) {
      panel.shapeTypeList = types;
      panel.detailList = deets;
      panel.repaint();
      repaint();
    } else {
      return true;
    }
    return false;
  }

  /**
   * Sorts all actions by their change type.
   *
   * @param time      time
   * @param changeMap Map consisting of actions
   * @return null
   */

  @Override
  public int[] byChangeType(int time, SortedMap<Integer, int[]> changeMap) {
    int[] nextDetails;
    int[] lastDetails = null;
    int lastTime = 0;
    int nextTime;
    boolean firstTime = true;
    if (time < changeMap.firstKey()) {
      return null;
    } else if (changeMap.containsKey(time)) {
      return changeMap.get(time);
    }
    for (Entry<Integer, int[]> pair : changeMap.entrySet()) {
      nextDetails = pair.getValue();
      nextTime = pair.getKey();
      if (time > lastTime && time < nextTime && !firstTime) {
        return compute(lastTime, nextTime, time, lastDetails, nextDetails);
      }
      lastDetails = pair.getValue();
      lastTime = pair.getKey();
      firstTime = false;
    }
    return null;
  }

  /**
   * Gets the tempo.
   *
   * @return 0
   */
  @Override
  public int getTempo() {
    return 0;
  }

  /**
   * Applies twinning formula.
   *
   * @param timeA    from formula
   * @param timeB    from formula
   * @param time     from formula
   * @param detailsA from formula
   * @param detailsB from formula
   * @return array of ints
   */
  private int[] compute(double timeA, double timeB, double time, int[] detailsA, int[] detailsB) {
    double tilEnd = timeB - time;
    double fromStart = time - timeA;
    double window = timeB - timeA;
    double detailA;
    double detailB;
    double holder;
    int[] currentDetail = new int[7];
    for (int i = 0; i < 7; i++) {
      detailA = detailsA[i];
      detailB = detailsB[i];
      if (detailA == detailB) {
        currentDetail[i] = (int) Math.round(detailA);
      } else {
        holder = detailA * (tilEnd / window) + detailB * (fromStart / window);
        currentDetail[i] = (int) Math.round(holder);
      }
    }
    return currentDetail;
  }

}