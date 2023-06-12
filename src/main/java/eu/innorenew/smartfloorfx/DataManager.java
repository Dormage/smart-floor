package eu.innorenew.smartfloorfx;

import java.net.InetAddress;
import java.util.HashMap;

public class DataManager {

    private HashMap<String, Measurement>  tilesData;
    private HashMap<String, Tile>         tiles;
    private HashMap<Integer, String>      mapping;
    private HashMap<String, InetAddress> networkMap;

    public DataManager(HashMap<Integer, String> mapping) {
        this.tilesData  = new HashMap<>();
        this.tiles      = new HashMap<>();
        this.networkMap = new HashMap<>();
        this.mapping    = mapping;
    }

    public Measurement getMeasurement(String tileId) {
        return tilesData.get(tileId);
    }

    public void setMeasurement(String tileId, Measurement measurement) {
        if (!tilesData.keySet().contains(tileId)) {
            System.out.println(tileId);
        }
        this.tilesData.put(tileId, measurement);

    }

    public void addTile(String tileId, Tile tile) {
        this.tiles.put(tileId, tile);
    }

    public Tile getTile(String tileId) {
        return tiles.get(tileId);
    }

    public HashMap<String, Measurement> getTilesData() {
        return tilesData;
    }

    public void setTilesData(HashMap<String, Measurement> tilesData) {
        this.tilesData = tilesData;
    }

    public HashMap<String, Tile> getTiles() {
        return tiles;
    }

    public void setTiles(HashMap<String, Tile> tiles) {
        this.tiles = tiles;
    }

    public HashMap<Integer, String> getMapping() {
        return mapping;
    }

    public void setMapping(HashMap<Integer, String> mapping) {
        this.mapping = mapping;
    }

    public void setNetworkMap(String tileId, InetAddress address) {
        if (!networkMap.containsKey(tileId)) {
            networkMap.put(tileId, address);
        }
    }

    public HashMap<String, InetAddress> getNetworkMap() {
        return networkMap;
    }
}
