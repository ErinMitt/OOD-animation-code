import org.junit.Test;

import cs3500.animator.model.Motion;
import cs3500.animator.model.Transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the class Transformation.
 */
public class TransformationTests {
  private Motion m1 = new Motion(1, 1, 1, 1, 1, 1, 1, 1);
  private Motion m2 = new Motion(5, 5, 5, 5, 5, 5, 5, 5);
  private Motion m3 = new Motion(1, 4, 4, 4, 4, 4, 4, 4);
  private Motion m4 = new Motion(8, 4, 4, 4, 4, 4, 4, 4);

  @Test
  public void testConstructor() {
    new Transformation(m1, m2);
    new Transformation(m1, m3);

    try {
      new Transformation(m2, m1);
      fail("Made a transformation with a start time after its end time");
    } catch (IllegalArgumentException e) {
      assertEquals("The time at the start must not come after the time at the end",
              e.getMessage());
    }
  }

  @Test
  public void testGetStateAt() {
    // get the state at a time in the middle of a transformation
    Transformation t1 = new Transformation(m1, m2);
    Motion s2 = t1.getStateAt(2);
    assertEquals(2,
            s2.getTime());
    assertEquals(2,
            s2.getX());
    assertEquals(2,
            s2.getY());
    assertEquals(2,
            s2.getWidth());
    assertEquals(2,
            s2.getHeight());
    assertEquals(2,
            s2.getRed());
    assertEquals(2,
            s2.getGreen());
    assertEquals(2,
            s2.getBlue());

    // if the start and end happen at the same time, choose the start
    Transformation t2 = new Transformation(m1, m3);
    Motion s3 = t2.getStateAt(1);
    assertEquals(1,
            s3.getTime());
    assertEquals(1,
            s3.getX());
    assertEquals(1,
            s3.getY());
    assertEquals(1,
            s3.getWidth());
    assertEquals(1,
            s3.getHeight());
    assertEquals(1,
            s3.getRed());
    assertEquals(1,
            s3.getGreen());
    assertEquals(1,
            s3.getBlue());

    // test that asking for times outside the boundaries throws an exception
    Transformation t3 = new Transformation(m2, m4);
    try {
      t3.getStateAt(2);
      fail("Got the state from before the transformation began");
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot calculate state outside of the transformation's boundaries",
              e.getMessage());
    }

    try {
      t3.getStateAt(10);
      fail("Got the state from after the transformation ended");
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot calculate state outside of the transformation's boundaries",
              e.getMessage());
    }
  }
}
