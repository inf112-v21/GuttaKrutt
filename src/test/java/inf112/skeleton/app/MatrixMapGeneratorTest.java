package inf112.skeleton.app;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class MatrixMapGeneratorTest {
    @Test
    public void FromFileTest() {
        MatrixMapGenerator gen = new MatrixMapGenerator();
        gen.fromFile("assets/TiledTest.tmx");

        int[][][] map = gen.matrixMap;

        assertTrue(map[0][0][0] == 5);
        assertTrue(map[2][4][0] == 55);
    }
}
