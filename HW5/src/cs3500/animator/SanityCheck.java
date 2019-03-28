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
    model.addEllipse("E");
    model.addMotion("E", 1, 1, 1, 20, 20, 100, 100, 255);
    model.addMotion("E", 10, 1, 1, 40, 60, 255, 100, 100);
    model.addMotion("E", 26, 70, 70, 20, 20, 100, 100, 255);

    model.addRectangle("R");
    model.addMotion("R", 0, 70, 70, 5, 30, 200, 200, 0);
    model.addMotion("R", 30, 50, 50, 20, 4, 0, 200, 200);

    EditorAnimationView view = new EditorView();
    view.setSpeed(2);
    new AnimationController(model, view).gogo();
  }
}
