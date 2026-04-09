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
	private Image[] variants;
	private int cost = 1;
	private String folderName = "";
	
	// A cache map to store the calculated index for each coordinate to prevent recalculating every time
	private Map<String, Integer> variantCache = new HashMap<>();
	
	private static final double NOISE_SCALE = 0.15;
	
	public Tile(int cost, String folderName, String... sprites) {
		this.cost = cost;
		this.folderName = folderName;
		variants = new Image[sprites.length];
		for (int i = 0; i < sprites.length; i++) {
			variants[i] = loadImage(String.format("assets/tiles/%s/%s", this.folderName, sprites[i]));
		}
	}
	
	/**
     * Returns the appropriate sprite image for a specific board coordinate.
     * Uses a cached Voronoi calculation to ensure the same tile always looks the same.
     * @param row The row coordinate of the tile on the board
     * @param col The column coordinate of the tile on the board
     * @return The specific Image variant to draw
     */
	public Image getImage(int row, int col) {
		if (variants.length == 0 || variants[0] == null) {
			Debug.error("'getImage': \t'variants' is null");
			return null;
		}
		if (variants.length == 1) return variants[0];
		
		// Create a unique key for this coordinate
		String key = row + "," + col;
		
		// If we haven't calculated this tile's variant yet, calculate and cache it
		if (!variantCache.containsKey(key)) {
			int index = getVoronoiIndex(row, col, variants.length);
			variantCache.put(key, index);
		}
		
		return variants[variantCache.get(key)];
	}
	
	public int getCost() {
		return this.cost;
	}
	
	/**
     * Determines the voronoi noise index for a tile.
     * @param row The row position
     * @param col The column position
     * @param numVariants The total number of loaded sprites for this tile type. Used as a modulo constraint.
     * @return An integer between 0 and (numVariants - 1) representing which sprite to use.
     */
	private int getVoronoiIndex(int row, int col, int numVariants) {
		double x = col * NOISE_SCALE;
		double y = row * NOISE_SCALE;

		int cellX = (int) Math.floor(x);
		int cellY = (int) Math.floor(y);

		double minDist = Double.MAX_VALUE;
		int closestPointId = 0;

		// Check the current cell and 8 surrounding cells for the closest "seed" point
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int neighborX = cellX + i;
				int neighborY = cellY + j;

				// Generate a deterministic random point inside this neighbor cell
				double pointX = neighborX + deterministicRandom(neighborX, neighborY);
				double pointY = neighborY + deterministicRandom(neighborY, neighborX);

				// Calculate distance to that point
				double dx = pointX - x;
				double dy = pointY - y;
				double distSq = dx * dx + dy * dy;

				// Find the absolute closest point
				if (distSq < minDist) {
					minDist = distSq;
					// Create a unique ID for the closest point to determine its sprite variant
					closestPointId = Math.abs(neighborX * 31 + neighborY * 17);
				}
			}
		}

		// Map the closest point's ID to one of the array indices safely
		return closestPointId % numVariants;
	}
	
	/**
     * A simple, fast hash function that acts as a consistent 'random' number generator.
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