package environment;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.effect.ColorAdjust;
import javafx.geometry.Pos;

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
					{ 'g', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'm',
							'g' },
					{ 'g', 'm', 'g', 'g', 'g', 'r', 'r', 'r', 'g', 'g', 'g', 'g', 'r', 'r', 'r', 'g', 'g', 'g', 'm',
							'g' },
					{ 'g', 'g', 'g', 'm', 'g', 'r', 'g', 'r', 'g', 'm', 'm', 'g', 'r', 'g', 'r', 'g', 'm', 'g', 'g',
							'g' },
					{ 'g', 'g', 'g', 'm', 'g', 'r', 'g', 'r', 'g', 'm', 'g', 'g', 'r', 'g', 'r', 'g', 'm', 'g', 'g',
							'g' },
					{ 'g', 'g', 'm', 'm', 'g', 'r', 'g', 'r', 'r', 'r', 'g', 'g', 'r', 'g', 'r', 'r', 'r', 'g', 'g',
							'g' },
					{ 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g',
							'g' },
					{ 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'm', 'm', 'm', 'm', 'g', 'g', 'g', 'g', 'r', 'g', 'g',
							'g' },
					{ 'g', 'm', 'g', 'g', 'g', 'r', 'r', 'r', 'r', 'g', 'g', 'r', 'r', 'r', 'r', 'r', 'r', 'g', 'g',
							'g' },
					{ 'g', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm',
							'g' },
					{ 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm',
							'm' },
					{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g' }
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
					{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
					{ 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'v', 'v', 'v', 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'g', 'g',
							'v', 'g', 'g', 'v', 'v', 'v', 'g', 'g' },
					{ 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'g', 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g',
							'v', 'g', 'v', 'g', 'g', 'g', 'g', 'g' },
					{ 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'g', 'g',
							'v', 'g', 'v', 'g', 'v', 'v', 'v', 'g' },
					{ 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g', 'g', 'g', 'v', 'g', 'g', 'v', 'g', 'v', 'g', 'g',
							'v', 'g', 'v', 'g', 'g', 'g', 'v', 'g' },
					{ 'g', 'v', 'v', 'v', 'g', 'g', 'v', 'v', 'v', 'v', 'g', 'v', 'v', 'v', 'g', 'g', 'g', 'v', 'v',
							'g', 'g', 'g', 'v', 'v', 'v', 'g', 'g' },
					{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
					{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
					{ 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm',
							'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm' },
					{ 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm',
							'm', 'm', 'm', 'm', 'm', 'm', 'm', 'm' }
			}
	};

	/**
	 * Returns the current board
	 */
	public static char[][] getBoard() {
		// Debug.log(3, "Accessing board: " + CURRENT_BOARD);
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
		canvas.setFocusTraversable(true);

		StackPane root = new StackPane(canvas);
		Button endTurnButton = new Button("End Turn");
		endTurnButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
		StackPane.setAlignment(endTurnButton, Pos.BOTTOM_RIGHT);
		// Initialize the scene with a stack containing the canvas
		Scene scene = new Scene(root, CANVAS_W, CANVAS_H);

		root.setStyle("-fx-background-color: #ffffff;");
		root.getChildren().addAll(endTurnButton);

		endTurnButton.setOnAction(e -> {
			GameManager.endTurn();
			canvas.requestFocus(); // Return focus to the canvas after clicking the button
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
			int[] clickedTile = canvasToTile(world[0], world[1]);

			if (GameManager.isSetupTurn()) {
				centerCameraOnTile(clickedTile[0], clickedTile[1]);
			}

			if (clickedTile[0] == -1 || clickedTile[1] == -1) {
				// Clicked outside board
				Debug.log(3, "Clicked outside of board.");
				GameManager.clearSelection();
				return;
			}

			Unit unitAtTile = getUnitAtTile(clickedTile[0], clickedTile[1]);

			// During setup phase, clicking on a tile will place your units around that tile
			if (GameManager.isSetupTurn() && unitAtTile == null) {
				Debug.log(2, "Placing units for player " + GameManager.getActivePlayerID() + " at row=" + clickedTile[0] + ", col=" + clickedTile[1]);
				for (Unit unit : GameManager.getActivePlayer().getUnits(clickedTile[0], clickedTile[1])) {
					UNITS_BOARD[clickedTile[0]][clickedTile[1]] = unit;
				}
				GameManager.endTurn();
				return;
			}

			if (unitAtTile == null) {
				Debug.log(2, "Clicked on empty tile at row=" + clickedTile[0] + ", col=" + clickedTile[1]);
				GameManager.clearSelection();
			} else {
				if (unitAtTile.getPlayerID() == GameManager.getActivePlayerID()) {

					GameManager.setSelectedUnit(unitAtTile);
				} else if ((unitAtTile.canAttackTile(clickedTile[0], clickedTile[1]))
						|| unitAtTile.canMoveToTile(clickedTile[0], clickedTile[1])) {

				} else {
					// Clicked on enemy, empty space, or neutral - deselect
					GameManager.clearSelection();
				}
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

		gc.setImageSmoothing(false);
		gc.setTransform(1, 0, 0, 1, 0, 0);
		gc.setFill(Color.web("#111111"));
		gc.fillRect(0, 0, CANVAS_W, CANVAS_H);

		gc.save();
		gc.translate(CANVAS_W / 2.0, CANVAS_H / 2.0);
		gc.scale(zoom, zoom);
		gc.translate(-cameraX, -cameraY);

		// Build highlight maps once per frame from the selected unit
		char[][] board = getBoard();
		int rows = board.length;
		int cols = board[0].length;

		boolean[][] moveHighlight = new boolean[rows][cols];
		boolean[][] attackHighlight = new boolean[rows][cols];

		Unit selected = GameManager.getSelectedUnit(); // You'll need to add this to GameManager
		if (selected != null) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					moveHighlight[r][c] = selected.canMoveToTile(r, c);
					attackHighlight[r][c] = selected.canAttackTile(r, c);
				}
			}
		}

		// Painter's algorithm: draw tile, then its highlight, then its unit —
		// all at the same diagonal depth so later diagonals correctly overlap earlier
		// ones.
		for (int diag = 0; diag < rows + cols - 1; diag++) {
			int rMin = Math.max(0, diag - (cols - 1));
			int rMax = Math.min(diag, rows - 1);
			for (int r = rMin; r <= rMax; r++) {
				int c = diag - r;
				drawTile(gc, r, c);
				drawTileHighlight(gc, r, c, moveHighlight, attackHighlight, selected);
				Unit unitHere = getUnitAtTile(r, c);
				if (unitHere != null) {
					drawUnit(gc, unitHere);
				}
			}
		}

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

	/**
	 * Draws a unit on top of the tile, handling sprite alignment based on the
	 * tile's top vertex
	 * and the unit sprite's dimensions.
	 * 
	 * @param gc
	 * @param unit
	 */
	public void drawUnit(GraphicsContext gc, Unit unit) {
		double[] top = tileTopPoint(unit.getX(), unit.getY()); // getX = row, getY = col
		Image img = unit.getImage();

		if (img != null) {
			double imgW = img.getWidth();
			double imgH = img.getHeight();
			double drawX = top[0] - (imgW / 2.0);
			double drawY = (top[1] + SPRITE_H) - imgH;
			gc.drawImage(img, drawX, drawY, imgW, imgH);
		}
	}

	/**
	 * Draws a semi-transparent overlay on a tile to indicate move/attack range
	 * or the currently selected unit's position.
	 */
	private void drawTileHighlight(GraphicsContext gc, int row, int col,
			boolean[][] moveHL, boolean[][] attackHL,
			Unit selected) {
		// Determine what color to overlay, if any
		Color overlayColor = null;

		if (selected != null && selected.getX() == row && selected.getY() == col) {
			overlayColor = new Color(1.0, 1.0, 0.0, 0.45); // Yellow: selected unit's tile
		} else if (attackHL[row][col]) {
			overlayColor = new Color(1.0, 0.15, 0.15, 0.40); // Red: attackable
		} else if (moveHL[row][col]) {
			overlayColor = new Color(0.25, 0.65, 1.0, 0.35); // Blue: reachable
		}

		if (overlayColor == null)
			return;

		double[] top = tileTopPoint(row, col);
		double hw = TILE_W / 2.0;
		double hh = FACE_H / 2.0;

		gc.save();

		// Clip to the diamond face so the overlay doesn't bleed onto adjacent tiles
		gc.beginPath();
		gc.moveTo(top[0], top[1]);
		gc.lineTo(top[0] + hw, top[1] + hh);
		gc.lineTo(top[0], top[1] + FACE_H);
		gc.lineTo(top[0] - hw, top[1] + hh);
		gc.closePath();
		gc.clip();

		gc.setFill(overlayColor);
		gc.fillRect(top[0] - hw, top[1], TILE_W, FACE_H);

		gc.restore();
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
	 * 
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

	public static Unit getUnitAtTile(int row, int col) {
		return UNITS_BOARD[row][col];
	}
}