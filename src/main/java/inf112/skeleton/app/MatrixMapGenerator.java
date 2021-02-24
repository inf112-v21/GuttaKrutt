package inf112.skeleton.app;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/*
    Generates an int[][][] from either a .tmx file (from Tiled) or
    a TiledMap object
 */
public class MatrixMapGenerator {
    int[][][] matrixMap;

    public MatrixMapGenerator() {
        matrixMap = new int[6][5][5];
    }

    public int[][][] getMap() {
        return matrixMap;
    }

    public MatrixMapGenerator fromFile(String fileName) {
        try {

            File tmxMap = new File(fileName);

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(tmxMap);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("layer");

            for (int i = 0; i < nList.getLength(); i++) {
                Element el = (Element) nList.item(i);
                matrixMap[i] = parseCSV(el.getElementsByTagName("data").item(0).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public int[][] parseCSV(String csv) {
        String[] lines = csv.split("\n");
        int[][] layer = new int[5][5];

        for (int i=1;i<lines.length;i++) {
            String[] tiles = lines[i].split(",");
            for (int j=0;j<tiles.length;j++) {
                layer[j][4-(i-1)] = Integer.parseInt(tiles[j]);
            }
        }

        return layer;
    }

    public MatrixMapGenerator fromTiledMap(TiledMap tiledMap) {
        MapLayers layers = tiledMap.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) layers.get(i)).getCell(j, k);
                    if (cell == null) {
                        matrixMap[i][j][k] = 0;
                    } else {
                        matrixMap[i][j][k] = cell.getTile().getId();
                    }
                }
            }
        }

        return this;
    }
}
