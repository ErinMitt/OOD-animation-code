package cs3500.animator;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.view.EditorAnimationView;
import cs3500.animator.view.EditorView;

public class SanityCheck {
  public static void main(String[] args) {
    AnimationModel model = new AnimationModelImpl();
    model.setBounds(0, 0, 100, 100);
    model.addLayer("background");
    model.addRectangle("background", "blue");
    model.addMotion("background", "blue", 0, 0, 0, 200, 200, 200, 200, 255);
    model.addMotion("background", "blue", 30, 0, 0, 200, 200, 200, 200, 255);

    model.addLayer("1");
    model.addEllipse("1", "E");
    model.addMotion("1", "E", 1, 10, 10, 20, 10, 100, 100, 255);
    model.addMotion("1", "E", 10, 10, 10, 20, 10, 100, 100, 255, 180);
    model.addMotion("1", "E", 26, 70, 10, 20, 40, 100, 100, 255, 180);
    model.addMotion("1", "E", 40, 70, 70, 20, 40, 100, 100, 255, -180);

    model.addRectangle("1", "R");
    model.addMotion("1", "R", 0, 70, 70, 5, 30, 200, 200, 0);
    model.addMotion("1", "R", 30, 50, 50, 20, 4, 0, 200, 200, 90);

    EditorAnimationView view = new EditorView();
    view.setSpeed(10);
    new AnimationController(model, view).gogo();
  }
}
