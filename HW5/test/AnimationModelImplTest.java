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
  }

  @Test
  public void addEllipse() {
    assertEquals(new ArrayList<>(), original.getShapes());
    original.addEllipse("E");
    assertEquals(new ArrayList<>(Arrays.asList("E")), original.getShapes());
  }

  @Test
  public void testAddEllipseForbidsRepeats() {
    original.addRectangle("Hi");
    try {
      original.addEllipse("Hi");
      fail("added an ellipse with a non-unique name");
    } catch (IllegalArgumentException e) {
      assertEquals("Every shape must have a unique name. The shape Hi already exists.",
              e.getMessage());
    }
  }

  @Test
  public void addRectangle() {
    assertEquals(new ArrayList<>(), original.getShapes());
    original.addRectangle("R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes());
  }

  @Test
  public void testAddRectangleForbidsRepeats() {
    original.addRectangle("Yo");
    try {
      original.addRectangle("Yo");
      fail("added an ellipse with a non-unique name");
    } catch (IllegalArgumentException e) {
      assertEquals("Every shape must have a unique name. The shape Yo already exists.",
              e.getMessage());
    }
  }

  @Test
  public void deleteShape() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.deleteShape("R");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteShapeThatDoesntExist() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.deleteShape("Z");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteShapeThatWasAlreadyDeleted() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.deleteShape("R");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes());
    original.deleteShape("R");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes());
  }

  @Test
  public void addMotionAndChangeColor() {
    original.addRectangle("R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes());
    assertEquals("shape R rectangle",
            original.displayAnimation());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("R", 7,100,100,40,
            50,0,255,0);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 100 40 50 0 255 0",
            original.displayAnimation());
  }

  @Test
  public void addMotionAndChangeSize() {
    original.addRectangle("R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes());
    assertEquals("shape R rectangle",
            original.displayAnimation());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());
  }

  @Test
  public void addMotionAndChangeTwoThings() {
    original.addRectangle("R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes());
    assertEquals("shape R rectangle",
            original.displayAnimation());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("R", 7,100,200,40,
            50,0,255,0);
    assertEquals("shape R rectangle\n" +
            "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 255 0",
            original.displayAnimation());
  }

  @Test
  public void addMotionAndChangeAllThings() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    assertEquals("shape R rectangle",
            original.displayAnimation());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());
    original.addMotion("R", 7,100,200,40,
            100,0,255,0);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 100 0 255 0",
            original.displayAnimation());
  }

  @Test
  public void testCorrectRGB() {
    original.addEllipse("3");
    original.addMotion("3", 1, 2, 3, 4, 5, 280, 300, 270);
    assertEquals("shape 3 ellipse\n" +
                    "motion 3 1 2 3 4 5 255 255 255    1 2 3 4 5 255 255 255",
            original.displayAnimation());
    original.deleteLastMotion("3");

    original.addMotion("3", 1, 2, 3, 4, 5, -10, -20, -30);
    assertEquals("shape 3 ellipse\n" +
                    "motion 3 1 2 3 4 5 0 0 0    1 2 3 4 5 0 0 0",
            original.displayAnimation());
    original.deleteLastMotion("3");
    original.addMotion("3", 1, 2, 3, 4, 5, -5, 3, 270);
    assertEquals("shape 3 ellipse\n" +
                    "motion 3 1 2 3 4 5 0 3 255    1 2 3 4 5 0 3 255",
            original.displayAnimation());
  }

  @Test
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
  }

  @Test
  public void testAddMotionToNonexistentShape() {
    try {
      original.addMotion("unreal", 1, 2, 3, 4, 5, 6, 7, 8);
      fail("added a motion to a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name unreal exists.",
              e.getMessage());
    }
    original.addRectangle("R");
    try {
      original.addMotion("unreal", 1, 2, 3, 4, 5, 6, 7, 8);
      fail("added a motion to a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name unreal exists.",
              e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void deleteMotionThatDoesntExist() {
    original.addRectangle("R");
    original.deleteLastMotion("R");
  }

  @Test
  public void testAddMotionAtNonpositiveTime() {
    original.addRectangle("test");
    try {
      original.addMotion("test", -5, 20, 20, 20, 20, 20, 20, 20);
      fail("added a motion at a negative time");
    } catch (IllegalArgumentException e) {
      assertEquals("Time must be a positive integer, given -5",
              e.getMessage());
    }
    // test no motion was added
    assertEquals("shape test rectangle", original.displayAnimation());
    try {
      original.addMotion("test", 0, 20, 20, 20, 20, 20, 20, 20);
      fail("added a motion at time 0");
    } catch (IllegalArgumentException e) {
      assertEquals("Time must be a positive integer, given 0",
              e.getMessage());
    }
    assertEquals("shape test rectangle", original.displayAnimation());
  }

  @Test
  public void extend() {
    original.addRectangle("R");
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.extend("R", 7);
    assertEquals("shape R rectangle\n" +
            "motion R 4 100 100 40 50 0 0 255    7 100 100 40 50 0 0 255",
            original.displayAnimation());
  }

  @Test
  public void testExtendNonexistentShape() {
    try {
      original.addMotion("R", 4, 100, 100, 40,
              50, 0, 0, 255);
      fail("extended a motion for a shape that does not exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name R exists.",
              e.getMessage());
    }
  }

  @Test
  public void testExtendShapeWithNoMotion() {
    original.addRectangle("E");
    try {
      original.extend("E", 5);
      fail("extended a shape's nonexistent motion.");
    } catch (IllegalStateException e) {
      assertEquals("A shape with no motions cannot have its last motion extended.",
              e.getMessage());
    }
  }

  @Test
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
  }

  @Test
  public void deleteLastMotionUntilLessThanNoneExist() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    original.addMotion("R", 10,100,200,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255\n" +
                    "motion R 7 100 200 40 50 0 0 255    10 100 200 40 50 0 0 255",
            original.displayAnimation());
    original.deleteLastMotion("R");
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());
    original.deleteLastMotion("R");
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    4 100 100 40 50 0 0 255",
            original.displayAnimation());

  }

  @Test
  public void deleteLastMotion() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    original.addMotion("R", 10,100,200,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255\n" +
                    "motion R 7 100 200 40 50 0 0 255    10 100 200 40 50 0 0 255",
            original.displayAnimation());
    original.deleteLastMotion("R");
    assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());

  }

  @Test
  public void testDeleteLastMotionFromAShapeWithNoMotions() {
    original.addEllipse("E");
    original.addMotion("E", 5, 2, 3, 4, 5, 6, 7, 8);
    assertEquals("shape E ellipse\n" +
                    "motion E 5 2 3 4 5 6 7 8    5 2 3 4 5 6 7 8",
            original.displayAnimation());
    original.deleteLastMotion("E");
    assertEquals("shape E ellipse",
            original.displayAnimation());
    try {
      original.deleteLastMotion("E");
      fail("deleted a motion from a shape that has no motion");
    } catch (IllegalStateException e) {
      assertEquals("There are no motions to remove.",
              e.getMessage());
    }
  }

  @Test
  public void testDeleteMotionFromNonexistentShape() {
    try {
      original.deleteLastMotion("S");
      fail("deleted the last motion from a shape that doesn't exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name S exists.",
              e.getMessage());
    }
  }

  @Test
  public void testDeleteNonexistentShape() {
    try {
      original.deleteShape("M");
      fail("deleted a shape that never existed");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name M exists.",
              e.getMessage());
    }
    original.addEllipse("E");
    original.deleteShape("E");
    try {
      original.deleteShape("E");
      fail("Deleted a shape twice");
    } catch (IllegalArgumentException e) {
      assertEquals("No shape with the name E exists.",
              e.getMessage());
    }
  }

  @Test
  public void getShapes() {
    assertEquals(new ArrayList<>(), original.getShapes());
    original.addRectangle("R");
    assertEquals(new ArrayList<>(Arrays.asList("R")), original.getShapes());
    original.addEllipse("E");
    assertEquals(new ArrayList<String>(Arrays.asList("R", "E")), original.getShapes());
  }

  @Test
  public void testGetShapesDoesntAllowMutationOfShapes() {
    original.addEllipse("a");
    original.addEllipse("b");
    List<String> list1 = original.getShapes();
    list1.add("3");
    assertNotEquals(list1, original.getShapes());
  }

  @Test
  public void testDisplayAnimation() {
    assertEquals("",
            original.displayAnimation());
    original.addRectangle("R");
    assertEquals("shape R rectangle",
            original.displayAnimation());
    original.addMotion("R", 1, 100, 100, 40, 50, 0, 0, 0);
    assertEquals("shape R rectangle\n" +
                    "motion R 1 100 100 40 50 0 0 0    1 100 100 40 50 0 0 0",
            original.displayAnimation());
    original.extend("R", 5);
    assertEquals("shape R rectangle\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0",
            original.displayAnimation());

    original.addEllipse("E");
    assertEquals("shape R rectangle\n" +
            "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n\n" +
            "shape E ellipse",
            original.displayAnimation());
    original.addMotion("E", 2, 20, 10, 30, 40, 255, 34, 19);
    assertEquals("shape R rectangle\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n\n" +
                    "shape E ellipse\n" +
                    "motion E 2 20 10 30 40 255 34 19    2 20 10 30 40 255 34 19",
            original.displayAnimation());
    original.addMotion("E", 4, 30, 20, 20, 30, 255, 34, 19);
    assertEquals("shape R rectangle\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n\n" +
                    "shape E ellipse\n" +
                    "motion E 2 20 10 30 40 255 34 19    4 30 20 20 30 255 34 19",
            original.displayAnimation());
    original.extend("E", 10);
    assertEquals("shape R rectangle\n" +
                    "motion R 1 100 100 40 50 0 0 0    5 100 100 40 50 0 0 0\n\n" +
                    "shape E ellipse\n" +
                    "motion E 2 20 10 30 40 255 34 19    4 30 20 20 30 255 34 19\n" +
                    "motion E 4 30 20 20 30 255 34 19    10 30 20 20 30 255 34 19",
            original.displayAnimation());
  }

  @Test
  public void setCanvasTest() {
    original.addEllipse("a");
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
    original.addEllipse("a");
    original.setBounds(50,50,-50,50);
  }

  @Test (expected = IllegalArgumentException.class)
  public void declareShapeTestFailsHeight() {
    original.addEllipse("a");
    original.setBounds(50,50,50,-50);
  }

  @Test
  public void declareShapeTestRect() {
    original.addRectangle("b");
    original.setBounds(50,50,50,50);
  }

  @Test (expected = IllegalArgumentException.class)
  public void declareShapeTestFailsRect() {
    original.addRectangle("a");
    original.setBounds(50,50,-50,50);
  }

  @Test (expected = IllegalArgumentException.class)
  public void declareShapeTestFailsHeightRect() {
    original.addRectangle("a");
    original.setBounds(50,50,50,-50);
  }

  @Test
  public void testGetTransformationAt() {
    original.addRectangle("a");
    original.addMotion("a", 1, 100, 100, 40, 50, 0, 0, 0);
    original.addMotion("a", 3, 0, 0, 40, 50, 0, 0, 0);

    Transformation t = original.getTransformationAt("a",1);
    Motion m = t.getStateAt(1);
    assertEquals(100, m.getX());

    Transformation t2 = original.getTransformationAt("a", 2);
    Motion m2 = t2.getStateAt(2);
    assertEquals(50, m2.getX());
  }
  @Test(expected = IllegalArgumentException.class)
  public void testGetTransformationAtFails() {
    original.addRectangle("a");
    original.addMotion("a", 2, 100, 100,
            40, 50, 0, 0, 0);
    original.getTransformationAt("a",1);
  }

  @Test
  public void testGetMotions() {
    original.addRectangle("R");
    original.addRectangle("S");
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    original.addMotion("R", 10,100,200,40,
            50,0,0,255);
    assertEquals(3, original.getMotions("R").size());
    assertEquals(0, original.getMotions("S").size());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testGetMotionsFail() {
    original.addEllipse("E");
    original.getMotions("R");
  }

  @Test
  public void testGetShapeType() {
    original.addRectangle("R");
    assertEquals("rectangle", original.getShapeType("R"));
  }

  @Test
  public void testGetShapeTypeEclipse() {
    original.addEllipse("E");
    assertEquals("ellipse", original.getShapeType("E"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetShapeTypeFail() {
    original.getShapeType("I");
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
    assertEquals(new ArrayList<String>(Arrays.asList("A")), testModel.getShapes());

    builder.declareShape("B", "rectangle");
    assertEquals(new ArrayList<String>(Arrays.asList("A", "B")), testModel.getShapes());

    try {
      builder.declareShape("C", "triangle");
      fail("Added an invalid shape type");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid shape type triangle", e.getMessage());
    }

    builder.addMotion("A", 1, 2, 3, 4, 5, 6, 7, 8,
            2, 3, 4, 5, 6, 7, 8, 9);
    assertEquals("shape A ellipse\n" +
                    "motion A 2 3 4 5 6 7 8 9    2 3 4 5 6 7 8 9\n" +
                    "\n" +
                    "shape B rectangle",
            testModel.displayAnimation());

    builder.addMotion("A", 2, 3, 4, 5, 6, 7, 8, 9,
            4, 4, 4, 4, 4, 4, 4, 4);
    assertEquals("shape A ellipse\n" +
                    "motion A 2 3 4 5 6 7 8 9    4 4 4 4 4 4 4 4\n" +
                    "\n" +
                    "shape B rectangle",
            testModel.displayAnimation());

    builder.addKeyframe("B", 2, 2, 2, 2, 2, 2, 2, 2);
    assertEquals("shape A ellipse\n" +
                    "motion A 2 3 4 5 6 7 8 9    4 4 4 4 4 4 4 4\n" +
                    "\n" +
                    "shape B rectangle\n" +
                    "motion B 2 2 2 2 2 2 2 2    2 2 2 2 2 2 2 2",
            testModel.displayAnimation());

    builder.addKeyframe("B", 3, 4, 5, 6, 6, 5, 4, 3);
    assertEquals("shape A ellipse\n" +
                    "motion A 2 3 4 5 6 7 8 9    4 4 4 4 4 4 4 4\n" +
                    "\n" +
                    "shape B rectangle\n" +
                    "motion B 2 2 2 2 2 2 2 2    3 4 5 6 6 5 4 3",
            testModel.displayAnimation());
  }
}