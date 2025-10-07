package base;

import java.util.*;

/**
 * Factory for creating game levels.
 */
public class LevelFactory {
    
    /**
     * Create a level by floor number.
     */
    public static Level createLevel(int floorNumber) {
        switch (floorNumber) {
            case 0:
                return createLevel0();
            case 1:
                return createLevel1();
            case 2:
                return createLevel2();
            case 3:
                return createLevel3();
            case 4:
                return createLevel4();
            case 5:
                return createLevel5();
            case 6:
                return createLevel6();
            case 7:
                return createLevel7();
            case 8:
                return createLevel8();
            case 9:
                return createLevel9();
            default:
                throw new IllegalArgumentException("Invalid floor number: " + floorNumber);
        }
    }
    
    /**
     * Create Level 0 (tutorial/starting level).
     */
    private static Level createLevel0() {
        Set<Level.Position> walls = new HashSet<>();
        Map<Level.Position, Level.Chest> chests = new HashMap<>();
        Map<Level.Position, Mob> mobs = new HashMap<>();
        
        // Add border walls
        addBorderWalls(walls);
        
        // Add interior walls
        addWallRange(walls, 11, 20, 1, 1);
        addWallRange(walls, 16, 20, 3, 3);
        addWallRange(walls, 20, 27, 4, 4);
        addWallRange(walls, 19, 29, 5, 5);
        addWallRange(walls, 2, 9, 6, 6);
        addWallRange(walls, 8, 14, 7, 7);
        addWallRange(walls, 1, 6, 8, 8);
        addWallRange(walls, 10, 22, 9, 9);
        addWallRange(walls, 11, 21, 10, 10);
        addWallRange(walls, 26, 28, 10, 10);
        addWallRange(walls, 14, 20, 11, 11);
        addWallRange(walls, 25, 28, 11, 11);
        addWallRange(walls, 6, 22, 17, 17);
        addWallRange(walls, 5, 21, 18, 18);
        addWallRange(walls, 26, 28, 18, 18);
        addWallRange(walls, 5, 8, 19, 19);
        addWallRange(walls, 9, 22, 19, 19);
        addWallRange(walls, 25, 28, 19, 19);
        addWallRange(walls, 19, 28, 23, 23);
        addWallRange(walls, 1, 9, 26, 26);
        addWallRange(walls, 1, 8, 27, 27);
        addWallRange(walls, 9, 20, 27, 27);
        addWallRange(walls, 1, 18, 28, 28);
        
        // Add specific wall positions
        walls.add(new Level.Position(7, 1));
        walls.add(new Level.Position(27, 1));
        walls.add(new Level.Position(7, 2));
        walls.add(new Level.Position(3, 2));
        walls.add(new Level.Position(4, 2));
        walls.add(new Level.Position(9, 2));
        walls.add(new Level.Position(11, 2));
        walls.add(new Level.Position(27, 2));
        walls.add(new Level.Position(2, 3));
        walls.add(new Level.Position(5, 3));
        walls.add(new Level.Position(7, 3));
        walls.add(new Level.Position(9, 3));
        walls.add(new Level.Position(11, 3));
        walls.add(new Level.Position(13, 3));
        walls.add(new Level.Position(14, 3));
        walls.add(new Level.Position(27, 3));
        walls.add(new Level.Position(7, 4));
        walls.add(new Level.Position(9, 4));
        walls.add(new Level.Position(13, 4));
        walls.add(new Level.Position(17, 4));
        walls.add(new Level.Position(9, 5));
        walls.add(new Level.Position(13, 5));
        walls.add(new Level.Position(17, 5));
        walls.add(new Level.Position(13, 6));
        walls.add(new Level.Position(17, 6));
        walls.add(new Level.Position(19, 6));
        walls.add(new Level.Position(23, 6));
        walls.add(new Level.Position(24, 6));
        walls.add(new Level.Position(28, 6));
        walls.add(new Level.Position(16, 7));
        walls.add(new Level.Position(17, 7));
        walls.add(new Level.Position(19, 7));
        walls.add(new Level.Position(28, 7));
        walls.add(new Level.Position(8, 8));
        walls.add(new Level.Position(17, 8));
        walls.add(new Level.Position(19, 8));
        walls.add(new Level.Position(28, 8));
        walls.add(new Level.Position(8, 9));
        walls.add(new Level.Position(25, 9));
        walls.add(new Level.Position(26, 9));
        walls.add(new Level.Position(27, 9));
        walls.add(new Level.Position(28, 9));
        walls.add(new Level.Position(6, 10));
        walls.add(new Level.Position(7, 10));
        walls.add(new Level.Position(8, 10));
        walls.add(new Level.Position(10, 10));
        walls.add(new Level.Position(22, 10));
        walls.add(new Level.Position(25, 10));
        walls.add(new Level.Position(6, 11));
        walls.add(new Level.Position(7, 11));
        walls.add(new Level.Position(8, 11));
        walls.add(new Level.Position(10, 11));
        walls.add(new Level.Position(11, 11));
        walls.add(new Level.Position(12, 11));
        walls.add(new Level.Position(22, 11));
        walls.add(new Level.Position(6, 12));
        walls.add(new Level.Position(12, 12));
        walls.add(new Level.Position(14, 12));
        walls.add(new Level.Position(20, 12));
        walls.add(new Level.Position(22, 12));
        walls.add(new Level.Position(25, 12));
        walls.add(new Level.Position(6, 13));
        walls.add(new Level.Position(12, 13));
        walls.add(new Level.Position(13, 13));
        walls.add(new Level.Position(14, 13));
        walls.add(new Level.Position(20, 13));
        walls.add(new Level.Position(21, 13));
        walls.add(new Level.Position(22, 13));
        walls.add(new Level.Position(28, 13));
        walls.add(new Level.Position(1, 14));
        walls.add(new Level.Position(2, 14));
        walls.add(new Level.Position(4, 14));
        walls.add(new Level.Position(5, 14));
        walls.add(new Level.Position(6, 14));
        walls.add(new Level.Position(2, 15));
        walls.add(new Level.Position(4, 15));
        walls.add(new Level.Position(6, 15));
        walls.add(new Level.Position(12, 15));
        walls.add(new Level.Position(13, 15));
        walls.add(new Level.Position(14, 15));
        walls.add(new Level.Position(20, 15));
        walls.add(new Level.Position(21, 15));
        walls.add(new Level.Position(22, 15));
        walls.add(new Level.Position(28, 15));
        walls.add(new Level.Position(2, 16));
        walls.add(new Level.Position(4, 16));
        walls.add(new Level.Position(6, 16));
        walls.add(new Level.Position(12, 16));
        walls.add(new Level.Position(14, 16));
        walls.add(new Level.Position(20, 16));
        walls.add(new Level.Position(22, 16));
        walls.add(new Level.Position(25, 16));
        walls.add(new Level.Position(2, 17));
        walls.add(new Level.Position(4, 17));
        walls.add(new Level.Position(25, 17));
        walls.add(new Level.Position(26, 17));
        walls.add(new Level.Position(27, 17));
        walls.add(new Level.Position(28, 17));
        walls.add(new Level.Position(2, 18));
        walls.add(new Level.Position(4, 18));
        walls.add(new Level.Position(22, 18));
        walls.add(new Level.Position(25, 18));
        walls.add(new Level.Position(2, 19));
        walls.add(new Level.Position(4, 19));
        walls.add(new Level.Position(1, 20));
        walls.add(new Level.Position(2, 20));
        walls.add(new Level.Position(4, 20));
        walls.add(new Level.Position(5, 20));
        walls.add(new Level.Position(6, 20));
        walls.add(new Level.Position(9, 20));
        walls.add(new Level.Position(17, 20));
        walls.add(new Level.Position(19, 20));
        walls.add(new Level.Position(28, 20));
        walls.add(new Level.Position(6, 21));
        walls.add(new Level.Position(9, 21));
        walls.add(new Level.Position(17, 21));
        walls.add(new Level.Position(19, 21));
        walls.add(new Level.Position(28, 21));
        walls.add(new Level.Position(6, 22));
        walls.add(new Level.Position(7, 22));
        walls.add(new Level.Position(8, 22));
        walls.add(new Level.Position(9, 22));
        walls.add(new Level.Position(17, 22));
        walls.add(new Level.Position(19, 22));
        walls.add(new Level.Position(23, 22));
        walls.add(new Level.Position(24, 22));
        walls.add(new Level.Position(28, 22));
        walls.add(new Level.Position(17, 23));
        walls.add(new Level.Position(6, 24));
        walls.add(new Level.Position(7, 24));
        walls.add(new Level.Position(8, 24));
        walls.add(new Level.Position(9, 24));
        walls.add(new Level.Position(17, 24));
        walls.add(new Level.Position(18, 24));
        walls.add(new Level.Position(19, 24));
        walls.add(new Level.Position(20, 24));
        walls.add(new Level.Position(25, 24));
        walls.add(new Level.Position(26, 24));
        walls.add(new Level.Position(6, 25));
        walls.add(new Level.Position(9, 25));
        walls.add(new Level.Position(20, 25));
        walls.add(new Level.Position(24, 25));
        walls.add(new Level.Position(25, 25));
        walls.add(new Level.Position(28, 25));
        walls.add(new Level.Position(17, 26));
        walls.add(new Level.Position(18, 26));
        walls.add(new Level.Position(24, 27));
        walls.add(new Level.Position(25, 27));
        walls.add(new Level.Position(28, 27));
        walls.add(new Level.Position(19, 28));
        walls.add(new Level.Position(20, 28));
        walls.add(new Level.Position(25, 28));
        walls.add(new Level.Position(26, 28));
        
        // Add chests
        chests.put(new Level.Position(22, 2), createChest(1, 0));
        chests.put(new Level.Position(25, 2), createChest(2, 0));
        chests.put(new Level.Position(21, 7), createChest(3, 0));
        chests.put(new Level.Position(26, 7), createChest(4, 0));
        chests.put(new Level.Position(26, 14), createChest(5, 0));
        chests.put(new Level.Position(11, 21), createChest(6, 0));
        chests.put(new Level.Position(21, 21), createChest(7, 0));
        chests.put(new Level.Position(26, 21), createChest(8, 0));
        chests.put(new Level.Position(2, 22), createChest(9, 0));
        chests.put(new Level.Position(2, 24), createChest(10, 0));
        chests.put(new Level.Position(11, 25), createChest(11, 0));
        chests.put(new Level.Position(3, 3), createChest(12, 0));
        chests.put(new Level.Position(4, 3), createChest(13, 0));
        
        // Add mobs (fixed positions to avoid walls)
        mobs.put(new Level.Position(11, 5), Mob.createMob(Mob.MobType.SMALL, 0)); 
        mobs.put(new Level.Position(15, 5), Mob.createMob(Mob.MobType.SMALL, 0)); 
        mobs.put(new Level.Position(3, 11), Mob.createMob(Mob.MobType.SMALL, 0)); 
        mobs.put(new Level.Position(3, 23), Mob.createMob(Mob.MobType.SMALL, 0)); 
        mobs.put(new Level.Position(9, 14), Mob.createMob(Mob.MobType.SMALL, 0)); 
        mobs.put(new Level.Position(17, 14), Mob.createMob(Mob.MobType.LARGE, 0)); 
        mobs.put(new Level.Position(13, 23), Mob.createMob(Mob.MobType.SMALL, 0));
        
        Level.Position spawn = new Level.Position(1, 1);
        Level.Position exit = new Level.Position(28, 26);
        
        // Boss directly in front of exit (one tile away)
        mobs.put(new Level.Position(27, 26), Mob.createMob(Mob.MobType.BOSS, 0));
        
        // Fill unreachable cells with walls
        fillUnreachableCells(walls, chests, spawn, exit);
        
        return new Level(0, walls, chests, mobs, exit, spawn);
    }
    
