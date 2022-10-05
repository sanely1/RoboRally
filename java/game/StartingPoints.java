package game;

import model.TypeName;

import java.util.HashMap;


public class StartingPoints {

    static HashMap<Integer, Position> startingPoints = new HashMap<>();
    private String map = "";


    public StartingPoints(String map) {
        if (map.equals(TypeName.DEATH_TRAP)) {
            fillFreeStartingPointsForDeathTrap();
        } else {
            fillFreeStartingPoints();
        }
    }

    public void fillFreeStartingPoints() {

        Position startPointPosition0 = new Position(1, 1);
        startingPoints.put(0, startPointPosition0);

        Position startPointPosition1 = new Position(0, 3);
        startingPoints.put(1, startPointPosition1);

        Position startPointPosition2 = new Position(1, 4);
        startingPoints.put(2, startPointPosition2);

        Position startPointPosition3 = new Position(1, 5);
        startingPoints.put(3, startPointPosition3);

        Position startPointPosition4 = new Position(0, 6);
        startingPoints.put(4, startPointPosition4);

        Position startPointPosition5 = new Position(1, 8);
        startingPoints.put(5, startPointPosition5);

    }

    public void fillFreeStartingPointsForDeathTrap() {

        Position startPointPosition0 = new Position(11, 1);
        startingPoints.put(0, startPointPosition0);

        Position startPointPosition1 = new Position(12, 3);
        startingPoints.put(1, startPointPosition1);

        Position startPointPosition2 = new Position(11, 4);
        startingPoints.put(2, startPointPosition2);

        Position startPointPosition3 = new Position(11, 5);
        startingPoints.put(3, startPointPosition3);

        Position startPointPosition4 = new Position(12, 6);
        startingPoints.put(4, startPointPosition4);

        Position startPointPosition5 = new Position(11, 8);
        startingPoints.put(5, startPointPosition5);

    }


    public HashMap<Integer, Position> getStartingPoints() {
        return startingPoints;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
