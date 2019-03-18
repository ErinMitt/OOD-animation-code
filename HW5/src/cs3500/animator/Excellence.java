package cs3500.animator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimationView;

/**
 * A class that can run the program through command line arguments.
 */
public final class Excellence {
  /**
   * Generate an animation from a given file through the command line.
   * The arguments must be one of:
   * -in [the input file name]
   * -out [the output file name]
   * -speed [the speed of the animation in ticks per second]
   * -view [the type of view]
   * -speed must receive a positive int larger than zero,
   * and -view must be one of visual, svg, or text.
   * The arguments -in and -view are required, while -out will default to System.out
   * and -speed will default to 1 if not otherwise specified.
   * If an argument is invalid, System.out will display a message explaining why.
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    int speed = 1;
    Appendable output = System.out;
    FileReader input = null;
    AnimationView view = null;

    for (int i = 0; i < args.length - 1; i += 2) {
      switch (args[i]) {
        case "-in":
          try {
            input = new FileReader(new File(args[i + 1]));
          } catch (FileNotFoundException e) {
            showErrorMessage("No file by the name " + args[i + 1] + " was found");
            return;
          }
          break;
        case "-out":
          try {
            output = new FileWriter(new File(args[i + 1]));
          } catch (IOException e) {
            showErrorMessage("No file by the name " + args[i + 1] + " was found");
            return;
          }
          break;
        case "-view":
          try {
            view = ViewFactory.buildView(args[i + 1]);
          } catch (IllegalArgumentException e) {
            showErrorMessage(args[i + 1] + " is not a valid view type");
            return;
          }
          break;
        case "-speed":
          try {
            if (Integer.parseInt(args[i + 1]) > 0) {
              speed = Integer.parseInt(args[i + 1]);
            } else {
              showErrorMessage("Speed must be a positive integer, given " + args[i + 1]);
              return;
            }
          } catch (NumberFormatException e) {
            showErrorMessage("Speed must be an integer");
            return;
          }
          break;
        default:
          showErrorMessage("Invalid command " + args[i]);
          return;
      }
    }

    if (view == null || input == null) {
      showErrorMessage("Please specify a valid view type and input file");
      return;
    }

    view.setSpeed(speed);
    view.setOutput(output);
    try {
      view.setModel(AnimationReader.parseFile(input, new AnimationModelImpl.Builder()));
    } catch (IllegalStateException e) {
      showErrorMessage("Unable to read file, returned error message: " + e.getMessage());
      return;
    }
    view.animate();

    if (output instanceof FileWriter) {
      try {
        ((FileWriter) output).close();
      } catch (IOException e) {
        showErrorMessage("Unable to close output file");
      }
    }
  }

  private static void showErrorMessage(String message) {
    System.out.println(message);
  }
}