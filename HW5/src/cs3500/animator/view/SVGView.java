package cs3500.animator.view;

import cs3500.animator.model.ReadOnlyModel;

public class SVGView implements AnimationView {
  private ReadOnlyModel model;

  @Override
  public void setModel(ReadOnlyModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    if (this.model != null) {
      throw new IllegalStateException("This view already has a model");
    }
    this.model = model;
  }

  @Override
  public void animate() {

  }
}