    /**
     * Create Level 1.
     */
    private static Level createLevel1() {
        Set<Level.Position> walls = new HashSet<>();
        Map<Level.Position, Level.Chest> chests = new HashMap<>();
        Map<Level.Position, Mob> mobs = new HashMap<>();
        
        addBorderWalls(walls);
        
        // Add walls
        walls.add(new Level.Position(9, 1));
        walls.add(new Level.Position(13, 1));
        walls.add(new Level.Position(20, 1));
        walls.add(new Level.Position(28, 1));
        walls.add(new Level.Position(16, 1));
        walls.add(new Level.Position(17, 1));
        
        // walls.add(new Level.Position(9, 2));
        walls.add(new Level.Position(13, 2));
        walls.add(new Level.Position(20, 2));
        walls.add(new Level.Position(28, 2));
        walls.add(new Level.Position(18, 2));
        walls.add(new Level.Position(15, 2));
        
        addWallRange(walls, 4, 9, 3, 3);
        walls.add(new Level.Position(13, 3));
        walls.add(new Level.Position(14, 3));
        walls.add(new Level.Position(15, 3));
        walls.add(new Level.Position(18, 3));
        walls.add(new Level.Position(19, 3));
        walls.add(new Level.Position(20, 3));
        walls.add(new Level.Position(28, 3));
        
        walls.add(new Level.Position(3, 4));
        addWallRange(walls, 20, 25, 4, 4);
        walls.add(new Level.Position(27, 4));
        walls.add(new Level.Position(28, 4));
        
        walls.add(new Level.Position(2, 5));
        addWallRange(walls, 5, 9, 5, 5);
        addWallRange(walls, 13, 18, 5, 5);
        addWallRange(walls, 20, 25, 5, 5);
        walls.add(new Level.Position(27, 5));
        walls.add(new Level.Position(28, 5));
        
        walls.add(new Level.Position(1, 6));
        walls.add(new Level.Position(4, 6));
        walls.add(new Level.Position(5, 6));
        walls.add(new Level.Position(9, 6));
        walls.add(new Level.Position(13, 6));
        walls.add(new Level.Position(18, 6));
        walls.add(new Level.Position(20, 6));
        walls.add(new Level.Position(21, 6));
        
        walls.add(new Level.Position(3, 7));
        walls.add(new Level.Position(7, 7));
        walls.add(new Level.Position(9, 7));
        walls.add(new Level.Position(13, 7));
        walls.add(new Level.Position(17, 7));
        walls.add(new Level.Position(18, 7));
        walls.add(new Level.Position(20, 7));
        walls.add(new Level.Position(21, 7));
        
        walls.add(new Level.Position(2, 8));
        walls.add(new Level.Position(3, 8));
        walls.add(new Level.Position(7, 8));
        walls.add(new Level.Position(11, 8));
        walls.add(new Level.Position(12, 8));
        walls.add(new Level.Position(13, 8));
        walls.add(new Level.Position(17, 8));
        walls.add(new Level.Position(21, 8));
        
        walls.add(new Level.Position(3, 9));
        addWallRange(walls, 7, 11, 9, 9);
        walls.add(new Level.Position(17, 9));
        
        walls.add(new Level.Position(1, 10));
        addWallRange(walls, 4, 8, 10, 10);
        walls.add(new Level.Position(13, 10));
        walls.add(new Level.Position(17, 10));
        walls.add(new Level.Position(21, 10));
        
        walls.add(new Level.Position(2, 11));
        walls.add(new Level.Position(5, 11));
        walls.add(new Level.Position(8, 11));
        walls.add(new Level.Position(12, 11));
        walls.add(new Level.Position(14, 11));
        addWallRange(walls, 17, 23, 11, 11);
        addWallRange(walls, 25, 28, 11, 11);
        
        walls.add(new Level.Position(3, 12));
        walls.add(new Level.Position(6, 12));
        walls.add(new Level.Position(8, 12));
        walls.add(new Level.Position(12, 12));
        walls.add(new Level.Position(15, 12));
        walls.add(new Level.Position(21, 12));
        walls.add(new Level.Position(22, 12));
        walls.add(new Level.Position(26, 12));
        
        walls.add(new Level.Position(4, 13));
        walls.add(new Level.Position(7, 13));
        walls.add(new Level.Position(8, 13));
        walls.add(new Level.Position(12, 13));
        walls.add(new Level.Position(13, 13));
        walls.add(new Level.Position(16, 13));
        walls.add(new Level.Position(21, 13));
        walls.add(new Level.Position(26, 13));
        walls.add(new Level.Position(28, 13));
        
        walls.add(new Level.Position(5, 14));
        walls.add(new Level.Position(8, 14));
        walls.add(new Level.Position(14, 14));
        walls.add(new Level.Position(17, 14));
        walls.add(new Level.Position(21, 14));
        
        walls.add(new Level.Position(6, 15));
        walls.add(new Level.Position(8, 15));
        walls.add(new Level.Position(12, 15));
        walls.add(new Level.Position(15, 15));
        walls.add(new Level.Position(18, 15));
        walls.add(new Level.Position(21, 15));
        walls.add(new Level.Position(26, 15));
        walls.add(new Level.Position(28, 15));
        
        walls.add(new Level.Position(5, 16));
        walls.add(new Level.Position(8, 16));
        walls.add(new Level.Position(13, 16));
        walls.add(new Level.Position(16, 16));
        walls.add(new Level.Position(18, 16));
        walls.add(new Level.Position(19, 16));
        walls.add(new Level.Position(20, 16));
        walls.add(new Level.Position(21, 16));
        walls.add(new Level.Position(22, 16));
        walls.add(new Level.Position(26, 16));
        
        walls.add(new Level.Position(4, 17));
        walls.add(new Level.Position(7, 17));
        walls.add(new Level.Position(8, 17));
        walls.add(new Level.Position(9, 17));
        walls.add(new Level.Position(13, 17));
        walls.add(new Level.Position(14, 17));
        walls.add(new Level.Position(16, 17));
        walls.add(new Level.Position(17, 17));
        walls.add(new Level.Position(23, 17));
        walls.add(new Level.Position(25, 17));
        walls.add(new Level.Position(26, 17));
        walls.add(new Level.Position(27, 17));
        
        walls.add(new Level.Position(3, 18));
        walls.add(new Level.Position(6, 18));
        walls.add(new Level.Position(8, 18));
        walls.add(new Level.Position(10, 18));
        walls.add(new Level.Position(13, 18));
        walls.add(new Level.Position(16, 18));
        walls.add(new Level.Position(19, 18));
        walls.add(new Level.Position(20, 18));
        walls.add(new Level.Position(21, 18));
        walls.add(new Level.Position(24, 18));
        walls.add(new Level.Position(28, 18));
        
        walls.add(new Level.Position(2, 19));
        walls.add(new Level.Position(5, 19));
        walls.add(new Level.Position(8, 19));
        walls.add(new Level.Position(11, 19));
        walls.add(new Level.Position(12, 19));
        walls.add(new Level.Position(15, 19));
        walls.add(new Level.Position(18, 19));
        walls.add(new Level.Position(22, 19));
        walls.add(new Level.Position(26, 19));
        walls.add(new Level.Position(28, 19));
        
        walls.add(new Level.Position(1, 20));
        walls.add(new Level.Position(4, 20));
        walls.add(new Level.Position(8, 20));
        walls.add(new Level.Position(14, 20));
        walls.add(new Level.Position(17, 20));
        walls.add(new Level.Position(20, 20));
        walls.add(new Level.Position(23, 20));
        walls.add(new Level.Position(25, 20));
        walls.add(new Level.Position(28, 20));
        
        walls.add(new Level.Position(1, 21));
        addWallRange(walls, 3, 8, 21, 21);
        walls.add(new Level.Position(12, 21));
        walls.add(new Level.Position(13, 21));
        walls.add(new Level.Position(16, 21));
        walls.add(new Level.Position(19, 21));
        walls.add(new Level.Position(20, 21));
        walls.add(new Level.Position(21, 21));
        walls.add(new Level.Position(24, 21));
        walls.add(new Level.Position(27, 21));
        
        walls.add(new Level.Position(8, 22));
        walls.add(new Level.Position(12, 22));
        walls.add(new Level.Position(15, 22));
        walls.add(new Level.Position(18, 22));
        walls.add(new Level.Position(22, 22));
        walls.add(new Level.Position(26, 22));
        
        walls.add(new Level.Position(1, 23));
        walls.add(new Level.Position(2, 23));
        walls.add(new Level.Position(3, 23));
        walls.add(new Level.Position(4, 23));
        walls.add(new Level.Position(14, 23));
        walls.add(new Level.Position(17, 23));
        addWallRange(walls, 23, 28, 23, 23);
        
        walls.add(new Level.Position(4, 24));
        walls.add(new Level.Position(8, 24));
        walls.add(new Level.Position(12, 24));
        walls.add(new Level.Position(13, 24));
        walls.add(new Level.Position(14, 24));
        walls.add(new Level.Position(16, 24));
        walls.add(new Level.Position(26, 24));
        
        addWallRange(walls, 4, 14, 25, 25);
        addWallRange(walls, 16, 26, 25, 25);
        
        addWallRange(walls, 4, 26, 27, 27);
        walls.add(new Level.Position(28, 27));
        walls.add(new Level.Position(28, 25));
        
        walls.add(new Level.Position(4, 28));
        addWallRange(walls, 5, 26, 28, 28);
        walls.add(new Level.Position(26, 28));
        
        // Add chests
        chests.put(new Level.Position(22, 2), createChest(1, 1));
        chests.put(new Level.Position(24, 2), createChest(2, 1));
        chests.put(new Level.Position(26, 2), createChest(3, 1));
        chests.put(new Level.Position(11, 4), createChest(4, 1));
        chests.put(new Level.Position(5, 8), createChest(5, 1));
        chests.put(new Level.Position(11, 17), createChest(6, 1));
        chests.put(new Level.Position(6, 23), createChest(7, 1));
        chests.put(new Level.Position(2, 25), createChest(8, 1));
        chests.put(new Level.Position(2, 27), createChest(9, 1));
        chests.put(new Level.Position(19, 9), createChest(10, 1));
        chests.put(new Level.Position(23, 7), createChest(11, 1));
        chests.put(new Level.Position(25, 7), createChest(12, 1));
        chests.put(new Level.Position(27, 7), createChest(13, 1));
        chests.put(new Level.Position(28, 24), createChest(14, 1));
        chests.put(new Level.Position(28, 26), createChest(15, 1));
        chests.put(new Level.Position(28, 28), createChest(16, 1));
        
        Level.Position spawn = new Level.Position(1, 1);
        Level.Position exit = new Level.Position(28, 14);
        
        // Add stronger mobs
        mobs.put(new Level.Position(10, 11), Mob.createMob(Mob.MobType.SMALL, 1));
        mobs.put(new Level.Position(19, 13), Mob.createMob(Mob.MobType.LARGE, 1));
        mobs.put(new Level.Position(23, 8), Mob.createMob(Mob.MobType.LARGE, 1));
        mobs.put(new Level.Position(25, 8), Mob.createMob(Mob.MobType.LARGE, 1));
        mobs.put(new Level.Position(27, 8), Mob.createMob(Mob.MobType.LARGE, 1));
        mobs.put(new Level.Position(24, 14), Mob.createMob(Mob.MobType.LARGE, 1));
        
        // Boss directly in front of exit (one tile away)
        mobs.put(new Level.Position(27, 14), Mob.createMob(Mob.MobType.BOSS, 1));
        
        // Fill unreachable cells with walls
        fillUnreachableCells(walls, chests, spawn, exit);
        
        return new Level(1, walls, chests, mobs, exit, spawn);
    }
    
