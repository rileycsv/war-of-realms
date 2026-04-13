package environment;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.effect.ColorAdjust;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;

import utils.Debug;
import core.GameManager;
import entities.Unit;
import environment.tile.*;

/**
 * Handles the visual rendering of the isometric game grid using JavaFX.
 * Responsibilities include:
 * - Translating screen coordinates to isometric grid coordinates.
 * - Managing camera zoom, panning, and event listeners.
 * - Rendering tiles and effects.
 */
public class Board {
	// Tile dimensions
	private static final double TILE_W = 38.0;
	private static final double FACE_H = 22.0;
	private static final double SPRITE_H = 36.0;

	// Canvas
	private static final double CANVAS_W = 900;
	private static final double CANVAS_H = 600;

	// Origin: canvas XY of the TOP VERTEX of tile (row=0, col=0)
	private static final double ORIGIN_X = CANVAS_W / 2.0;
	private static final double ORIGIN_Y = 60.0;

	/**
	 * The index of the board currently being renderred
	 */
	private static int CURRENT_BOARD = 2;

	public static void setActiveBoard(int index) {
		if (index >= 0 && index < BOARD.length) {
			CURRENT_BOARD = index;
			UNITS_BOARD = new Unit[getBoard().length][getBoard()[0].length];
		}
	}

	private static Unit[][] UNITS_BOARD = null;
	
	public static Unit[][] getUnitsBoard() {
		return UNITS_BOARD;
	}

