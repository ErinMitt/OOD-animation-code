import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.model.Motion;
import cs3500.animator.model.Transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the class AnimationModelImpl.
 */
public class AnimationModelImplTest {
  AnimationModelImpl original;

  @Before
  public void init() {
    original = new AnimationModelImpl();
    original.addLayer("1");
  }

  @Test
  public void testAddLayer() {
    assertEquals("layer 1",
            original.displayAnimation());
    original.addLayer("2");
    assertEquals("layer 1\n" +
            "\n" +
            "layer 2", original.displayAnimation());
    try {
      original.addLayer("2");
      fail("Added two layers with the same name");
    } catch (IllegalArgumentException e) {
      assertEquals("There is already a layer by the name 2",
              e.getMessage());
    }
  }

  @Test
  public void testDeleteLayer() {
    assertEquals("layer 1",
            original.displayAnimation());
    try {
      original.deleteLayer("3");
      fail("removed a layer that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("There is no layer named 3", e.getMessage());
    }
    original.deleteLayer("1");
    assertEquals("", original.displayAnimation());
  }

  @Test
  public void testMoveLayer() {
    try {
      original.moveLayer("3", 0);
      fail("moved a layer that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("There is no layer named 3", e.getMessage());
    }

    try {
      original.moveLayer("1", -3);
      fail("moved a layer to a position that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("A layer's new position must fall within the existing list size",
              e.getMessage());
    }

    try {
      original.moveLayer("1", 3);
      fail("moved a layer to a position that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("A layer's new position must fall within the existing list size",
              e.getMessage());
    }

    original.addLayer("3");
    assertEquals("layer 1\n" +
            "\n" +
            "layer 3", original.displayAnimation());
    original.moveLayer("3", 0);
    assertEquals("layer 3\n" +
            "\n" +
            "layer 1", original.displayAnimation());
    original.moveLayer("3", 1);
    assertEquals("layer 1\n" +
            "\n" +
            "layer 3", original.displayAnimation());
  }

  @Test
  public void testGetLayers() {
    assertEquals(new ArrayList<>(Arrays.asList("1")), original.getLayers());
    original.addLayer("2");
    assertEquals(new ArrayList<>(Arrays.asList("1", "2")), original.getLayers());
    original.deleteLayer("1");
    assertEquals(new ArrayList<>(Arrays.asList("2")), original.getLayers());
  }

  @Test
  public void addEllipse() {
    assertEquals(new ArrayList<>(), original.getShapes("1"));
    original.addEllipse("1", "E");
    assertEquals(new ArrayList<>(Arrays.asList("E")), original.getShapes("1"));
  }

  @Test
  public void testAddEllipseForbidsRepeats() {
    original.addRectangle("1", "Hi");
    try {
      original.addEllipse("1", "Hi");
      fail("added an ellipse with a non-unique name");
    } catch (IllegalArgumentException e) {
      assertEquals("Every shape must have a unique name. " +
                      "The shape Hi already exists in the layer 1",
              e.getMessage());
    }
  }

  @Test
  public void addRectangle() {
    assertEquals(new ArrayList<>(), original.getShapes("1"));
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes("1"));
  }

  @Test
  public void testAddRectangleForbidsRepeats() {
    original.addRectangle("1", "Yo");
    try {
      original.addRectangle("1", "Yo");
      fail("added an ellipse with a non-unique name");
    } catch (IllegalArgumentException e) {
      assertEquals("Every shape must have a unique name. " +
                      "The shape Yo already exists in the layer 1",
              e.getMessage());
    }
  }

  @Test
  public void deleteShape() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes("1"));
    original.deleteShape("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes("1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteShapeThatDoesntExist() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes("1"));
    original.deleteShape("1", "Z");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes("1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteShapeThatWasAlreadyDeleted() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes("1"));
    original.deleteShape("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes("1"));
    original.deleteShape("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes("1"));
  }

  @Test
  public void addMotionAndChangeColor() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes("1"));
    assertEquals("layer 1\n" +
                    "shape R rectangle",
            original.displayAnimation());
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("1", "R", 7,100,100,40,
            50,0,255,0);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                   "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 100 40 50 0 255 0",
            original.displayAnimation());
  }

  @Test
  public void addMotionAndChangeSize() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes("1"));
    assertEquals("layer 1\n" +
                    "shape R rectangle",
            original.displayAnimation());
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("1", "R", 7,100,200,40,
            50,0,0,255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());
  }

  @Test
  public void addMotionAndChangeTwoThings() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes("1"));
    assertEquals("layer 1\n" +
                    "shape R rectangle",
            original.displayAnimation());
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                     "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("1", "R", 7,100,200,40,
            50,0,255,0);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
            "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 255 0",
            original.displayAnimation());
  }

  @Test
  public void addMotionAndChangeAllThings() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes("1"));
    assertEquals("layer 1\n" +
                    "shape R rectangle",
            original.displayAnimation());
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("1", "R", 7,100,200,40,
            100,0,255,0);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 100 0 255 0",
            original.displayAnimation());
  }

  @Test
  public void testCorrectRGB() {
    original.addEllipse("1", "3");
    original.addMotion("1", "3", 1, 2, 3, 4, 5, 280, 300, 270);
    assertEquals("layer 1\n" +
                    "shape 3 ellipse\n" +
                    "rotation 0\n" +
                    "motion 3 1 2 3 4 5 255 255 255    1 2 3 4 5 255 255 255",
            original.displayAnimation());
    original.deleteLastMotion("1", "3");

    original.addMotion("1", "3", 1, 2, 3, 4, 5, -10, -20, -30);
    assertEquals("layer 1\n" +
                    "shape 3 ellipse\n" +
                    "rotation 0\n" +
                    "motion 3 1 2 3 4 5 0 0 0    1 2 3 4 5 0 0 0",
            original.displayAnimation());
    original.deleteLastMotion("1", "3");
    original.addMotion("1", "3", 1, 2, 3, 4, 5, -5, 3, 270);
    assertEquals("layer 1\n" +
                    "shape 3 ellipse\n" +
                    "rotation 0\n" +
                    "motion 3 1 2 3 4 5 0 3 255    1 2 3 4 5 0 3 255",
            original.displayAnimation());
  }

  /*  @Test
  public void addIllegalMotion() {
    original.addRectangle("R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    try {
      original.addMotion("R", 5, 200, 200, 40,
              50, 0, 0, 255);
      fail("Added a motion that starts before the shape's last motion ends");
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot add a new motion into the middle of a sequence. " +
                      "New motions must occur after this shape's last existing motion.",
              e.getMessage());
    }
    // check that no motion was added
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());
  }*/

  @Test
  public void testAddMotionToNonexistentShape() {
    try {
      original.addMotion("1", "unreal", 1, 2, 3, 4, 5, 6, 7, 8);
      fail("added a motion to a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name unreal exists in the layer 1.",
              e.getMessage());
    }
    original.addRectangle("1", "R");
    try {
      original.addMotion("1", "unreal", 1, 2, 3, 4, 5, 6, 7, 8);
      fail("added a motion to a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name unreal exists in the layer 1.",
              e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void deleteMotionThatDoesntExist() {
    original.addRectangle("1", "R");
    original.deleteLastMotion("1", "R");
  }

  @Test
  public void testAddMotionAtNonpositiveTime() {
    original.addRectangle("1", "test");
    try {
      original.addMotion("1", "test", -5, 20, 20, 20, 20, 20, 20, 20);
      fail("added a motion at a negative time");
    } catch (IllegalArgumentException e) {
      assertEquals("Time must be a positive integer, given -5",
              e.getMessage());
    }
    // test no motion was added
    assertEquals("layer 1\n" +
            "shape test rectangle",
            original.displayAnimation());
  }

  @Test
  public void extend() {
    original.addRectangle("1", "R");
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("1", "R", 7, 100, 100, 40, 50, 0, 0, 255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
            "motion R 4 100 100 40 50 0 0 255    7 100 100 40 50 0 0 255",
            original.displayAnimation());
  }

  /*@Test
  public void testExtendMotionBackwardsInTime() {
    original.addRectangle("R");
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    try {
      original.extend("R", 2);
      fail("extended a shape's motion backwards in time.");
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot add a new motion into the middle of a sequence. " +
                      "New motions must occur after this shape's last existing motion.",
              e.getMessage());
    }
  }*/

  @Test
  public void deleteLastMotionUntilLessThanNoneExist() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes("1"));
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("1", "R", 7,100,200,40,
            50,0,0,255);
    original.addMotion("1", "R", 10,100,200,40,
            50,0,0,255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 7 100 200 40 50 0 0 255    10 100 200 40 50 0 0 255",
            original.displayAnimation());
    original.deleteLastMotion("1", "R");
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());
    original.deleteLastMotion("1", "R");
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());

  }

  @Test
  public void deleteLastMotion() {
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes("1"));
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("1", "R", 7,100,200,40,
            50,0,0,255);
    original.addMotion("1", "R", 10,100,200,40,
            50,0,0,255);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 7 100 200 40 50 0 0 255    10 100 200 40 50 0 0 255",
            original.displayAnimation());
    original.deleteLastMotion("1", "R");
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255\n" +
                    "rotation 0\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());

  }

  @Test
  public void testDeleteLastMotionFromAShapeWithNoMotions() {
    original.addEllipse("1", "E");
    original.addMotion("1", "E", 5, 2, 3, 4, 5, 6, 7, 8);
    assertEquals("layer 1\n" +
                    "shape E ellipse\n" +
                    "rotation 0\n" +
                    "motion E 5 2 3 4 5 6 7 8    5 2 3 4 5 6 7 8",
            original.displayAnimation());
    original.deleteLastMotion("1", "E");
    assertEquals("layer 1\n" +
                    "shape E ellipse",
            original.displayAnimation());
    try {
      original.deleteLastMotion("1", "E");
      fail("deleted a motion from a shape that has no motion");
    } catch (IllegalStateException e) {
      assertEquals("There are no motions to remove.",
              e.getMessage());
    }
  }

  @Test
  public void testDeleteMotionFromNonexistentShape() {
    try {
      original.deleteLastMotion("1", "S");
      fail("deleted the last motion from a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name S exists in the layer 1.",
              e.getMessage());
    }
  }

  @Test
  public void testDeleteNonexistentShape() {
    try {
      original.deleteShape("1", "M");
      fail("deleted a shape that never existed");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name M exists in the layer 1.",
              e.getMessage());
    }
    original.addEllipse("1", "E");
    original.deleteShape("1", "E");
    try {
      original.deleteShape("1", "E");
      fail("Deleted a shape twice");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name E exists in the layer 1.",
              e.getMessage());
    }
  }

  @Test
  public void getShapes() {
    assertEquals(new ArrayList<>(), original.getShapes("1"));
    original.addRectangle("1", "R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes("1"));
    original.addEllipse("1", "E");
    assertEquals(new ArrayList<>(Arrays.asList("R", "E")), original.getShapes("1"));
  }

  @Test
  public void testGetShapesDoesntAllowMutationOfShapes() {
    original.addEllipse("1", "a");
    original.addEllipse("1", "b");
    List<String> list1 = original.getShapes("1");
    list1.add("3");
    assertNotEquals(list1, original.getShapes("1"));
  }

  @Test
  public void testDisplayAnimation() {
    assertEquals("layer 1",
            original.displayAnimation());
    original.addRectangle("1", "R");
    assertEquals("layer 1\n" +
                    "shape R rectangle",
            original.displayAnimation());
    original.addMotion("1", "R", 1, 100, 100, 40, 50, 0, 0, 0);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    1 100 100 40 50 0 0 0",
            original.displayAnimation());
    original.addMotion("1", "R", 5, 100, 100, 40, 50, 0, 0, 0);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    1 100 100 40 50 0 0 0\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0",
            original.displayAnimation());

    original.addEllipse("1", "E");
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    1 100 100 40 50 0 0 0\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n" +
                    "shape E ellipse",
            original.displayAnimation());
    original.addMotion("1", "E", 2, 20, 10, 30, 40, 255, 34, 19);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    1 100 100 40 50 0 0 0\n" +
                     "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n" +
                    "shape E ellipse\n" +
                    "rotation 0\n" +
                    "motion E 2 20 10 30 40 255 34 19    2 20 10 30 40 255 34 19",
            original.displayAnimation());
    original.addMotion("1", "E", 4, 30, 20, 20, 30, 255, 34, 19);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    1 100 100 40 50 0 0 0\n" +
            "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n" +
                    "shape E ellipse\n" +
                    "rotation 0\n" +
            "motion E 2 20 10 30 40 255 34 19    2 20 10 30 40 255 34 19\n" +
                    "rotation 0\n" +
                    "motion E 2 20 10 30 40 255 34 19    4 30 20 20 30 255 34 19",
            original.displayAnimation());
    original.addMotion("1", "E", 10, 30, 20, 20, 30, 255, 34, 19);
    assertEquals("layer 1\n" +
                    "shape R rectangle\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    1 100 100 40 50 0 0 0\n" +
                    "rotation 0\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n" +
                    "shape E ellipse\n" +
                    "rotation 0\n" +
                    "motion E 2 20 10 30 40 255 34 19    2 20 10 30 40 255 34 19\n" +
                    "rotation 0\n" +
                    "motion E 2 20 10 30 40 255 34 19    4 30 20 20 30 255 34 19\n" +
                    "rotation 0\n" +
                    "motion E 4 30 20 20 30 255 34 19    10 30 20 20 30 255 34 19",
            original.displayAnimation());
  }

  @Test
  public void setCanvasTest() {
    original.addEllipse("1", "a");
    assertEquals(0, original.getX());
    assertEquals(0, original.getY());
    assertEquals(1, original.getWidth());
    assertEquals(1, original.getHeight());
    original.setBounds(50,50,50,50);
    assertEquals(50, original.getX());
    assertEquals(50, original.getY());
    assertEquals(50, original.getWidth());
    assertEquals(50, original.getHeight());
  }

  @Test (expected = IllegalArgumentException.class)
  public void declareShapeTestFails() {
    original.addEllipse("1", "a");
    original.setBounds(50,50,-50,50);
  }

  @Test (expected = IllegalArgumentException.class)
  public void declareShapeTestFailsHeight() {
    original.addEllipse("1", "a");
    original.setBounds(50,50,50,-50);
  }

  @Test (expected = IllegalArgumentException.class)
  public void declareShapeTestFailsRect() {
    original.addRectangle("1", "a");
    original.setBounds(50,50,-50,50);
  }

  @Test (expected = IllegalArgumentException.class)
  public void declareShapeTestFailsHeightRect() {
    original.addRectangle("1", "a");
    original.setBounds(50,50,50,-50);
  }

  @Test
  public void testGetTransformationAt() {
    original.addRectangle("1", "a");
    original.addMotion("1", "a", 1, 100, 100, 40, 50, 0, 0, 0);
    original.addMotion("1", "a", 3, 0, 0, 40, 50, 0, 0, 0);

    Transformation t = original.getTransformationAt("1", "a",1);
    Motion m = t.getStateAt(1);
    assertEquals(100, m.getX());

    Transformation t2 = original.getTransformationAt("1", "a", 2);
    Motion m2 = t2.getStateAt(2);
    assertEquals(50, m2.getX());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTransformationAtFails() {
    original.addRectangle("1", "a");
    original.addMotion("1", "a", 2, 100, 100,
            40, 50, 0, 0, 0);
    original.getTransformationAt("1", "a",1);
  }

  @Test
  public void testGetMotions() {
    original.addRectangle("1", "R");
    original.addRectangle("1", "S");
    original.addMotion("1", "R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("1", "R", 7,100,200,40,
            50,0,0,255);
    original.addMotion("1", "R", 10,100,200,40,
            50,0,0,255);
    assertEquals(3, original.getMotions("1", "R").size());
    assertEquals(0, original.getMotions("1", "S").size());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testGetMotionsFail() {
    original.addEllipse("1", "E");
    original.getMotions("1", "R");
  }

  @Test
  public void testGetShapeType() {
    original.addRectangle("1", "R");
    assertEquals("rectangle", original.getShapeType("1", "R"));
  }

  @Test
  public void testGetShapeTypeEclipse() {
    original.addEllipse("1", "E");
    assertEquals("ellipse", original.getShapeType("1", "E"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetShapeTypeFail() {
    original.getShapeType("1", "I");
  }

  @Test
  public void testBuilder() {
    AnimationModelImpl.Builder builder = new AnimationModelImpl.Builder();
    AnimationModel testModel = builder.build();
    assertTrue(testModel == builder.build());

    assertEquals("",
            testModel.displayAnimation());
    assertEquals(0, testModel.getX());
    assertEquals(0, testModel.getY());
    assertEquals(1, testModel.getWidth());
    assertEquals(1, testModel.getHeight());

    builder.setBounds(1, 1000, 100, 10);
    assertEquals(1, testModel.getX());
    assertEquals(1000, testModel.getY());
    assertEquals(100, testModel.getWidth());
    assertEquals(10, testModel.getHeight());

    builder.declareShape("A", "ellipse");
    assertEquals(new ArrayList<String>(Arrays.asList("A")), testModel.getShapes("layer1"));

    builder.declareShape("B", "rectangle");
    assertEquals(new ArrayList<String>(Arrays.asList("A", "B")), testModel.getShapes("layer1"));

    try {
      builder.declareShape("C", "triangle");
      fail("Added an invalid shape type");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid shape type triangle", e.getMessage());
    }

    builder.addMotion("A", 1, 2, 3, 4, 5, 6, 7, 8,
            2, 3, 4, 5, 6, 7, 8, 9);
    assertEquals("layer layer1\n" +
                    "shape A ellipse\n" +
                    "rotation 0\n" +
                    "motion A 2 3 4 5 6 7 8 9    2 3 4 5 6 7 8 9\n" +
                    "shape B rectangle",
            testModel.displayAnimation());

    builder.addMotion("A", 2, 3, 4, 5, 6, 7, 8, 9,
            4, 4, 4, 4, 4, 4, 4, 4);
    assertEquals("layer layer1\n" +
                    "shape A ellipse\n" +
                    "rotation 0\n" +
                    "motion A 2 3 4 5 6 7 8 9    2 3 4 5 6 7 8 9\n" +
                    "rotation 0\n" +
                    "motion A 2 3 4 5 6 7 8 9    4 4 4 4 4 4 4 4\n" +
                    "shape B rectangle",
            testModel.displayAnimation());

    builder.addKeyframe("B", 2, 2, 2, 2, 2, 2, 2, 2);
    assertEquals("layer layer1\n" +
                    "shape A ellipse\n" +
                    "rotation 0\n" +
                    "motion A 2 3 4 5 6 7 8 9    2 3 4 5 6 7 8 9\n" +
                    "rotation 0\n" +
                    "motion A 2 3 4 5 6 7 8 9    4 4 4 4 4 4 4 4\n" +
                    "shape B rectangle\n" +
                    "rotation 0\n" +
                    "motion B 2 2 2 2 2 2 2 2    2 2 2 2 2 2 2 2",
            testModel.displayAnimation());

    builder.addKeyframe("B", 3, 4, 5, 6, 6, 5, 4, 3);
    assertEquals("layer layer1\n" +
                    "shape A ellipse\n" +
                    "rotation 0\n" +
                    "motion A 2 3 4 5 6 7 8 9    2 3 4 5 6 7 8 9\n" +
                    "rotation 0\n" +
                    "motion A 2 3 4 5 6 7 8 9    4 4 4 4 4 4 4 4\n" +
                    "shape B rectangle\n" +
                    "rotation 0\n" +
                    "motion B 2 2 2 2 2 2 2 2    2 2 2 2 2 2 2 2\n" +
                    "rotation 0\n" +
                    "motion B 2 2 2 2 2 2 2 2    3 4 5 6 6 5 4 3",
            testModel.displayAnimation());
  }
}