    // Levels 6-9 similar pattern...
    private static Level createLevel2() { return createGenericLevel(2); }
    private static Level createLevel3() { return createGenericLevel(3); }
    private static Level createLevel4() { return createGenericLevel(4); }
    private static Level createLevel5() { return createGenericLevel(5); }
    private static Level createLevel6() { return createGenericLevel(6); }
    private static Level createLevel7() { return createGenericLevel(7); }
    private static Level createLevel8() { return createGenericLevel(8); }
    private static Level createLevel9() { return createGenericLevel(9); }
    
    /**
     * Create a generic level template (can be customized per floor).
     */
    private static Level createGenericLevel(int floorNumber) {
        Set<Level.Position> walls = new HashSet<>();
        Map<Level.Position, Level.Chest> chests = new HashMap<>();
        Map<Level.Position, Mob> mobs = new HashMap<>();
        
        addBorderWalls(walls);
        
        // Add many more random walls for proper maze structure (tripled from 120 to 360)
        Random rand = new Random(floorNumber * 100); // Seed for consistency
        for (int i = 0; i < 360; i++) {
            int x = rand.nextInt(Constants.BOARD_WIDTH - 2) + 1;
            int y = rand.nextInt(Constants.BOARD_HEIGHT - 2) + 1;
            walls.add(new Level.Position(x, y));
        }
        
        // Add more random chests (increased from 5 to 8)
        for (int i = 0; i < 8; i++) {
            Level.Position pos = findSafePosition(rand, walls, chests, mobs, 100);
            if (pos != null) {
                chests.put(pos, createChest(i, floorNumber));
            }
        }
        
        // Further reduce initial mob count (now just 1 + floorNumber/3)
        // Auto-spawn will add some more, but sparingly
        int mobCount = 1 + floorNumber / 3;
        for (int i = 0; i < mobCount; i++) {
            Level.Position pos = findSafePosition(rand, walls, chests, mobs, 100);
            if (pos != null) {
                Mob.MobType type = (i % 3 == 0) ? Mob.MobType.LARGE : Mob.MobType.SMALL;
                mobs.put(pos, Mob.createMob(type, floorNumber));
            }
        }
        
        Level.Position spawn = new Level.Position(1, 1);
        // Move exit away from border (was 29,29 which is a wall)
        Level.Position exit = new Level.Position(27, 27);
        
        // Boss directly in front of exit (one tile away)
        Level.Position bossPos = new Level.Position(26, 27);  // One tile to the left of exit
        if (!walls.contains(bossPos) && !chests.containsKey(bossPos)) {
            mobs.put(bossPos, Mob.createMob(Mob.MobType.BOSS, floorNumber));
        } else {
            // Try one tile up from exit
            bossPos = new Level.Position(27, 26);
            if (!walls.contains(bossPos) && !chests.containsKey(bossPos)) {
                mobs.put(bossPos, Mob.createMob(Mob.MobType.BOSS, floorNumber));
            } else {
                // Find alternative position near exit (but stay within bounds)
                boolean bossPlaced = false;
                for (int dx = -2; dx <= 0 && !bossPlaced; dx++) {
                    for (int dy = -2; dy <= 0 && !bossPlaced; dy++) {
                        if (dx == 0 && dy == 0) continue;
                        Level.Position altPos = new Level.Position(27 + dx, 27 + dy);
                        if (!walls.contains(altPos) && !chests.containsKey(altPos) && !mobs.containsKey(altPos)) {
                            mobs.put(altPos, Mob.createMob(Mob.MobType.BOSS, floorNumber));
                            bossPlaced = true;
                        }
                    }
                }
            }
        }
        
        // Fill unreachable cells with walls
        fillUnreachableCells(walls, chests, spawn, exit);
        
        // Spawn fewer mobs in open 3x3 spaces (reduced probability)
        spawnMobsInOpenSpaces(walls, chests, mobs, floorNumber);
        
        return new Level(floorNumber, walls, chests, mobs, exit, spawn);
    }
    
