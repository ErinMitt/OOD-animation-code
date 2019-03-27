import org.junit.Test;

import cs3500.animator.model.Motion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the class Motion.
 */
public class MotionTests {
  private Motion m = new Motion(1, 2, 3, 4, 5, 6, 7, 8);

  @Test
  public void testConstructor() {
    // all valid
    Motion m1 = new Motion(1, 1, 1, 1, 1, 1, 1, 1);
    assertEquals(1, m1.getTime());
    assertEquals(1, m1.getX());
    assertEquals(1, m1.getY());
    assertEquals(1, m1.getWidth());
    assertEquals(1, m1.getHeight());
    assertEquals(1, m1.getRed());
    assertEquals(1, m1.getGreen());
    assertEquals(1, m1.getBlue());

    // width and height <= 0
    try {
      new Motion(2, 5, 6, 0, 2, 3, 4, 5);
      fail("Made a motion with a zero width");
    } catch (IllegalArgumentException e) {
      assertEquals("Width and height must be positive nonzero integers, given 0 & 2",
              e.getMessage());
    }

    try {
      new Motion(2, 5, 6, -5, 2, 3, 4, 5);
      fail("Made a motion with a negative width");
    } catch (IllegalArgumentException e) {
      assertEquals("Width and height must be positive nonzero integers, given -5 & 2",
              e.getMessage());
    }

    try {
      new Motion(2, 5, 6, 7, 0, 3, 4, 5);
      fail("Made a motion with a zero height");
    } catch (IllegalArgumentException e) {
      assertEquals("Width and height must be positive nonzero integers, given 7 & 0",
              e.getMessage());
    }

    try {
      new Motion(2, 5, 6, 8, -4, 3, 4, 5);
      fail("Made a motion with a negative height");
    } catch (IllegalArgumentException e) {
      assertEquals("Width and height must be positive nonzero integers, given 8 & -4",
              e.getMessage());
    }

    try {
      new Motion(-2, 1, 2, 3, 4, 5, 6, 7);
      fail("Made a motion with a negative time");
    } catch (IllegalArgumentException e) {
      assertEquals("Time must be a positive integer, given -2",
              e.getMessage());
    }

    // regularize rgb
    Motion m2 = new Motion(1, 2, 3, 4, 5, -40, -20, -1);
    assertEquals(0, m2.getRed());
    assertEquals(0, m2.getBlue());
    assertEquals(0, m2.getGreen());

    Motion m3 = new Motion(1, 2, 3, 4, 5, 300, 400, 500);
    assertEquals(255, m3.getRed());
    assertEquals(255, m3.getBlue());
    assertEquals(255, m3.getGreen());
  }

  @Test
  public void testDisplay() {
    assertEquals("1 2 3 4 5 6 7 8",
            m.display());
  }
}
