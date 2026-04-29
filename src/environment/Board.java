package environment;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.Main;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.geometry.Pos;

import utils.Debug;

import java.util.Arrays;

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
		CURRENT_BOARD = index;
		UNITS_BOARD = new Unit[getBoard().length][getBoard()[0].length];
		Debug.log(2, "Active board set to " + index + " with dimensions: " + getBoard().length + " rows x " + getBoard()[0].length + " cols");
	}

	private static Unit[][] UNITS_BOARD = null;

	public static Unit[][] getUnitsBoard() {
		return UNITS_BOARD;
	}

	/**
	 * Returns the current board
	 */
	public static char[][] getBoard() {
		// Debug.log(3, "Accessing board: " + CURRENT_BOARD);
		return Boards.board[CURRENT_BOARD];
	}

	public static int[][] getBoardCosts() {
		final int[][] ret = new int[getBoard().length][getBoard()[0].length];
		for (int r = 0; r < getBoard().length; r++) {
			for (int c = 0; c < getBoard()[r].length; c++) {
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
		return getBoard()[r][c];
	}
	
	// Styles for the end turn button
	private static String endTurnButtonStyle = "-fx-font-size: 16px; -fx-height: 100px; -fx-width: 100px; -fx-padding: 10px 20px; -fx-text-fill: white;";


	// Runtime state
	private static Canvas canvas;

	// Camera & mouse state
	// Defaulting closer to the center of the specific grid layout
	private static double cameraX = ORIGIN_X;
	private static double cameraY = ORIGIN_Y + 100;
	private static double zoom = 3.5;
	private static double max_zoom = 7.0;
	private static double min_zoom = 1;

	private static double lastMouseX = -1;
	private static double lastMouseY = -1;
	private static int hoverRow = -1;
	private static int hoverCol = -1;
	
	/**
	 * Initializes the canvas, sets up input listeners for mouse and keyboard,
	 * and returns the constructed scene.
	 * 
	 * @return The constructed JavaFX Scene containing the interactive board.
	 */
	public static Scene getScene() {
		// Initialize the canvas with the width and height of the board
		canvas = new Canvas(CANVAS_W, CANVAS_H);
		canvas.setFocusTraversable(true);
		
		if (UNITS_BOARD == null) {
			setActiveBoard(CURRENT_BOARD); 
		}

		StackPane root = new StackPane(canvas);
		Button endTurnButton = new Button("End Turn");
		endTurnButton.setId("endTurnButton"); 
		
		// Set the initial background color based on the active player's kingdom color
		endTurnButton.setStyle(endTurnButtonStyle + String.format("-fx-background-color: %s;", GameManager.getActivePlayer().getKingdomColor().toString().replace("0x", "#")));
		StackPane.setAlignment(endTurnButton, Pos.BOTTOM_RIGHT);
		// Initialize the scene with a stack containing the canvas
		Scene scene = new Scene(root, CANVAS_W, CANVAS_H);
		
		root.setStyle("-fx-background-color: #ffffff;");
		root.getChildren().addAll(endTurnButton);
		
		// Bind canvas size to the root layout pane
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		// Rerender the board anytime the canvas dimensions change
		canvas.widthProperty().addListener((obs, oldVal, newVal) -> render());
		canvas.heightProperty().addListener((obs, oldVal, newVal) -> render());

		endTurnButton.setOnAction(e -> {
			GameManager.endTurn();
			endTurnButton.setStyle(endTurnButtonStyle + String.format("-fx-background-color: %s;", GameManager.getActivePlayer().getKingdomColor().toString().replace("0x", "#")));
			canvas.requestFocus(); // Return focus to the canvas after clicking the button
		});
		
		onCanvasCursorMoved();
		
		canvas.setOnMouseClicked(e -> onCanvasClick(e));

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
	 * The mouseClick handler for the canvas
	 */
	private static void onCanvasClick(MouseEvent e) {
		double[] world = screenToWorld(e.getX(), e.getY());
		int[] clickedTile = canvasToTile(world[0], world[1]);

		if (clickedTile[0] == -1 || clickedTile[1] == -1) {
			Debug.log(3, "Clicked outside of board.");
			GameManager.clearSelection();
			render();
			return;
		}

		Unit unitAtTile = getUnitAtTile(clickedTile[0], clickedTile[1]);

		// Setup phase: unit placement only — no selection or movement allowed
		if (GameManager.isSetupTurn()) {
			if (unitAtTile == null && spaceClearAroundTile(clickedTile[0], clickedTile[1])) {
				for (Unit unit : GameManager.getActivePlayer().getUnits(clickedTile[0], clickedTile[1])) {
					if (unit == null) continue;
					int r = unit.getX(), c = unit.getY();
					if (r >= 0 && r < UNITS_BOARD.length && c >= 0 && c < UNITS_BOARD[0].length) {
						UNITS_BOARD[r][c] = unit;
					}
				}
				GameManager.endTurn();
			}
			centerCameraOnTile(clickedTile[0], clickedTile[1]);
			render();
			Main.primaryStage.getScene().lookup("#endTurnButton").setStyle(endTurnButtonStyle + String.format("-fx-background-color: %s;", GameManager.getActivePlayer().getKingdomColor().toString().replace("0x", "#")));
			return; // Never fall through to gameplay logic during setup
		}
		
		// Gameplay phase
		Unit selected = GameManager.getSelectedUnit();
		if (unitAtTile == null) {
			// Clicked empty tile: move selected unit there if it can reach it
			boolean canMove = false;
			canMove = selected != null && selected.canMoveToTile(clickedTile[0], clickedTile[1]);
			
			if (canMove) {
				UNITS_BOARD[selected.getX()][selected.getY()] = null;
				GameManager.moveUnit(selected, clickedTile[0], clickedTile[1]);
				UNITS_BOARD[clickedTile[0]][clickedTile[1]] = selected;
				selected.spendAllMovement();
				GameManager.clearSelection();
			} else {
				Debug.log(2, "Clicked on empty tile at row=" + clickedTile[0] + ", col=" + clickedTile[1]);
				GameManager.clearSelection();
			}
		} else if (unitAtTile.getPlayerID() == GameManager.getActivePlayerID()) {
			// Clicked own unit — select it
			GameManager.setSelectedUnit(unitAtTile);
		} else {
			// Clicked enemy unit — attack if within range
			if (selected != null && GameManager.getUnitCanAttack()[clickedTile[0]][clickedTile[1]]) {
				GameManager.unitAttacks(unitAtTile);
			} else {
				GameManager.clearSelection();
			}
		}
		render();
	}
	
	public static void onCanvasCursorMoved() {
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
	private static double[] screenToWorld(double cx, double cy) {
		// Perfectly mirrors the new render() translations/scaling
		double wx = (cx - CANVAS_W / 2.0) / zoom + cameraX;
		double wy = (cy - CANVAS_H / 2.0) / zoom + cameraY;

		// The -6.5 is needed for some reason since the
		return new double[] { wx, wy - 6.5 };
	}

	/**
	 * Run when canvas is scrolled or mouse is moved to update tile highlights.
	 */
	private static void updateHover() {
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
	private static void render() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		double cw = canvas.getWidth();
		double ch = canvas.getHeight();
		
		gc.setImageSmoothing(false);
		gc.setTransform(1, 0, 0, 1, 0, 0);
		gc.setFill(Color.web("#111111"));
		gc.fillRect(0, 0, cw, ch);
		
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
		
		Unit selected = GameManager.getSelectedUnit();
		if (selected != null && !GameManager.isSetupTurn()) {
			try {
				boolean[][] movMap = selected.canMoveTo();
				boolean[][] atkMap = selected.canAttack();
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < cols; c++) {
						moveHighlight[r][c] = r < movMap.length && c < movMap[r].length && movMap[r][c];
						attackHighlight[r][c] = r < atkMap.length && c < atkMap[r].length && atkMap[r][c];
					}
				}
			} catch (UnsupportedOperationException ignored) {
				Debug.log(2, "Unimplimented canMoveTo/canAttack called for unit " + selected);
			}
		}
		
		// Painter's algorithm: draw diagonal by diagonal (back to front)
		for (int diag = 0; diag < rows + cols; diag++) {
			// Draw tiles and highlights for the CURRENT diagonal
			if (diag < rows + cols - 1) {
				int rMin = Math.max(0, diag - (cols - 1));
				int rMax = Math.min(diag, rows - 1);
				for (int r = rMin; r <= rMax; r++) {
					int c = diag - r;
					// Pass the highlight arrays and the selected unit down to drawTile
					drawTile(gc, r, c);	
				}
			}
			
			// Draw units for the PREVIOUS diagonal (diag - 1)
			// This forces the unit to render AFTER the adjacent forward tiles, 
			// but BEFORE the tile at (r+1, c+1)
			int unitDiag = diag - 1;
			if (unitDiag >= 0) {
				int rMinUnit = Math.max(0, unitDiag - (cols - 1));
				int rMaxUnit = Math.min(unitDiag, rows - 1);
				for (int r = rMinUnit; r <= rMaxUnit; r++) {
					int c = unitDiag - r;
					Unit unitHere = getUnitAtTile(r, c);
					if (unitHere != null) {
						drawUnit(gc, unitHere);
					}
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
	private static void drawTile(GraphicsContext gc, int row, int col) {
		double[] top = tileTopPoint(row, col);
		Image img = getTile(row, col);
		
		if (img != null) {
			double imgW = img.getWidth();
			double imgH = img.getHeight();
			
			double drawX = top[0] - (imgW / 2.0);
			double drawY;
			
			if (imgH <= FACE_H + 2) {
				drawY = top[1];
			} else {
				drawY = (top[1] + SPRITE_H) - imgH;
			}
			
			// --- SPRITE TINTING & SHADING LOGIC ---
			gc.setEffect(resolveTileEffect(row, col));

			gc.drawImage(img, drawX, drawY, imgW, imgH);
			gc.setEffect(null); // Always clear the effect after drawing
			
		} else {
			Debug.log(2, "Using fallback polygon for tile at row=" + row + ", col=" + col);
			// Fallback uses the standard hardcoded dimensions
			double defaultDrawX = top[0] - TILE_W / 2.0;
			double defaultDrawY = top[1];

			gc.save();
			clipToTopFace(gc, top);
			
			Color baseColor = fallbackColor(getBoard()[row][col]);
			
			// Apply color adjustments for fallback polygons
			if (row == hoverRow && col == hoverCol) {
				baseColor = baseColor.deriveColor(0, 1, 1.3, 1);
			} else if (GameManager.getSelectedUnit() != null && !GameManager.isSetupTurn()) {
				Unit unitAtTile = getUnitAtTile(row, col);
				boolean isEnemy = unitAtTile != null && unitAtTile.getPlayerID() != GameManager.getSelectedUnit().getPlayerID();
				boolean isAlly = unitAtTile != null && unitAtTile.getPlayerID() == GameManager.getSelectedUnit().getPlayerID();

				if (isEnemy && GameManager.getUnitCanAttack()[row][col]) {
					baseColor = Color.rgb(255, 100, 100);
				} else if (!GameManager.getUnitCanMoveTo()[row][col] || isAlly) {
					baseColor = baseColor.deriveColor(0, 1, 0.6, 1);
				}
			}

			gc.setFill(baseColor);
			gc.fillRect(defaultDrawX, defaultDrawY, TILE_W, SPRITE_H);
			gc.restore();
		}
	}
	
	/**
	 * Resolves the visual effect to apply to a tile sprite based on game state.
	 * Conditions are evaluated in descending priority order.
	 * Returns null for no effect (normal/natural rendering).
	 */
	private static Effect resolveTileEffect(int row, int col) {
		Unit selectedUnit = GameManager.getSelectedUnit();

		if (selectedUnit != null && !GameManager.isSetupTurn()) {
			Unit unitAtTile  = getUnitAtTile(row, col);
			boolean isEnemy  = unitAtTile != null && unitAtTile.getPlayerID() != selectedUnit.getPlayerID();
			boolean isAlly   = unitAtTile != null && unitAtTile.getPlayerID() == selectedUnit.getPlayerID();
			boolean canAttack  = GameManager.getUnitCanAttack()[row][col];
			boolean canMoveTo  = GameManager.getUnitCanMoveTo()[row][col];
			boolean isSelectedTile = Arrays.equals(selectedUnit.getPos(), new int[]{row, col});

			if (isSelectedTile)				  		return tintBrightness(0.5);   // Selected unit tile: lighten
			if (canAttack && isEnemy)				return tintTile(Color.rgb(245, 85, 85)); // Attackable tile: red
			if (canMoveTo && (isAlly || isEnemy)) 	return tintBrightness(-0.3);  // Reachable but occupied: slight darken
			if (!canMoveTo)							return tintBrightness(-0.5);  // Unreachable tile: darken
			
			return null;															// Reachable empty: no effect
		}

		if (row == hoverRow && col == hoverCol)	{
			return tintBrightness(0.3);   // Hover highlight (no unit selected)
		}

		return null;
	}
	
	/**
	 * Returns a ColorAdjust effect that tints the tile by the given brightness amount
	 * @param brightness
	 * @return
	 */
	private static ColorAdjust tintBrightness(double brightness) {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(brightness);
		return effect;
	}

	/**
	 * Returns a Lighting effect that tints the tile with the given color
	 * @param tintColor
	 * @return
	 */
	private static Lighting tintTile(Color tintColor) {
		Distant light = new Distant();
		light.setColor(tintColor);

		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(0.0); // 0.0 = flat, no bump mapping
		return lighting;
	}
	
	/**
	 * Draws a unit on top of the tile, handling sprite alignment based on the
	 * tile's top edge and the unit sprite's dimensions.
	 */
	public static void drawUnit(GraphicsContext gc, Unit unit) {
		double[] top = tileTopPoint(unit.getX(), unit.getY()); // getX = row, getY = col
		Image img = unit.getImage();
		
		if (img != null) {
			double imgW = img.getWidth();
			double imgH = img.getHeight();
			
			// The number of pixels to nudge the unit UP. 
			// Increase this to move the unit higher, decrease to move it lower.
			double UNIT_Y_OFFSET = 10.0; 
			
			double drawX = top[0] - (imgW / 2.0);
			
			// Subtracting the offset moves the sprite UP in JavaFX
			double drawY = (top[1] + SPRITE_H) - imgH - UNIT_Y_OFFSET; 
			
			double healthRatio = Math.max(0, (double) unit.getHealth() / unit.getMaxHealth()); 
			
			double healthBarWidth = 8;
			double healthBarOffsetX = (imgW - healthBarWidth) / 2.0;

			gc.setFill(Color.ANTIQUEWHITE);
			gc.fillRect(drawX + healthBarOffsetX - .25, drawY - 2, healthBarWidth + 0.5, 1.5);

			// Linear gradient, hue scales from 0 (Red) to 120 (Green)
			gc.setFill(Color.hsb(healthRatio * 120, 0.85, 0.8)); 

			gc.fillRect(drawX + healthBarOffsetX, drawY - 1.75, healthRatio * healthBarWidth, 1);
			gc.drawImage(img, drawX, drawY, imgW, imgH);
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
		Tile tile = getTileObject(row, col);
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
	 * Returns the tile object for a given tile coordinate.
	 * @param row
	 * @param col
	 * @return
	 */
	public static Tile getTileObject(int row, int col) {
		return switch (getBoard()[row][col]) {
			case 'g' -> GRASS_TILE;
			case 'f' -> FOREST_TILE;
			case 'm' -> MOUNTAIN_TILE;
			case 'r' -> RIVER_TILE;
			case 'v' -> VOID_TILE;
			default -> null;
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
		Tile tile = getTileObject(row, col);
		return tile != null ? tile.getCost() : 0;
	}

	/**
	 * Returns the topmost point of a tile, used for positioning tile sprites in the
	 * grid
	 */
	public static double[] tileTopPoint(int row, int col) {
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
	public static int[] canvasToTile(double worldX, double worldY) {
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
	private static void centerCameraOnTile(int x, int y) {
		double[] centerPt = tileTopPoint(x, y);

		cameraX = centerPt[0];
		cameraY = centerPt[1] + (FACE_H / 2.0);
		render();
	}

	/**
	 * Calculates the middle tile of the current board layout and centers the camera
	 * on it.
	 */
	private static void centerCameraOnBoard() {
		// Find the middle row and column
		int midRow = getBoard().length / 2;
		int midCol = getBoard()[0].length / 2;

		centerCameraOnTile(midRow, midCol);
	}

	public static Unit getUnitAtTile(int row, int col) {
		// Debug.log(3, "Getting unit at row=" + row + ", col=" + col + ": " + UNITS_BOARD[row][col]);
		return UNITS_BOARD[row][col];
	}
	
	/**
	 * Returns true if there are no units on the 8 tiles surrounding the given tile coordinates.
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean spaceClearAroundTile(int x, int y) {
		int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1} };
		
		for (int[] dir : directions) {
			int newX = x + dir[0];
			int newY = y + dir[1];
			
			if (newX < 0 || newX >= getBoard().length || newY < 0 || newY >= getBoard()[0].length) {
				Debug.log(2, "Space not clear around row=" + x + ", col=" + y + " because adjacent tile at row=" + newX + ", col=" + newY + " is outside of the board boundaries.");
				return false;
			}
			
			if (UNITS_BOARD[newX][newY] != null) {
				Debug.log(2, "Cannot place unit at row=" + x + ", col=" + y + " because adjacent tile at row=" + newX + ", col=" + newY + " is occupied by unit: " + UNITS_BOARD[newX][newY]);
				return false; // Found a unit in an adjacent tile
			} 
		}

		return true; // No adjacent units found, and no boundaries hit
	}
	
	/**
	 * Creates and displays a popup over the board when one player remains alive. 
	 */
	public static void showEndGameScreen() {
		VBox popup = new VBox();
		popup.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 40px; -fx-border-radius: 10px;");
		popup.setAlignment(Pos.CENTER);
		
		Label endGameLabel = new Label("Game Over! Player " + (GameManager.getActivePlayerID() + 1) + " wins!");
		endGameLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
		Button exitButton = new Button("Exit Game");
		exitButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
		exitButton.setOnAction(e -> Main.exit());

		popup.getChildren().addAll(endGameLabel, exitButton);
				
		Scene scene = canvas.getScene();
		if (scene != null) {
			StackPane root = (StackPane) scene.getRoot();
			root.getChildren().add(popup);
		}
	}
}