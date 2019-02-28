import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AnimationModelImplTest {
  @Before
  public void init() {

  }
  @Test
  public void addEllipse() {
    AnimationModelImpl original = new AnimationModelImpl();
    original.addEllipse("E");
    Assert.assertEquals(new ArrayList<String>(Arrays.asList("E")), original.getShapes());
  }

  @Test
  public void addRectangle() {
    AnimationModelImpl original = new AnimationModelImpl();
    original.addRectangle("R");
    Assert.assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
  }

  @Test
  public void deleteShape() {
    AnimationModelImpl original = new AnimationModelImpl();
    original.addRectangle("R");
    Assert.assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.deleteShape("R");
    Assert.assertEquals(new ArrayList<String>(Arrays.asList()), original.getShapes());
  }

  @Test
  public void addMotion() {
    AnimationModelImpl original = new AnimationModelImpl();
    original.addRectangle("R");
    Assert.assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    Assert.assertEquals("shape R rectangle\n" +
            "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());
  }

  @Test
  public void extend() {
    AnimationModelImpl original = new AnimationModelImpl();
    original.addRectangle("R");
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.extend("R", 7);
    Assert.assertEquals("shape R rectangle\n" +
            "motion R 4 100 100 40 50 0 0 255    7 100 100 40 50 0 0 255",
            original.displayAnimation());
  }

  @Test
  public void deleteLastMotion() {
    AnimationModelImpl original = new AnimationModelImpl();
    original.addRectangle("R");
    Assert.assertEquals(new ArrayList<String>(Arrays.asList("R")), original.getShapes());
    original.addMotion("R", 4,100,100,40,
            50,0,0,255);
    original.addMotion("R", 7,100,200,40,
            50,0,0,255);
    original.addMotion("R", 10,100,200,40,
            50,0,0,255);
    Assert.assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255\n" +
                    "motion R 7 100 200 40 50 0 0 255    10 100 200 40 50 0 0 255",
            original.displayAnimation());
    original.deleteLastMotion("R");
    Assert.assertEquals("shape R rectangle\n" +
                    "motion R 4 100 100 40 50 0 0 255    7 100 200 40 50 0 0 255",
            original.displayAnimation());

  }

  @Test
  public void getShapes() {
    AnimationModelImpl original = new AnimationModelImpl();
    original.addRectangle("R");
    original.addEllipse("E");
    Assert.assertEquals(new ArrayList<String>(Arrays.asList("R", "E")), original.getShapes());
  }
  
}