package cs3500.animator;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.SVGView;
import cs3500.animator.view.VisualView;

public final class Excellence {
  public static void main(String[] args) {
    AnimationModel model = new AnimationModelImpl();
    model.setBounds(100, 100, 500, 500);
    model.addEllipse("E");
    model.addMotion("E", 1, 0, 0, 10, 10, 0, 0, 255);
    model.addMotion("E", 100, 200, 200, 20, 40, 255, 255, 0);
    model.addRectangle("R");
    model.addMotion("R", 5, 200, 0, 50, 100, 0, 0, 0);
    model.addMotion("R", 95, 20, 80, 100, 10, 0, 0, 0);

    /*
    AnimationView view = new VisualView();
    ((VisualView) view).setSpeed(25);
    view.setModel(model);
    view.animate();
     */

    ///*
    Appendable out = new StringBuilder();
    AnimationView view = new SVGView();
    ((SVGView) view).setSpeed(25);
    view.setModel(model);
    ((SVGView) view).setOutput(out);
    view.animate();
    System.out.println(out.toString());
    // */
  }
}