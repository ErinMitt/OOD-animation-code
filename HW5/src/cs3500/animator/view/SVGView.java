package cs3500.animator.view;

import cs3500.animator.model.ReadOnlyModel;

public class SVGView implements AnimationView {
  private ReadOnlyModel model;

  @Override
  public void setModel(ReadOnlyModel model) {
    this.model = model;
  }

  @Override
  public void animate() {

  }
}
