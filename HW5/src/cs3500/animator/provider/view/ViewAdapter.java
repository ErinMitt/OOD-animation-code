package cs3500.animator.provider.view;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.ReadOnlyModel;
import cs3500.animator.view.AnimationView;

/**
 * A class that adapts from the provider's EditView to our EditorAnimationView interface
 * to let out controller use it properly.
 */
public class ViewAdapter implements AnimationView {
  private IView view;
  private AnimationController controller;

  @Override
  public void setModel(ReadOnlyModel model) {
    if (this.view != null) {
      throw new IllegalStateException("The model has already been set");
    }
    // We have to use casting here because in our code the view only receives an
    // immutable ReadOnlyModel, but in the provider code the view mutates the model.
    // Therefore the method in our code only expects a ReadOnlyModel,
    // while their code requires a mutable AnimationModel.
    IModel pModel = new ModelAdapter((AnimationModel) model);
    this.view = new EditView(model.getWidth(), model.getHeight(), 1, pModel.getShapes());
    this.controller = new AnimationController(pModel, this.view, "edit", null, 1);
  }

  @Override
  public void animate() {
    if (this.controller == null) {
      throw new IllegalStateException("Must set a model before animating");
    }
    this.controller.run();
  }

  @Override
  public void setSpeed(double speed) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setOutput(Appendable output) {
    throw new UnsupportedOperationException();
  }
}
