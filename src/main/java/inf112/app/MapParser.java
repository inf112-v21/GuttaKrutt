package inf112.app;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates an int[][][] and Map<String,int[][]> from either a .tmx
 * file (from Tiled) or a TiledMap object.
 */
public class MapParser {
    int[][][] matrixMap;
    Map<String,int[][]> hashMap = new HashMap<>();

    public int[][][] getMap() { return getMatrixMap(); }

    /**
     * @return map in form int[][][], where the first value is the layer, int[][x][y] is the value of the cell in (x,y).
     */
    public int[][][] getMatrixMap() { return matrixMap; }

    /**
     * @return map in form Map<String,int[][]>. String is the name of the layer, int[][] is the layer. int[x][y] is value of cell in position (x,y).
     */
    public Map<String,int[][]> getHashMap() { return hashMap; }

    /**
     * Generates maps based on a .tmx file.
     * @param fileName Path to the file
     * @return Returns itself, so it can be easily chained into a getMap() function.
     */
    public MapParser fromFile(String fileName) {
        try {
            File tmxMap = new File(fileName); //Loads file in as File object.

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(tmxMap); //Parses the document as XML.

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("layer"); //Gets list of all layers in the map.
            matrixMap = new int[nList.getLength()][][];

            for (int i = 0; i < nList.getLength(); i++) {
                Element el = (Element) nList.item(i);
                int[][] layer = parseCSV(el.getElementsByTagName("data").item(0).getTextContent()); //Gets the csv in the .tmx file and parses it.
                matrixMap[i] = layer; //Adds the layer to the corresponding layer in matrixMap
                hashMap.put(el.getAttribute("name"),layer); //Adds the layer with the key being the name of the layer.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Parses a string in csv format into an int[][] matrix representing a layer
     * of the map stored in the .tmx file.
     * @param csv String to be parsed.
     * @return int[][] matrix representing a map layer.
     */
    public int[][] parseCSV(String csv) {
        String[] lines = csv.split("\n");
        int[][] layer = new int[lines[1].split(",").length][lines.length-1];

        for (int i=1;i<lines.length;i++) { //the first line in lines is an empty string, so we start at i=1.
            String[] tiles = lines[i].split(",");
            for (int j=0;j<tiles.length;j++) {
                layer[j][lines.length-i-1] = Integer.parseInt(tiles[j]); //The map has y=0 at the bottom, so the y value of the
                    // layer needs to be inverted from the i-value.
            }
        }

        return layer;
    }

    /**
     * Generates maps based on a TiledMap object.
     * @param tiledMap TiledMap object to be converted.
     * @return Returns itself, so it can be easily chained into a getMap() function.
     */
    public MapParser fromTiledMap(TiledMap tiledMap) {
        matrixMap = new int[tiledMap.getLayers().size()][][];

        MapLayers layers = tiledMap.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layers.get(i);
            int[][] layer = parseMapLayer(tiledLayer);

            matrixMap[i] = layer;
            hashMap.put(tiledLayer.getName(),layer);
        }

        return this;
    }

    /**
     * Parses a layer of a TiledMap into a layer in format int[][].
     * @param tiledLayer Layer to be parsed.
     * @return int[][] representing a layer.
     */
    public int[][] parseMapLayer(TiledMapTileLayer tiledLayer) {
        int x = tiledLayer.getWidth();
        int y = tiledLayer.getHeight();
        int[][] layer = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(i, j);
                if (cell == null) {
                    //The cell object is null if there is no cell at a location.
                    //We can't execute .getTile().getId() on the object if it is null.
                    //Therefore we will enter value 0 if there is no cell.
                    //This is the same value as in the .tmx file.
                    layer[i][j] = 0;
                } else {
                    layer[i][j] = cell.getTile().getId(); //Gets
                }
            }
        }

        return layer;
    }
}
