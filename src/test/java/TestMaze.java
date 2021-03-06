import static org.testng.Assert.fail;

import java.util.concurrent.ThreadLocalRandom;

import org.testng.Assert;
import org.testng.annotations.Test;

import edu.illinois.cs.cs125.lib.mazemaker.Maze;
import edu.illinois.cs.cs125.lib.mazemaker.Maze.Location;
import edu.illinois.cs.cs125.lib.mazemaker.Maze.LocationException;

/**
 * Test the maze class.
 */
public class TestMaze {

    /**
     * Test that we can create a maze.
     */
    @Test
    public void testMazeCreation() {

        for (int i = 0; i < 10; i++) {
            int randomX = ThreadLocalRandom.current().nextInt(80, 100);
            int randomY = ThreadLocalRandom.current().nextInt(80, 100);
            Maze maze = new Maze(randomX, randomY);
            Assert.assertNotNull(maze);
        }
    }

    /**
     * Test bad maze dimensions.
     */
    @Test
    public void testBadDimensions() {
        Maze maze = null;
        try {
            maze = new Maze(-1, 10);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertNull(maze);
        }
        try {
            maze = new Maze(10, -1);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertNull(maze);
        }
        try {
            maze = new Maze(1, 1);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertNull(maze);
        }
    }

    /**
     * Test basic location setting and getting.
     *
     * @throws LocationException the location exception
     */
    @Test
    public void testBasicLocations() throws LocationException {

        Assert.assertTrue(new Location(0, 1).equals(new Location(0, 1)));
        for (int i = 0; i < 10; i++) {
            int randomX = ThreadLocalRandom.current().nextInt(80, 100);
            int randomY = ThreadLocalRandom.current().nextInt(80, 100);

            Maze maze = new Maze(randomX, randomY);

            for (int j = 0; j < 10; j++) {

                // Start at zero
                maze.startAtZero();
                Assert.assertEquals(maze.getCurrentLocation(), new Location(0, 0));

                // Start at random given locations
                int randomLocationX = ThreadLocalRandom.current().nextInt(0, randomX);
                int randomLocationY = ThreadLocalRandom.current().nextInt(0, randomY);
                maze.startAt(randomLocationX, randomLocationY);
                Assert.assertEquals(maze.getCurrentLocation(),
                        new Location(randomLocationX, randomLocationY));

                // Start at a random location
                maze.startAtRandomLocation();
                Assert.assertNotEquals(maze.getCurrentLocation(),
                        new Location(randomLocationX, randomLocationY));

                // End at the top right
                maze.endAtTopRight();
                Assert.assertEquals(maze.getEndLocation(),
                        new Location(randomX - 1, randomY - 1));
                Assert.assertFalse(maze.isFinished());

                // End at a random given location
                randomLocationX = ThreadLocalRandom.current().nextInt(0, randomX);
                randomLocationY = ThreadLocalRandom.current().nextInt(0, randomY);
                maze.endAt(randomLocationX, randomLocationY);
                Assert.assertEquals(maze.getEndLocation(),
                        new Location(randomLocationX, randomLocationY));
                Assert.assertFalse(maze.isFinished());

                // End at a random location
                maze.endAtRandomLocation();
                Assert.assertNotEquals(maze.getEndLocation(),
                        new Location(randomLocationX, randomLocationY));
                Assert.assertFalse(maze.isFinished());

            }
        }
    }

    /**
     * Test bad locations.
     */
    @Test
    public void testBadLocations() {
        for (int i = 0; i < 10; i++) {
            int randomX = ThreadLocalRandom.current().nextInt(80, 100);
            int randomY = ThreadLocalRandom.current().nextInt(80, 100);

            Maze maze = new Maze(randomX, randomY);

            // Test various out-of-bounds locations
            try {
                maze.startAt(-1, randomY);
                fail();
            } catch (LocationException e) {
            }
            try {
                maze.startAt(randomY, -1);
                fail();
            } catch (LocationException e) {
            }
            try {
                maze.startAt(randomX + 1, randomY);
                fail();
            } catch (LocationException e) {
            }
            try {
                maze.startAt(randomX, randomY + 1);
                fail();
            } catch (LocationException e) {
            }

            try {
                maze.endAt(-1, randomY);
                fail();
            } catch (LocationException e) {
            }
            try {
                maze.endAt(randomY, -1);
                fail();
            } catch (LocationException e) {
            }
            try {
                maze.endAt(randomX + 1, randomY);
                fail();
            } catch (LocationException e) {
            }
            try {
                maze.endAt(randomX, randomY + 1);
                fail();
            } catch (LocationException e) {
            }
        }
    }

    /**
     * Test random walk.
     */
    @Test(enabled = false)
    public void testRandomWalk() {
        int randomX = ThreadLocalRandom.current().nextInt(4, 5);
        int randomY = ThreadLocalRandom.current().nextInt(4, 5);

        Maze maze = new Maze(randomX, randomY);
        maze.startAtRandomLocation();
        maze.endAtRandomLocation();

        // Solve a small maze using a random walk. Reliably finishing this takes a _large_ number of
        // steps, even for a small maze.
        for (int steps = 0; steps < 10000000; steps++) {
            if (maze.canMove()) {
                maze.move();
            } else {
                do {
                    for (int turnCount = 0; turnCount < ThreadLocalRandom.current().nextInt(0,
                            4); turnCount++) {
                        maze.turnLeft();
                    }
                } while (!(maze.canMove()));
                Assert.assertTrue(maze.move());
            }
            if (maze.isFinished()) {
                break;
            }
        }
        if (!(maze.isFinished())) {
            fail();
        }
    }
}
