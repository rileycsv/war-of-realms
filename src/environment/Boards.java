package environment;
public class Boards {
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
    public static final char[][][] board = {
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
}
