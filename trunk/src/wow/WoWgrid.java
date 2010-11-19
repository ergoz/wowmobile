// World of Warcraft Mobile
//
// World maps constants and functions for: map, grid, cell

package wow;

public class WoWgrid {

    public static final int MAX_CELLS            = 8;
    public static final int MAX_GRIDS            = 64;
    public static final int GRID_CENTER_ID       = MAX_GRIDS / 2;
    public static final int CELL_CENTER_ID       = MAX_CELLS * GRID_CENTER_ID;
    public static final int ADT_MAX_CELLS        = 16;
    public static final int ADT_CELL_SIZE        = 8;
    public static final int ADT_GRID_SIZE        = ADT_MAX_CELLS * ADT_CELL_SIZE;
    public static final float GRID_SIZE          = 533.33333f;
    public static final float GRID_CENTER_OFFS   = GRID_SIZE / 2;
    public static final float CELL_SIZE          = GRID_SIZE / MAX_CELLS;
    public static final float CELL_CENTER_OFFS   = CELL_SIZE / 2;
    public static final float MAP_SIZE           = GRID_SIZE * MAX_GRIDS;
    public static final float MAP_HALFSIZE       = MAP_SIZE / 2;
    public static final float MAP_HALFSIZE_FLOOR = MAP_HALFSIZE - 0.5f;

    public static boolean isValidCoord(float coord) {
	return !(Float.isNaN(coord) || Float.isInfinite(coord));
    }

    public static boolean isValidMapCoord(float coord) {
	return isValidCoord(coord) && Math.abs(coord) <= MAP_HALFSIZE_FLOOR;
    }

    public static boolean isValidMapCoord(float x, float y) {
	return isValidMapCoord(x) && isValidMapCoord(y);
    }

    public static boolean isValidMapCoord(float x, float y, float z) {
	return isValidMapCoord(x,y) && isValidCoord(z);
    }

    public static boolean isValidMapCoord(float x, float y, float z, float o) {
	return isValidMapCoord(x,y,z) && isValidCoord(o);
    }

    public static float clampToValidMap(float coord) {
	if (!isValidCoord(coord))
	    return coord;
	if (coord > MAP_HALFSIZE_FLOOR)
	    coord = MAP_HALFSIZE_FLOOR;
	else if (coord < -MAP_HALFSIZE_FLOOR)
	    coord = -MAP_HALFSIZE_FLOOR;
	return coord;
    }

    public static int getGridId(float coord) {
	if (!isValidMapCoord(coord))
	    return -1;
	return (int)Math.floor(coord / -GRID_SIZE) + GRID_CENTER_ID;
    }

    public static float getGridBase(int id) {
	if (id < 0 || id >= MAX_GRIDS)
	    return 0f;
	return (GRID_CENTER_ID - id) * GRID_SIZE;
    }

    public static float getGridOffs(float coord) {
	int id = getGridId(coord);
	if (id < 0)
	    return 0.5f;
	return (getGridBase(id) - coord) / GRID_SIZE;
    }

    public static int getCellId(float coord) {
	if (!isValidMapCoord(coord))
	    return -1;
	return (int)Math.floor(coord / -CELL_SIZE) + CELL_CENTER_ID;
    }

}
