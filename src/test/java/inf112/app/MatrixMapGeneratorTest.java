package inf112.app;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class MatrixMapGeneratorTest {
    @Test
    public void FromFileMatrixTest() {
        int[][][] map = new MapParser().fromFile("assets/TiledTest.tmx").getMatrixMap();

        assertEquals(5, map[0][0][0]);
        assertEquals(55, map[2][4][4]);
    }

    @Test
    public void FromFileMapTest() {
        Map<String,int[][]> map = new MapParser().fromFile("assets/TiledTest.tmx").getHashMap();

        assertEquals(5, map.get("board")[0][0]);
        assertEquals(55, map.get("flag")[4][4]);
    }
}