    /**
     * Spawn mobs in open 3x3 spaces (no walls in the 3x3 area).
     * Chests are considered free space - only walls block mob spawning.
     * A mob spawns in EVERY valid 3x3 open space.
     */
    private static void spawnMobsInOpenSpaces(Set<Level.Position> walls, 
                                             Map<Level.Position, Level.Chest> chests,
                                             Map<Level.Position, Mob> mobs,
                                             int floorNumber) {
        // Check every possible 3x3 space
        for (int centerX = 2; centerX < Constants.BOARD_WIDTH - 2; centerX++) {
            for (int centerY = 2; centerY < Constants.BOARD_HEIGHT - 2; centerY++) {
                Level.Position center = new Level.Position(centerX, centerY);
                
                // Skip if there's already a chest at center
                if (chests.containsKey(center)) {
                    continue;
                }
                
                // Skip if there's already a mob at center
                if (mobs.containsKey(center)) {
                    continue;
                }
                
                // Check if entire 3x3 area is clear of walls (chests are OK)
                boolean isOpenSpace = true;
                for (int dx = -1; dx <= 1 && isOpenSpace; dx++) {
                    for (int dy = -1; dy <= 1 && isOpenSpace; dy++) {
                        Level.Position pos = new Level.Position(centerX + dx, centerY + dy);
                        if (walls.contains(pos)) {
                            isOpenSpace = false;
                        }
                    }
                }
                
                // If it's an open 3x3 space, ALWAYS spawn a mob (100% probability)
                if (isOpenSpace) {
                    // Randomly decide mob type (70% small, 30% large)
                    Mob.MobType type = (Math.random() < 0.7) ? Mob.MobType.SMALL : Mob.MobType.LARGE;
                    mobs.put(center, Mob.createMob(type, floorNumber));
                }
            }
        }
    }
    
