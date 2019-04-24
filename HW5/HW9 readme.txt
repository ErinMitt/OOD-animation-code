Level 1 Scrubbing:
This required adding methods to the Features interface and methods/fields to the
EditorView class and interface.

Level 2 Rotation:
For this change, we had to add a new constructor to the Motion class,
add a method to the Transformation class,
and change AnimationPanel, SVGView, and TextView to show rotations in graphics.
We also added the method declareRotation to the AnimationBuilder
to let it interpret the new file with rotation information.

Level 3 Layers:
This required the most extensive changes. We added a new class "Layer" and delegated
most of the responsibilities of the original model here (including keeping track of Shapes).
For the model, we added both a map of Layers (for easy access by name)
and a list (to keep track of their order).
The model, EditorView, and Features (controller) required several new methods
to add, move, and delete Layers.
To interpret layer information from .txt files,
we added the method declareLayer to the AnimationBuilder.