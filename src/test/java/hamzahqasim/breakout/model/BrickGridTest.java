package hamzahqasim.breakout.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BrickGridTest {

    @Test
    void allBricksStartActive() {
        BrickGrid grid = new BrickGrid(4, 9, 100, 60, 50, 50);

        assertEquals(36, grid.getRemaining());
        assertTrue(grid.isActive(0, 0));
        assertTrue(grid.isActive(3, 8));
    }

    @Test
    void destroyDeactivatesBrickAndDecrementsRemaining() {
        BrickGrid grid = new BrickGrid(2, 2, 100, 60, 0, 0);

        boolean destroyed = grid.destroy(0, 1);

        assertTrue(destroyed);
        assertFalse(grid.isActive(0, 1));
        assertEquals(3, grid.getRemaining());
    }

    @Test
    void destroyingAlreadyInactiveBrickIsNoOp() {
        BrickGrid grid = new BrickGrid(1, 1, 100, 60, 0, 0);
        grid.destroy(0, 0);

        boolean destroyedAgain = grid.destroy(0, 0);

        assertFalse(destroyedAgain);
        assertEquals(0, grid.getRemaining());
    }

    @Test
    void isClearedOnlyOnceEveryBrickIsDestroyed() {
        BrickGrid grid = new BrickGrid(1, 2, 100, 60, 0, 0);

        assertFalse(grid.isCleared());
        grid.destroy(0, 0);
        assertFalse(grid.isCleared());
        grid.destroy(0, 1);
        assertTrue(grid.isCleared());
    }

    @Test
    void resetReactivatesAllBricks() {
        BrickGrid grid = new BrickGrid(1, 2, 100, 60, 0, 0);
        grid.destroy(0, 0);
        grid.destroy(0, 1);

        grid.reset();

        assertTrue(grid.isActive(0, 0));
        assertTrue(grid.isActive(0, 1));
        assertEquals(2, grid.getRemaining());
    }

    @Test
    void boundsAreOffsetByGridPositionAndRowColumn() {
        BrickGrid grid = new BrickGrid(4, 9, 100, 60, 50, 50);

        var bounds = grid.getBounds(2, 3);

        assertEquals(50 + 3 * 100, bounds.x);
        assertEquals(50 + 2 * 60, bounds.y);
        assertEquals(100, bounds.width);
        assertEquals(60, bounds.height);
    }
}