	/**
	 * The predefined map layouts for the game.
	 * Dimension 1: The Board ID (Level)
	 * Dimension 2: Rows
	 * Dimension 3: Columns
	 * Legend:
	 * 'g' = Grass
	 * 'm' = Mountain
	 * 'r' = River
	 * 'f' = Forest
	 * 'v' = Void
	 */
	private static final char[][][] BOARD = {
		{
			// Easy - open terrain, simple river loop (16x9)
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'r', 'r', 'r', 'r', 'r', 'r', 'g', 'g', 'm', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'r', 'r', 'r', 'r', 'r', 'r', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g' },
			{ 'g', 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' }
		},
		{
			// Medium - mountain range with a winding river (18x10)
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'm', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'm', 'g', 'm', 'g', 'g', 'r', 'r', 'r', 'g', 'g', 'g', 'g', 'g', 'm', 'm', 'g', 'g' },
			{ 'g', 'm', 'g', 'g', 'g', 'g', 'r', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'm', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'r', 'r', 'r', 'r', 'g', 'g', 'm', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'r', 'r', 'g', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'm', 'm', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g', 'g' }
		},
		{
			// Hard - dense terrain, narrow passages, dual river channels (20x11)
			{ 'g', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'm', 'g' },
			{ 'g', 'm', 'g', 'g', 'g', 'r', 'r', 'r', 'g', 'g', 'g', 'g', 'r', 'r', 'r', 'g', 'g', 'g', 'm', 'g' },
			{ 'g', 'g', 'g', 'm', 'g', 'r', 'g', 'r', 'g', 'm', 'm', 'g', 'r', 'g', 'r', 'g', 'm', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'm', 'g', 'r', 'g', 'r', 'g', 'm', 'g', 'g', 'r', 'g', 'r', 'g', 'm', 'g', 'g', 'g' },
			{ 'g', 'g', 'm', 'm', 'g', 'r', 'g', 'r', 'r', 'r', 'g', 'g', 'r', 'g', 'r', 'r', 'r', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'm', 'm', 'm', 'm', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g' },
			{ 'g', 'm', 'g', 'g', 'g', 'r', 'r', 'r', 'r', 'g', 'g', 'r', 'r', 'r', 'r', 'r', 'r', 'g', 'g', 'g' },
			{ 'g', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g' },
			{ 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'm' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' }
		},
		{
			// Two player, river divide
			{ 'm', 'm', 'm', 'm', 'g', 'r', 'g', 'g', 'm', 'm' },
			{ 'm', 'm', 'm', 'g', 'g', 'r', 'g', 'g', 'g', 'm' },
			{ 'm', 'm', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'm' },
			{ 'm', 'm', 'g', 'g', 'g', 'r', 'm', 'g', 'm', 'm' },
			{ 'm', 'm', 'm', 'm', 'm', 'r', 'm', 'm', 'm', 'm' }
		},
		{
			// DEBUG map
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'v', 'v', 'v', 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'g', 'g', 'v', 'g', 'g', 'v', 'v', 'v', 'g', 'g' },
			{ 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'g', 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'v', 'v', 'v', 'g' },
			{ 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'g', 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'g', 'v', 'g' },
			{ 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'v', 'v', 'v', 'g', 'v', 'v', 'v', 'g', 'g', 'g', 'v', 'v', 'g', 'g', 'g', 'v', 'v', 'v', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
			{ 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm' },
			{ 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm' }
		}
	};

	/**
	 * Returns the current board
	 */
	public static char[][] getBoard() {
		Debug.log(3, "Accessing board: " + CURRENT_BOARD);
		return BOARD[CURRENT_BOARD];
	}

	public static int[][] getBoardCosts() {
		final int[][] ret = new int[BOARD[CURRENT_BOARD].length][BOARD[CURRENT_BOARD][0].length];
		for (int r = 0; r < BOARD[CURRENT_BOARD].length; r++) {
			for (int c = 0; c < BOARD[CURRENT_BOARD][r].length; c++) {
				int cost = getTileCost(r, c);
				ret[r][c] = cost;
			}
		}
		return ret;
	}

	/**
	 * Returns the tile at a given coordinate on the current board
	 */
	public static char getBoardTile(int r, int c) {
		Debug.log(3, "Accessing tile at row=" + r + ", col=" + c);
		return BOARD[CURRENT_BOARD][r][c];
	}

	// Runtime state
	private Canvas canvas;
	private HBox turnIndicatorBox;

	// Camera & mouse state
	// Defaulting closer to the center of the specific grid layout
	private double cameraX = ORIGIN_X;
	private double cameraY = ORIGIN_Y + 100;
	private double zoom = 3.5;
	private double max_zoom = 7.0;
	private double min_zoom = 1;

	private double lastMouseX = -1;
	private double lastMouseY = -1;
	private int hoverRow = -1;
	private int hoverCol = -1;

	@SuppressWarnings("unused")
	/**
	 * Initializes the canvas, sets up input listeners for mouse and keyboard,
	 * and returns the constructed scene.
	 * 
	 * @return The constructed JavaFX Scene containing the interactive board.
	 */
	public Scene getScene() {
		// Initialize the canvas with the width and height of the board
		canvas = new Canvas(CANVAS_W, CANVAS_H);
		StackPane root = new StackPane(canvas);
		// Initialize the scene with a stack containing the canvas
		Scene scene = new Scene(root, CANVAS_W, CANVAS_H);

		// 1. Create the Turn Indicator HUD
		turnIndicatorBox = createTurnIndicator();

		// 2. Create the StackPane and add both Canvas and HUD
		root.setStyle("-fx-background-color: #111111;");
		root.getChildren().addAll(turnIndicatorBox);
		turnIndicatorBox.toFront();
		canvas.setManaged(false);

		// 3. Align the HUD to the top center and add some margin
		StackPane.setAlignment(turnIndicatorBox, Pos.TOP_CENTER);
		StackPane.setMargin(turnIndicatorBox, new Insets(15, 0, 0, 0));

		Button endTurnButton = new Button("End Turn");
		endTurnButton.setOnAction(e -> {
			GameManager.endTurn();
			updateTurnIndicatorUI();
		});

		// Get cursor position when cursor moves
		canvas.setOnMouseMoved(e -> {
			lastMouseX = e.getX();
			lastMouseY = e.getY();
			updateHover();
		});

		// When the mouse exits the canvas, don't highlight any tiles
		canvas.setOnMouseExited(e -> {
			lastMouseX = -1;
			lastMouseY = -1;
			hoverRow = -1;
			hoverCol = -1;
			render();
		});

		canvas.setOnMouseClicked(e -> {
			double[] world = screenToWorld(e.getX(), e.getY());
			int[] tile = canvasToTile(world[0], world[1]);

			if (tile[0] == -1 || tile[1] == -1) {
				// Clicked outside board
				GameManager.clearSelection();
				return;
			}

			Unit unitAtTile = GameManager.getUnitAtTile(tile[0], tile[1]);

			if (unitAtTile != null && unitAtTile.getPlayerID() == GameManager.getActivePlayer()) {
				// Clicked on own unit - select it
				GameManager.setSelectedUnit(unitAtTile);
			} else {
				// Clicked on enemy, empty space, or neutral - deselect
				GameManager.clearSelection();
			}
		});

		// When the canvas is scrolled, zoom logic and rerender
		canvas.setOnScroll(e -> {
			if (e.getDeltaY() > 0) {
				zoom = Math.min(max_zoom, zoom * 1.1);
			} else if (e.getDeltaY() < 0) {
				zoom = Math.max(min_zoom, zoom / 1.1);
			}
			updateHover();
			render();
		});

		scene.setOnKeyPressed(e -> {
			Debug.log(3, "Camera: (x:" + cameraX + ", y:" + cameraY + ", z:" + zoom + ") ");
			double CAMERA_MOVE_AMOUNT = 20.0;
			switch (e.getCode()) {
				case W:
				case UP:
					cameraY -= CAMERA_MOVE_AMOUNT; // Move camera UP (decrease Y)
					break;
				case S:
				case DOWN:
					cameraY += CAMERA_MOVE_AMOUNT; // Move camera DOWN (increase Y)
					break;
				case A:
				case LEFT:
					cameraX -= CAMERA_MOVE_AMOUNT; // Move camera LEFT (decrease X)
					break;
				case D:
				case RIGHT:
					cameraX += CAMERA_MOVE_AMOUNT; // Move camera RIGHT (increase X)
					break;
				case EQUALS:
				case PLUS:
					zoom = Math.min(max_zoom, zoom * 1.1);
					break;
				case MINUS:
					zoom = Math.max(min_zoom, zoom / 1.1);
					break;
				case SPACE:
					centerCameraOnBoard();
					break;
				default:
					break;
			}
			updateHover();
			render();
		});

		centerCameraOnBoard();
		canvas.requestFocus();
		return scene;
	}

	/**
	 * Builds the UI element for the turn indicator.
	 */
	private HBox createTurnIndicator() {
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER);
		hbox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		// This allows mouse clicks to pass through the empty space
		// between and around your avatars, down to the canvas below
		hbox.setPickOnBounds(false);

		// Now it's safe to loop through the actual players
		for (int i = 0; i < GameManager.players.length; i++) {
			Rectangle avatarPlaceholder = new Rectangle(50, 50, Color.DARKSLATEGRAY);

			StackPane avatarContainer = new StackPane(avatarPlaceholder);
			avatarContainer.setPrefSize(54, 54);
			avatarContainer.setStyle("-fx-border-color: #333333; -fx-border-width: 2; -fx-background-color: black;");

			hbox.getChildren().add(avatarContainer);
		}

		return hbox;
	}

	/**
	 * Call this method whenever a turn ends to update the UI highlights.
	 */
	public void updateTurnIndicatorUI() {
		// Don't do anything if the box wasn't created or has no avatars
		if (turnIndicatorBox == null || turnIndicatorBox.getChildren().isEmpty())
			return;

		int activeIndex = GameManager.getActivePlayer();

		for (int i = 0; i < turnIndicatorBox.getChildren().size(); i++) {
			StackPane container = (StackPane) turnIndicatorBox.getChildren().get(i);

			if (i == activeIndex && activeIndex != -1) {
				// Active Player
				container.setStyle("-fx-border-color: #FFD700; -fx-border-width: 4; -fx-background-color: black;");
				container.setScaleX(1.1);
				container.setScaleY(1.1);
			} else {
				// Inactive Player
				container.setStyle("-fx-border-color: #333333; -fx-border-width: 2; -fx-background-color: black;");
				container.setScaleX(1.0);
				container.setScaleY(1.0);
			}
		}
	}

	// =========================================================================
	// Camera / Coordinate Logic
	// =========================================================================

	/**
	 * Translates raw window/screen coordinates into absolute world coordinates,
	 * accounting for current camera pan and zoom levels.
	 * 
	 * @param cx The x position on the screen
	 * @param cy The y position on the screen
	 * @return An array containing [WorldX, WorldY]
	 */
	private double[] screenToWorld(double cx, double cy) {
		// Perfectly mirrors the new render() translations/scaling
		double wx = (cx - CANVAS_W / 2.0) / zoom + cameraX;
		double wy = (cy - CANVAS_H / 2.0) / zoom + cameraY;

		// The -6.5 is needed for some reason since the
		return new double[] { wx, wy - 6.5 };
	}

	/**
	 * Run when canvas is scrolled or mouse is moved to update tile highlights.
	 */
	private void updateHover() {
		if (lastMouseX < 0 || lastMouseY < 0) {
			return;
		}

		double[] world = screenToWorld(lastMouseX, lastMouseY);
		int[] t = canvasToTile(world[0], world[1]);

		if (t[0] != hoverRow || t[1] != hoverCol) {
			hoverRow = t[0];
			hoverCol = t[1];
			render();
		}
	}

	// =========================================================================
	// Rendering
	// =========================================================================

	/**
	 * Core rendering pipeline.
	 * Clears the canvas, applies the global camera translation and zoom transforms,
	 * and draws the grid back-to-front (painter's algorithm) to prevent overlap
	 * issues.
	 */
	private void render() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.web("#111111"));
		gc.fillRect(0, 0, CANVAS_W, CANVAS_H);
		
		// Save graphics state
		gc.save();
		
		// Apply camera transform
		gc.translate(cameraX, cameraY);
		gc.scale(zoom, zoom);
		
		// === RENDERING PIPELINE (PAINTER'S ALGORITHM) ===
		char[][] board = getBoard();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				drawTile(gc, row, col);
			}
		}
		
		// Render highlights for selected unit (if any)
		drawUnitHighlights(gc);
		
		// Restore graphics state
		gc.restore();
	}

	/**
	 * Draws a tile, handles positioning of differently sized tile sprites
	 * 
	 * @param gc  The Graphics Context to draw to
	 * @param row The row of the tile
	 * @param col The column of the tile
	 */
	private void drawTile(GraphicsContext gc, int row, int col) {
		double[] top = tileTopPoint(row, col);

		Image img = getTile(row, col);

		if (img != null) {
			double imgW = img.getWidth();
			double imgH = img.getHeight();

			double drawX = top[0] - (imgW / 2.0);
			double drawY;

			// If the image is roughly the height of the top face (~22px), it's a flat tile
			// (River).
			// If it's taller, it contains the dirt block (Grass/Mountain).
			if (imgH <= FACE_H + 2) {
				// Flat tiles lack dirt. Anchor them directly to the top edge.
				drawY = top[1];
			} else {
				// Block tiles have dirt. Align their bottom to the standard dirt baseline.
				drawY = (top[1] + SPRITE_H) - imgH;
			}

			// Tint sprite on hover
			if (row == hoverRow && col == hoverCol) {
				ColorAdjust highlight = new ColorAdjust();
				highlight.setBrightness(0.3);
				gc.setEffect(highlight);
			}

			gc.drawImage(img, drawX, drawY, imgW, imgH);
			gc.setEffect(null);

		} else {
			// Fallback uses the standard hardcoded dimensions instead of calculating them
			double defaultDrawX = top[0] - TILE_W / 2.0;
			double defaultDrawY = top[1];

			gc.save();
			clipToTopFace(gc, top);
			gc.setFill(fallbackColor(getBoard()[row][col]));
			gc.fillRect(defaultDrawX, defaultDrawY, TILE_W, SPRITE_H);

			if (row == hoverRow && col == hoverCol) {
				gc.setFill(new Color(1, 1, 1, 0.3));
				gc.fillRect(defaultDrawX, defaultDrawY, TILE_W, SPRITE_H);
			}
			gc.restore();
		}
	}

	private static final Tile GRASS_TILE = new GrassTile();
	private static final Tile MOUNTAIN_TILE = new MountainTile();
	private static final Tile RIVER_TILE = new RiverTile();
	private static final Tile VOID_TILE = new VoidTile();
	private static final Tile FOREST_TILE = new ForestTile();

	/**
	 * Returns the image for a given tile
	 * 
	 * @param row The row position of the tile to get
	 * @param col The column position of the tile to get
	 * @return The image of the tile
	 */
	private static Image getTile(int row, int col) {
		Tile tile = null;
		switch (getBoard()[row][col]) {
			case 'g':
				tile = GRASS_TILE;
				break;
			case 'f':
				tile = FOREST_TILE;
				break;
			case 'm':
				tile = MOUNTAIN_TILE;
				break;
			case 'r':
				tile = RIVER_TILE;
				break;
			case 'v':
				tile = VOID_TILE;
				break;
		}
		// Now it just calculates the Voronoi noise and returns an already-loaded Image!
		return tile != null ? tile.getImage(row, col) : null;
	}

	/**
	 * Returns the fallback color for a given tile in case assets are missing
	 */
	private static Color fallbackColor(char c) {
		return switch (c) {
			case 'g' -> Color.rgb(78, 158, 58);
			case 'f' -> Color.rgb(50, 84, 42);
			case 'm' -> Color.rgb(175, 188, 200);
			case 'r' -> Color.rgb(55, 115, 200);
			case 'v' -> Color.rgb(0, 0, 0);
			default -> Color.GRAY;
		};
	}

	/**
	 * Returns the cost for a given tile
	 * 
	 * @param row The row position of the tile to get
	 * @param col The column position of the tile to get
	 * @return The cost of the tile
	 */
	private static int getTileCost(int row, int col) {
		Tile tile = null;
		switch (getBoard()[row][col]) {
			case 'g':
				tile = GRASS_TILE;
				break;
			case 'f':
				tile = FOREST_TILE;
				break;
			case 'm':
				tile = MOUNTAIN_TILE;
				break;
			case 'r':
				tile = RIVER_TILE;
				break;
			case 'v':
				tile = VOID_TILE;
				break;
		}
		return tile != null ? tile.getCost() : 0;
	}

	/**
	 * Returns the topmost point of a tile, used for positioning tile sprites in the
	 * grid
	 */
	public double[] tileTopPoint(int row, int col) {
		double x = ORIGIN_X + (col - row) * (TILE_W / 2.0);
		double y = ORIGIN_Y + (col + row) * (FACE_H / 2.0);
		return new double[] { x, y };
	}

	/**
	 * Converts the absolute world coordinates to the coordinates on the board grid.
	 * 
	 * @param worldX The absolute x position in the world
	 * @param worldY The absolute y position in the world
	 * @return An array containing [row, col], or [-1, -1] if out of bounds.
	 */
	public int[] canvasToTile(double worldX, double worldY) {
		double hw = TILE_W / 2.0;
		double hh = FACE_H / 2.0;

		// Shift world coordinates relative to our grid origin
		double dx = worldX - ORIGIN_X;
		double dy = worldY - ORIGIN_Y;

		/*
		 * Isometric Math:
		 * The standard conversion for a diamond grid:
		 * row = (y / halfHeight - x / halfWidth) / 2
		 * col = (y / halfHeight + x / halfWidth) / 2
		 */
		double rowF = (dy / hh - dx / hw) / 2.0;
		double colF = (dy / hh + dx / hw) / 2.0;

		int r = (int) Math.floor(rowF);
		int c = (int) Math.floor(colF);

		// Bounds check
		if (r < 0 || r >= getBoard().length || c < 0 || c >= getBoard()[0].length) {
			return new int[] { -1, -1 };
		}

		return new int[] { r, c };
	}
	
	/**
	 * Makes a polygon as fallback for if no sprite texture file is found
	 */
	private static void clipToTopFace(GraphicsContext gc, double[] top) {
		double hw = TILE_W / 2.0;
		double hh = FACE_H / 2.0;
		gc.beginPath();
		gc.moveTo(top[0], top[1]);
		gc.lineTo(top[0] + hw, top[1] + hh);
		gc.lineTo(top[0], top[1] + FACE_H);
		gc.lineTo(top[0] - hw, top[1] + hh);
		gc.closePath();
		gc.clip();
	}
	
	/**
	 * Instantly moves the camera to center on a specific grid tile.
	 * @param x The row coordinate
	 * @param y The col coordinate
	 */
	private void centerCameraOnTile(int x, int y) {
		double[] centerPt = tileTopPoint(x, y);

		cameraX = centerPt[0];
		cameraY = centerPt[1] + (FACE_H / 2.0);
		render();
	}
	
	/**
	 * Calculates the middle tile of the current board layout and centers the camera
	 * on it.
	 */
	private void centerCameraOnBoard() {
		// Find the middle row and column
		int midRow = getBoard().length / 2;
		int midCol = getBoard()[0].length / 2;

		centerCameraOnTile(midRow, midCol);
	}
	
	/**
	 * Renders movement/attack highlights for the selected unit.
	 * Separate from tile rendering to avoid spaghetti logic.
	 */
	private void drawUnitHighlights(GraphicsContext gc) {
		Unit selected = GameManager.getSelectedUnit();
		if (selected == null) {
			return;
		}
		
		char[][] board = getBoard();
		
		// Get valid move and attack tiles
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				// Skip the unit's own tile
				if (row == selected.getX() && col == selected.getY()) {
					continue;
				}
				
				Color highlightColor = null;
				
				// Determine highlight color based on tile state
				if (GameManager.isEnemyAtTile(row, col, selected)) {
					// Red for enemy unit (attackable)
					if (selected.canAttack(row, col)[0]) { // Simplified check; your canAttack may differ
						highlightColor = Color.web("#FF3333");
					}
				} else if (GameManager.isAllyAtTile(row, col, selected)) {
					// Allied unit blocks movement completely (no highlight)
					continue;
				} else {
					// Empty tile - check if unit can move there
					if (selected.canMoveTo(row, col)[0]) { // Simplified check
						highlightColor = Color.web("#444444"); // Dark grey
					}
				}
				
				// Draw overlay if a highlight color was determined
				if (highlightColor != null) {
					drawTileHighlight(gc, row, col, highlightColor);
				}
			}
		}
	}
	
	/**
	 * Draws a colored overlay on a tile (for movement/attack highlights).
	 */
	private void drawTileHighlight(GraphicsContext gc, int row, int col, Color color) {
		double[] top = tileTopPoint(row, col);
		if (top[0] < 0 || top[1] < 0) {
			return;
		}
		
		gc.setFill(color);
		gc.setGlobalAlpha(0.5); // Semi-transparent
		
		// Draw filled diamond shape (same as tile footprint)
		double x = top[0];
		double y = top[1];
		double[] xs = { x, x + TILE_W / 2, x, x - TILE_W / 2 };
		double[] ys = { y, y + FACE_H, y + 2 * FACE_H, y + FACE_H };
		gc.fillPolygon(xs, ys, 4);
		
		gc.setGlobalAlpha(1.0);
	}

    public static Unit getUnitAtTile(int row, int col) {
        return UNITS_BOARD[row][col];
    }
}