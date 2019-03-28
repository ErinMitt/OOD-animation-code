package cs3500.animator.controller;

/**
 * This class represents all of the functionality that an EditorAnimationView requires.
 */
public interface Features {
  /**
   * Play the animation if paused, pause the animation if playing.
   */
  void togglePlay();

  /**
   * Loop the animation if it's not looping, unloop the animation if it's looping.
   */
  void toggleLoop();

  /**
   * Rewind the animation to the start.
   */
  void rewind();

  /**
   * Move one frame forward.
   */
  void stepForward();

  /**
   * Move one frame backward.
   */
  void stepBackwards();

  /**
   * Set the animation's speed to the given speed.
   * @param input A String representing the desired speed.
   */
  void setSpeedToUserInput(String input);

  void resetTextFields();

  /**
   * Enter a screen from which a shape's Motions can be edited.
   * @param shape the shape to be edited
   */
  void enterShapeEditor(String shape);

  /**
   * Return to the default screen from the shape editor screen.
   */
  void exitShapeEditor();

  /**
   * Add a keyframe to the given shape with the given parameters.
   * @param shape the shape name
   * @param time the time
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the width
   * @param height the height
   * @param red the r component of the color
   * @param green the g component of the color
   * @param blue the b component of the color
   */
  void addKeyframe(String shape, String time, String x, String y,
                   String width, String height, String red, String green, String blue);

  /**
   * Edit an existing keyframe according to the given parameters.
   * @param shape the shape name
   * @param time the time
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the width
   * @param height the height
   * @param red the r component of the color
   * @param green the g component of the color
   * @param blue the b component of the color
   */
  void editKeyframe(String shape, String time, String x, String y,
                    String width, String height, String red, String green, String blue);

  /**
   * Delete the keyframe of the specified shape at the given time.
   * @param shape the shape
   * @param time the time of the keyframe
   */
  void removeKeyframe(String shape, String time);

  /**
   * If the user attempts to add a keyframe, autofill the most likely motion parameters.
   * If the keyframe occurs between two other, suggest the in-between.
   * If the keyframe happens before the first or after the last existing keyframe,
   * suggest the nearest one.
   * @param shape the shape whose keyframes are being edited
   * @param time the time of the new keyframe
   */
  void suggestNewKeyframe(String shape, String time);

  /**
   * If the user attempts to edit a keyframe, autofill the current motion parameters.
   * @param shape the shape whose keyframes are being edited
   * @param time the time of the edited keyframe
   */
  void suggestEditKeyframe(String shape, String time);

  /**
   * Add a shape by the given name of the given type to the model's list of shapes.
   * @param name the shape's name
   * @param type the shape's type
   */
  void addShape(String name, String type);

  /**
   * Delete a shape from the model and all of it's keyframes by deleting the given shape name.
   * @param name the shape's name
   */
  void deleteShape(String name);

  /**
   * Save the model's output in the given file type in a file by the given name.
   * @param type format to save in: either .svg or .txt
   */
  void save(String type, String fileName);

  /**
   * Load a file from the source folder by file name.
   * @param fileName the name of the file in the source folder.
   */
  void load(String fileName);
}
