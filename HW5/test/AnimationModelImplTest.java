import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class AnimationModelImplTest {
  AnimationModelImpl original;

  @Before
  public void init() {
    original = new AnimationModelImpl();
  }
  @Test
  public void addEllipse() {
    original.addEllipse("E");
    assertEquals(new ArrayList<String>(Arrays.asList("E")), original.getShapes());
  }

  @Test
  public void addRectangle() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
  }

  @Test
  public void deleteShape() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.deleteShape("R");
    assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes());
  }

  @Test
  public void addMotion() {
    original.addRectangle("R");
    assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    assertEquals("shape R rectangle\n" +
            "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());
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
  public void getShapes() {
    original.addRectangle("R");
    original.addEllipse("E");
    assertEquals(new ArrayList<String>(Arrays.asList("R", "E")), original.getShapes());
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
}