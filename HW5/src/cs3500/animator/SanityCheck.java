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
    EditorAnimationView view = new EditorView();
    view.setModel(model);
    view.setSpeed(2);
    new AnimationController(model, view);
    view.animate();
  }
}
