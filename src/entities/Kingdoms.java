package entities;

import utils.Debug;

public class Kingdoms {
    /**
     * Returns an array of Units corresponding to the specified kingdom, player ID, and center position.
     * @param kingdom
     * @param playerID
     * @param cPos
     * @return
     */
    public static Unit[] getUnitsForKingdom(String kingdom, int playerID, int[] cPos) {
        // Unit counts defined as: {Infantry, Archer, Cavalry, Pikeman}
        int[] unitCounts = switch (kingdom) {
            case "Hussites"   -> new int[] {5, 1, 1, 1};
            case "Portuguese" -> new int[] {3, 1, 1, 3};
            case "Mongolians" -> new int[] {1, 3, 3, 1};
            case "Aztec"      -> new int[] {3, 2, 3, 0};
            case "English"    -> new int[] {3, 5, 0, 0};
            default           -> null;
        };
        
        if (unitCounts == null) {
            Debug.log(2, "Invalid kingdom specified. Returning empty unit array.");
            return new Unit[0];
        }
        
        // Define the 3x3 grid offsets around center
        int[][] positions = {
            {-1, -1}, {0, -1}, {1, -1},  // top row
            {-1,  0}, {0,  0}, {1,  0},  // middle row
            {-1,  1}, {0,  1}, {1,  1}   // bottom row
        };
        
        Unit[] units = new Unit[8];
        int posIndex = 0;
        
        // Loop through the unit counts and instantiate them at the correct positions
        for (int type = 0; type < unitCounts.length; type++) {
            for (int i = 0; i < unitCounts[type]; i++) {
                int x = cPos[0] + positions[posIndex][0];
                int y = cPos[1] + positions[posIndex][1];
                
                units[posIndex] = switch (type) {
                    case 0 -> new Infantry(playerID, kingdom, x, y);
                    case 1 -> new Archer(playerID, kingdom, x, y);
                    case 2 -> new Cavalry(playerID, kingdom, x, y);
                    case 3 -> new Pikeman(playerID, kingdom, x, y);
                    default -> throw new IllegalStateException("Unexpected unit type index: " + type);
                };
                
                posIndex++;
            }
        }

        return units;
    }
}