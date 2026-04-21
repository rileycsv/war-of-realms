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
            // The Easy Board - 16x9, Flat plains with a river in the middle and some forests
            { 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'f', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'f', 'f', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'f', 'f', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'r', 'r', 'r', 'g', 'g', 'g', 'g', 'f', 'g' },
            { 'g', 'g', 'g', 'f', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'f', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'f', 'f', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g', 'g' }
        },
        {
            // The Medium Board - Larger, than the last at 18x10, includes impassable mountain ranges and a short river, possibly allowing 'king of the hill' gameplay
			// Also includes a flat area in the bottom middle to allow for army setup if the river embankment has been captured
            { 'g', 'g', 'g', 'm', 'g', 'r', 'g', 'g', 'g', 'f', 'g', 'g', 'r', 'r', 'g', 'm', 'm', 'm' },
            { 'g', 'g', 'g', 'm', 'g', 'r', 'g', 'g', 'f', 'f', 'f', 'g', 'r', 'f', 'g', 'g', 'm', 'g' },
            { 'g', 'g', 'g', 'm', 'g', 'r', 'r', 'g', 'g', 'f', 'g', 'r', 'r', 'f', 'f', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'r', 'r', 'g', 'r', 'r', 'g', 'f', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'f', 'f', 'g', 'g', 'g', 'r', 'r', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'f', 'f', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'm', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'm', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'm', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'm', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'm', 'm', 'm', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'm', 'm', 'g', 'g' }
        },
        {
            // The Hard Board - Even larger 20x11 map, allowing for longer games. Two rivers and thin mountain passes, allowing for horrible choke-points
            { 'g', 'g', 'g', 'g', 'm', 'r', 'g', 'g', 'g', 'g', 'g', 'm', 'r', 'g', 'm', 'm', 'm', 'm', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'm', 'r', 'g', 'g', 'f', 'f', 'g', 'm', 'r', 'r', 'g', 'm', 'm', 'm', 'g', 'g' },
            { 'g', 'f', 'f', 'g', 'm', 'r', 'g', 'g', 'f', 'm', 'm', 'f', 'g', 'r', 'r', 'g', 'm', 'm', 'g', 'g' },
            { 'g', 'f', 'f', 'g', 'm', 'r', 'g', 'g', 'f', 'm', 'm', 'f', 'g', 'g', 'r', 'g', 'g', 'm', 'g', 'g' },
            { 'g', 'g', 'f', 'g', 'f', 'r', 'g', 'g', 'g', 'g', 'f', 'f', 'g', 'r', 'r', 'g', 'f', 'f', 'f', 'g' },
            { 'g', 'g', 'g', 'g', 'f', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'f', 'f', 'f', 'g' },
            { 'g', 'g', 'g', 'g', 'm', 'r', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'm', 'g', 'g' },
            { 'g', 'g', 'm', 'm', 'm', 'r', 'r', 'r', 'r', 'g', 'g', 'm', 'r', 'r', 'm', 'm', 'm', 'm', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'm', 'm', 'm', 'r', 'g', 'm', 'r', 'r', 'f', 'g', 'g', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'f', 'g', 'r', 'g', 'g', 'r', 'f', 'f', 'g', 'g', 'g', 'g', 'g', 'g' },
            { 'g', 'g', 'g', 'g', 'g', 'g', 'f', 'f', 'r', 'r', 'r', 'r', 'f', 'f', 'm', 'm', 'm', 'm', 'g', 'g' }
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
