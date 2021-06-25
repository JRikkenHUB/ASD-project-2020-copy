package nl.ritogames.generator;

import nl.ritogames.generator.grid.GridBuilder;
import nl.ritogames.generator.grid.PathBuilder;
import nl.ritogames.generator.grid.RoomBuilder;
import nl.ritogames.generator.grid.TrapBuilder;
import nl.ritogames.generator.world.WorldBench;


public class GridGenerator {
    private GridBuilder gridBuilder;
    private RoomBuilder roomBuilder;
    private PathBuilder pathBuilder;
    private TrapBuilder trapBuilder;

    public GridGenerator() {
        this.gridBuilder = new GridBuilder();
        this.roomBuilder = new RoomBuilder();
        this.pathBuilder = new PathBuilder();
        this.trapBuilder = new TrapBuilder();
    }

    public WorldBench generateGrid(WorldBench bench, long seed) {
        bench = gridBuilder.addGrid(bench);
        bench = roomBuilder.addRooms(bench, seed, 1);
        bench = pathBuilder.addPaths(bench);
        bench = trapBuilder.addTraps(bench, seed, 1);
        return bench;
    }

    public GridBuilder getGridBuilder() {
        return gridBuilder;
    }

    public void setGridBuilder(GridBuilder gridBuilder) {
        this.gridBuilder = gridBuilder;
    }

    public RoomBuilder getRoomBuilder() {
        return roomBuilder;
    }

    public void setRoomBuilder(RoomBuilder roomBuilder) {
        this.roomBuilder = roomBuilder;
    }

    public PathBuilder getPathBuilder() {
        return pathBuilder;
    }

    public void setPathBuilder(PathBuilder pathBuilder) {
        this.pathBuilder = pathBuilder;
    }

    public TrapBuilder getTrapBuilder() {
        return trapBuilder;
    }

    public void setTrapBuilder(TrapBuilder trapBuilder) {
        this.trapBuilder = trapBuilder;
    }
}
