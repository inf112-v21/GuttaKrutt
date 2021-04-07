package inf112.app;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class MapParserTest {
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

    @Test
    public void sizeTest() {
        Map<String,int[][]> map = new MapParser().fromFile("assets/TiledTest.tmx").getHashMap();
        assertEquals("width",map.get("board").length,5);
        assertEquals("height",map.get("board")[0].length,5);

        map = new MapParser().fromFile("assets/Checkmate.tmx").getHashMap();
        assertEquals("width",map.get("board").length,12);
        assertEquals("height",map.get("board")[0].length,16);
    }
}
