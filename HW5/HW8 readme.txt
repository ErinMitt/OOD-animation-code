Features that work:
The new hybrid view can play/pause, loop, add new keyframes, edit existing keyframes,
   delete keyframes, restart the animation from the beginning.
(Occasionally we get weird bugs when removing keyframes,
   but we're pretty sure it's because the project is not threadsafe
   ie ConcurrentModificationExceptionExceptions.)

Features that do not work:
The provider view doesn't have any way to add or delete shapes,
   and can't add new keyframes after the last existing keyframe.
The view also can't change the speed once the animation begins.
This is because the speed is determined by a timer in the controller,
   but the controller has no permanent reference to the timer and as a result
   can't change its delay.
This was fixable by rewriting the controller, but we chose not to edit it for reasons
   explained in point 2 of the questionable design choices section.


Explaining some questionable design choices:
   1) Why we implemented built-in Java interfaces in the Shape class:
   The provider view interacted with the model by getting its fields and calling methods on them
      rather than through methods on the model itself.
   For example, to edit a keyframe, it got the required Shape from a map provided upon the
      view's construction, called a getter to get the shape's TreeMap of motions,
      then found the keyframe at the tick it wanted and set its value to the new keyframe params.
   Because of this, we had to write adapters that helped our model act like the
      built-in interfaces used by the provider code.

   2) Why we included the provider controller in the view adapter:
   The provider's controller included some relatively complicated logic,
      especially the TimerTask class,
      that we couldn't have easily replicated if we hadn't received the controller as well.
   Because of this, we chose not to copy-and-paste code from there into a new controller,
      and instead decided to use the existing controller as-is and package it into the view adapter.
   Also, the TimerTask class did several view-like jobs
      such as determining the next tick based on whether the "looping" button was selected
      and deciding whether to progress the animation based on whether the view was currently paused.
   Thanks to this ambiguity about controller/view responsibilities, we thought it was appropriate
      to treat the controller as if it were functionally part of the view.
   One of the assignment requirements was to avoid altering provider view code,
      so we also made sure not to make any changes to the controller.

   3) Why we use casting in the view adapter:
   When designing our code, we decided that the view will never directly mutate the model
      and therefore only needs access to a ReadOnlyModel (super-interface of AnimationModel).
   The provider code breaks that limitation and requires mutating the model.
   Therefore, it wasn't enough to give just a ReadOnlyModel:
      to mutate the model, the model adapter needed a mutable AnimationModel.
   Our setModel method, which the view adapter inherited from the interface AnimationView,
      takes a ReadOnlyModel.
   The provider code is a special case that needs to receive an AnimationModel
      from a method that only takes ReadOnlyModels.