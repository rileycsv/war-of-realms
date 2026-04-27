package environment.tile;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

import utils.Debug;

/**
 * Represents a single tile on the game board.
 * Handles the loading of multiple sprite variants and uses deterministic 
 * Voronoi noise to cache and assign a visual variant based on board coordinates.
 */
public abstract class Tile {
	private Image[] sprites;
	private double[] variantWeights;
	private int cost = 1;
	private String folderName = "";
	
	// A cache map to store the calculated index for each coordinate to prevent recalculating every time
	private Map<String, Integer> variantCache = new HashMap<>();
	
	public Tile(int cost, String folderName, Object... entries) {
		this.cost = cost;
		this.folderName = folderName;
		sprites = new Image[entries.length / 2];
		variantWeights = new double[entries.length / 2];
		for (int i = 0; i < entries.length; i += 2) {
			sprites[i / 2] = loadImage(String.format("assets/tiles/%s/%s", this.folderName, entries[i]));
			variantWeights[i / 2] = (double) entries[i + 1];
		}
	}
	
	/**
     * Returns the appropriate sprite image for a specific board coordinate.
     * Uses a cached weighted selection to ensure the same tile always looks the same.
     */
    public Image getImage(int row, int col) {
        if (sprites.length == 0 || sprites[0] == null) {
            Debug.error("'getImage': \t'sprites' is null");
            return null;
        }
        if (sprites.length == 1) return sprites[0];

        String key = row + "," + col;

        if (!variantCache.containsKey(key)) {
            int index = getWeightedIndex(row, col);
            variantCache.put(key, index);
        }

        return sprites[variantCache.get(key)];
    }
	
	/**
     * Maps a coordinate to a sprite index using its deterministic [0, 1) value
     * checked against the cumulative weight thresholds.
     */
    private int getWeightedIndex(int row, int col) {
    	double value = deterministicRandom(row, col);
		
        for (int i = 0; i < variantWeights.length; i++) {
            if (value < variantWeights[i]) return i;
        }
		
        // Fallback for any floating point edge case at exactly 1.0
        return sprites.length - 1;
    }
	
	public int getCost() {
		return this.cost;
	}
	
	/**
     * A method that acts as a consistent 'random' number generator.
     * Returns a pseudo-random value between 0.0 and 1.0 based ONLY on the input x,y coordinate.
     */
	private double deterministicRandom(int x, int y) {
		int n = x * 374761393 + y * 668265263;
		n = (n ^ (n >> 13)) * 1274126177;
		return ((double) ((n ^ (n >> 16)) & 0x7fffffff)) / 0x7fffffff;
	}

	private Image loadImage(String path) {
		try {
			Image img = new Image("file:" + path);
			if (img.isError()) {
				Debug.error("'loadImage': \t'img' is null");
				return null;
			} else {
				return img;
			}
		} catch (Exception ex) {
			Debug.error(ex.getMessage());
			return null;
		}
	}
}