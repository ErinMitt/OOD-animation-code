package cs3500.animator.provider.view;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.SortedMap;

/**
 * Interface for all of the views.
 */

public interface IView {

  /**
   * Builds the view.
   *
   * @param output output file
   * @throws FileNotFoundException        if the output file is not found
   * @throws UnsupportedEncodingException if the encoding is unsupported
   */

  void buildView(String output) throws FileNotFoundException, UnsupportedEncodingException;

  boolean[] getLooping();

  void updateShapes(Map<String, Shape> s);

  Boolean update(int time, boolean firstTime, boolean firstCycle);

  int[] byChangeType(int time, SortedMap<Integer, int[]> changeMap);

  int getTempo();
}
