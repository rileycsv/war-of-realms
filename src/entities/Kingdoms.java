package entities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Kingdoms {
    private static Map<String, List<Object>> kingdomToImage = Map.of(
        "Hussites", Arrays.asList(),
        "Portuguese", Arrays.asList(),
        "Mongolians", Arrays.asList(),
        "Aztec", Arrays.asList(),
        "English", Arrays.asList()
    );
    
    public static Unit[] getUnitsForKingdom(String kingdom, int playerID) {
        return switch (kingdom) {
            case "Hussites" -> new Unit[] {
                // Infantry: 5
                new Infantry(playerID, kingdom, 0, 0),
                new Infantry(playerID, kingdom, 0, 1),
                new Infantry(playerID, kingdom, 0, 2),
                new Infantry(playerID, kingdom, 0, 3),
                new Infantry(playerID, kingdom, 0, 4),
                // Archer: 1
                new Archer(playerID, kingdom, 1, 0),
                // Cavalry: 1
                new Cavalry(playerID, kingdom, 1, 1),
                // Pikemen: 1
                new Pikeman(playerID, kingdom, 1, 2)
            };
            case "Portuguese" -> new Unit[] {
                // Infantry: 3
                new Infantry(playerID, kingdom, 0, 0),
                new Infantry(playerID, kingdom, 0, 1),
                new Infantry(playerID, kingdom, 0, 2),
                // Archer: 1
                new Archer(playerID, kingdom, 1, 0),
                // Cavalry: 1
                new Cavalry(playerID, kingdom, 1, 1),
                // Pikemen: 3
                new Pikeman(playerID, kingdom, 1, 2),
                new Pikeman(playerID, kingdom, 1, 3),
                new Pikeman(playerID, kingdom, 0, 3)
            };
            case "Mongolians" -> new Unit[] {
                // Infantry: 1
                new Infantry(playerID, kingdom, 0, 0),
                // Archer: 3
                new Archer(playerID, kingdom, 0, 1),
                new Archer(playerID, kingdom, 0, 2),
                new Archer(playerID, kingdom, 0, 3),
                // Cavalry: 3
                new Cavalry(playerID, kingdom, 1, 0),
                new Cavalry(playerID, kingdom, 1, 1),
                new Cavalry(playerID, kingdom, 1, 2),
                // Pikemen: 1
                new Pikeman(playerID, kingdom, 1, 3)
            };
            case "Aztec" -> new Unit[] {
                // Infantry: 3
                new Infantry(playerID, kingdom, 0, 0),
                new Infantry(playerID, kingdom, 0, 1),
                new Infantry(playerID, kingdom, 0, 2),
                // Archer: 2
                new Archer(playerID, kingdom, 0, 3),
                new Archer(playerID, kingdom, 1, 0),
                // Cavalry: 3
                new Cavalry(playerID, kingdom, 1, 1),
                new Cavalry(playerID, kingdom, 1, 2),
                new Cavalry(playerID, kingdom, 1, 3)
            };
            case "English" -> new Unit[] {
                // Infantry: 3
                new Infantry(playerID, kingdom, 0, 0),
                new Infantry(playerID, kingdom, 0, 1),
                new Infantry(playerID, kingdom, 0, 2),
                // Archer: 5
                new Archer(playerID, kingdom, 0, 3),
                new Archer(playerID, kingdom, 1, 0),
                new Archer(playerID, kingdom, 1, 1),
                new Archer(playerID, kingdom, 1, 2),
                new Archer(playerID, kingdom, 1, 3)
            };
            default -> new Unit[] {};
        };
    }
}