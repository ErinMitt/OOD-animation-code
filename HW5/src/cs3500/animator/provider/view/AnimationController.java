package cs3500.animator.provider.view;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class representing the controller of the animation model.
 */

public class AnimationController {
  private final IModel model;
  private final IView view;
  private final String viewT;
  private final String outFile;
  private int tempo;
  private int maxTime;

  /**
   * Constructs a controller initializes the model, view, output file and tempo.
   *
   * @param m        model
   * @param v        view
   * @param viewType represents the type of the view (text, edit, svg or visual)
   * @param outFile1 output file
   * @param tempo1   tempo of the animation
   */

  public AnimationController(IModel m, IView v, String viewType, String outFile1, int tempo1) {
    this.model = m;
    this.view = v;
    this.viewT = viewType;
    this.outFile = outFile1;
    this.tempo = tempo1;
  }

  /**
   * Runs the animation if there is any. Sets the timer.
   */

  public void run() {
    if (viewT.equalsIgnoreCase("visual") || viewT.equals("edit")) {
      findMaxTime();
      Timer time = new Timer();
      Task task = new Task();
      time.schedule(task, 0, 1000 / tempo);
    } else {
      try {
        view.buildView(outFile);
      } catch (FileNotFoundException | NullPointerException | UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Finds the key frame with the maximum time.
   */

  private void findMaxTime() {
    SortedMap<Integer, int[]> moves;
    for (Entry<String, Shape> entry : model.getShapes().entrySet()) {
      moves = entry.getValue().getMoves();
      if (moves.lastKey() > maxTime) {
        maxTime = moves.lastKey();
      }
    }
  }

  /**
   * Class representing the tasks for the timer.
   */

  public class Task extends TimerTask {

    int ticks = 0;
    Boolean firstTime = true;
    boolean firstCycle = true;
    boolean looping = true;
    boolean pause = false;

    @Override
    public void run() {
      tempo = view.getTempo();
      if (viewT.equals("edit")) {
        pause = view.getLooping()[1];
      }
      if (!pause) {
        firstTime = view.update(ticks, firstTime, firstCycle);
        if (firstTime == null) {
          ticks = 0;
          firstTime = view.update(ticks, true, false);
          firstTime = true;
          firstCycle = false;
        }
        looping = view.getLooping()[0];
        if (looping) {
          if (ticks == maxTime) {
            ticks = -1;
            firstTime = true;
            firstCycle = false;
          }
        }
        if (ticks < maxTime) {
          ticks++;
        }
      }
    }
  }
}