    // Helper methods
    
    /**
     * Add border walls around the entire map.
     */
    private static void addBorderWalls(Set<Level.Position> walls) {
        // Top and bottom borders
        for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
            walls.add(new Level.Position(x, 0));
            walls.add(new Level.Position(x, Constants.BOARD_HEIGHT - 1));
        }
        
        // Left and right borders
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            walls.add(new Level.Position(0, y));
            walls.add(new Level.Position(Constants.BOARD_WIDTH - 1, y));
        }
    }
    
    /**
     * Add a horizontal range of walls.
     */
    private static void addWallRange(Set<Level.Position> walls, int startX, int endX, int y, int yEnd) {
        for (int x = startX; x <= endX; x++) {
            for (int yPos = y; yPos <= yEnd; yPos++) {
                walls.add(new Level.Position(x, yPos));
            }
        }
    }
    
    /**
     * Create a chest with random loot.
     */
    private static Level.Chest createChest(int chestId, int floorLevel) {
        List<Equipment> items = new ArrayList<>();
        Random rand = new Random(chestId * 1000 + floorLevel);
        
        // ALWAYS add at least one health potion
        items.add(EquipmentFactory.createPotion());
        
        // Generate 1-3 additional items
        int itemCount = rand.nextInt(3) + 1;
        for (int i = 0; i < itemCount; i++) {
            int itemType = rand.nextInt(10);
            
            if (itemType < 4) {
                // Armor (40% chance - increased from 30%)
                Equipment.EquipmentType[] armorTypes = {
                    Equipment.EquipmentType.HEAD,
                    Equipment.EquipmentType.ARMOR,
                    Equipment.EquipmentType.BELT,
                    Equipment.EquipmentType.BOOTS
                };
                Equipment.EquipmentType type = armorTypes[rand.nextInt(armorTypes.length)];
                Equipment.Rarity rarity = EquipmentFactory.generateRarity(floorLevel * 3);
                items.add(EquipmentFactory.createArmor(type, floorLevel * 3, rarity));
                
            } else if (itemType < 8) {
                // Additional Potion (40% chance - increased from 30%)
                items.add(EquipmentFactory.createPotion());
                
            } else {
                // Rune (20% chance - reduced from 40%)
                Equipment.EquipmentType[] runeTypes = {
                    Equipment.EquipmentType.RUNE_FORCE,
                    Equipment.EquipmentType.RUNE_AGILITY,
                    Equipment.EquipmentType.RUNE_WISDOM,
                    Equipment.EquipmentType.RUNE_PV,
                    Equipment.EquipmentType.RUNE_XP,
                    Equipment.EquipmentType.RUNE_GOLD  // Added gold rune
                };
                Equipment.EquipmentType type = runeTypes[rand.nextInt(runeTypes.length)];
                items.add(EquipmentFactory.createRune(type, 5 + floorLevel));
            }
        }
        
        return new Level.Chest(chestId, items);
    }
    
    /**
     * Find a safe position that's not in walls, chests, or other mobs.
     * Returns null if no position found after max attempts.
     */
    private static Level.Position findSafePosition(Random rand, Set<Level.Position> walls,
                                                   Map<Level.Position, Level.Chest> chests,
                                                   Map<Level.Position, Mob> mobs,
                                                   int maxAttempts) {
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = rand.nextInt(Constants.BOARD_WIDTH - 2) + 1;
            int y = rand.nextInt(Constants.BOARD_HEIGHT - 2) + 1;
            Level.Position pos = new Level.Position(x, y);
            
            if (!walls.contains(pos) && !chests.containsKey(pos) && !mobs.containsKey(pos)) {
                return pos;
            }
        }
        return null;
    }
    
    /**
     * Fill all unreachable cells with walls using flood-fill algorithm.
     * Ensures that the exit and all chests are reachable from spawn.
     * If exit or chests are unreachable, attempts to create a path to them.
     * 
     * @param walls The set of walls to modify
     * @param chests The map of chests
     * @param spawn The spawn position
     * @param exit The exit position
     */
    private static void fillUnreachableCells(Set<Level.Position> walls, 
                                            Map<Level.Position, Level.Chest> chests,
                                            Level.Position spawn, 
                                            Level.Position exit) {
        // Find all reachable cells using flood fill
        Set<Level.Position> reachable = floodFill(walls, spawn);
        
        // Check if exit is reachable
        if (!reachable.contains(exit)) {
            System.out.println("Warning: Exit is unreachable on level. Creating path...");
            createPath(walls, reachable, exit);
            // Re-run flood fill to include newly accessible areas
            reachable = floodFill(walls, spawn);
        }
        
        // Check if all chests are reachable
        for (Level.Position chestPos : chests.keySet()) {
            if (!reachable.contains(chestPos)) {
                System.out.println("Warning: Chest at " + chestPos + " is unreachable. Creating path...");
                createPath(walls, reachable, chestPos);
                // Re-run flood fill to include newly accessible areas
                reachable = floodFill(walls, spawn);
            }
        }
        
        // Fill all unreachable cells with walls
        for (int x = 1; x < Constants.BOARD_WIDTH - 1; x++) {
            for (int y = 1; y < Constants.BOARD_HEIGHT - 1; y++) {
                Level.Position pos = new Level.Position(x, y);
                if (!reachable.contains(pos) && !walls.contains(pos)) {
                    walls.add(pos);
                }
            }
        }
    }
    
    /**
     * Perform flood fill from a starting position to find all reachable cells.
     * 
     * @param walls The set of walls
     * @param start The starting position
     * @return Set of all reachable positions
     */
    private static Set<Level.Position> floodFill(Set<Level.Position> walls, Level.Position start) {
        Set<Level.Position> visited = new HashSet<>();
        Queue<Level.Position> queue = new LinkedList<>();
        
        queue.add(start);
        visited.add(start);
        
        // Directions: up, down, left, right
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        
        while (!queue.isEmpty()) {
            Level.Position current = queue.poll();
            
            // Check all 4 directions
            for (int[] dir : directions) {
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];
                Level.Position neighbor = new Level.Position(newX, newY);
                
                // Check if position is valid and not visited
                if (isValidPosition(newX, newY) && 
                    !walls.contains(neighbor) && 
                    !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return visited;
    }
    
    /**
     * Create a path from reachable area to an unreachable target by removing walls.
     * Uses a simple approach: find the closest reachable cell to the target and 
     * create a straight path.
     * 
     * @param walls The set of walls to modify
     * @param reachable The set of currently reachable positions
     * @param target The target position to reach
     */
    private static void createPath(Set<Level.Position> walls, 
                                   Set<Level.Position> reachable, 
                                   Level.Position target) {
        // Find the closest reachable position to the target
        Level.Position closest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Level.Position pos : reachable) {
            int distance = Math.abs(pos.getX() - target.getX()) + 
                          Math.abs(pos.getY() - target.getY());
            if (distance < minDistance) {
                minDistance = distance;
                closest = pos;
            }
        }
        
        if (closest == null) {
            return; // No reachable positions found
        }
        
        // Create a path from closest to target
        int x = closest.getX();
        int y = closest.getY();
        
        // Move horizontally first
        while (x != target.getX()) {
            x += (target.getX() > x) ? 1 : -1;
            Level.Position pos = new Level.Position(x, y);
            walls.remove(pos); // Remove wall if present
        }
        
        // Then move vertically
        while (y != target.getY()) {
            y += (target.getY() > y) ? 1 : -1;
            Level.Position pos = new Level.Position(x, y);
            walls.remove(pos); // Remove wall if present
        }
    }
    
    /**
     * Check if a position is within the valid game board bounds.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if position is valid
     */
    private static boolean isValidPosition(int x, int y) {
        return x >= 0 && x < Constants.BOARD_WIDTH && 
               y >= 0 && y < Constants.BOARD_HEIGHT;
    }
}
