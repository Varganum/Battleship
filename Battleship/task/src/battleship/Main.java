package battleship;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static final int SHIPS_QUANTITY = 5; //quantity of ships
    static final String[] INIT_MESSAGES  = {"\nEnter the coordinates of the Aircraft Carrier (5 cells):\n",
                                           "\nEnter the coordinates of the Battleship (4 cells):\n",
                                           "\nEnter the coordinates of the Submarine (3 cells):\n",
                                           "\nEnter the coordinates of the Cruiser (3 cells):\n",
                                           "\nEnter the coordinates of the Destroyer (2 cells):\n",
                                           "\nPress Enter and pass the move to another player\n",
                                           ", place your ships on the game field\n"};
    static final String[] GAME_MESSAGES = {"\nThe game starts!\n",
                                           ", it's your turn:\n",
                                           "Error! You entered the wrong coordinates! Try again:\n",
                                           "You missed!\n",
                                           "You hit a ship!\n",
                                           "You sank a ship!\n",
                                           "You sank the last ship. You won. Congratulations!\n"};
    static final String[] INIT_ERRORS = {"Error! Wrong length of the Aircraft Carrier (5 cells)! Try again:\n",
                                                "Error! Wrong length of the Battleship (4 cells)! Try again:\n",
                                                "Error! Wrong length of the Submarine (3 cells)! Try again:\n",
                                                "Error! Wrong length of the Cruiser (3 cells)! Try again:\n",
                                                "Error! Wrong length of the Destroyer (2 cells)! Try again:\n",
                                                "Error! Wrong ship location! Try again:\n",
                                                "Error! You placed it too close to another one. Try again:\n",
                                                "Error! Wrong coordinates. Try again:\n"};
    public static void main(String[] args) {

        String shotLiteralCoordinate; //literal format coordinate of a game shot
        Coordinates2D shotCoordinate; //coordinates of shot in coordinates2D type

        Player player1 = new Player("Player 1", true);
        int numberOfShipsAlivePlayer1 = player1.getNumberOfShipsAlive();
        Player player2 = new Player("Player 2", false);
        int numberOfShipsAlivePlayer2 = player2.getNumberOfShipsAlive();

        //start dialog: player1 is to place ships on the battlefield
        System.out.println(player1.getName() + INIT_MESSAGES[6]);
        player1.initBattleField();
        player1.printField(Player.withoutFogOfWar);
        player1.placeShipsOnBattleField();
        System.out.println(INIT_MESSAGES[5]);
        (new Scanner(System.in)).nextLine(); //waiting for Enter pressed

        //continue dialog: player2 is to place ships on the battlefield
        System.out.println(player2.getName() + INIT_MESSAGES[6]);
        player2.initBattleField();
        player2.printField(Player.withoutFogOfWar);
        player2.placeShipsOnBattleField();
        System.out.println(INIT_MESSAGES[5]);
        (new Scanner(System.in)).nextLine(); //waiting for Enter pressed


        //starting game
        System.out.println(GAME_MESSAGES[0]);
        Scanner scanner = new Scanner(System.in);

        //main game loop
        while (numberOfShipsAlivePlayer1 > 0 && numberOfShipsAlivePlayer2 > 0) {

            if (player1.getTurn()) {
                player2.printField(Player.withFogOfWar);
                System.out.println("---------------------");
                player1.printField(Player.withoutFogOfWar);
                System.out.println("\n" + player1.getName() + GAME_MESSAGES[1]);

                shotLiteralCoordinate = acceptShot(scanner);
                shotCoordinate = Coordinates2D.getCoordinates2D(shotLiteralCoordinate);
                player2.changeNumberOfShipsAlive(player2.checkShot(shotCoordinate, numberOfShipsAlivePlayer2));
                numberOfShipsAlivePlayer2 = player2.getNumberOfShipsAlive();
                if (numberOfShipsAlivePlayer2 > 0) {
                    player1.setTurn(false);
                    player2.setTurn(true);
                    System.out.println(INIT_MESSAGES[5]);
                    (new Scanner(System.in)).nextLine(); //waiting for Enter pressed
                }

            } else {
                player1.printField(Player.withFogOfWar);
                System.out.println("---------------------");
                player2.printField(Player.withoutFogOfWar);
                System.out.println("\n" + player2.getName() + GAME_MESSAGES[1]);

                shotLiteralCoordinate = acceptShot(scanner);
                shotCoordinate = Coordinates2D.getCoordinates2D(shotLiteralCoordinate);
                player1.changeNumberOfShipsAlive(player1.checkShot(shotCoordinate, numberOfShipsAlivePlayer1));
                numberOfShipsAlivePlayer1 = player1.getNumberOfShipsAlive();
                if (numberOfShipsAlivePlayer1 > 0) {
                    player2.setTurn(false);
                    player1.setTurn(true);
                    System.out.println(INIT_MESSAGES[5]);
                    (new Scanner(System.in)).nextLine(); //waiting for Enter pressed
                }
            }

        }

    }

    /*
    Method accepts user input and check the correctness of literal format coordinate.
     */
    private static String acceptShot(Scanner scanner) {
        String shotLiteralCoordinate;
        do {
            shotLiteralCoordinate = scanner.next();
            if (Coordinates2D.checkCoordinate(shotLiteralCoordinate)) {
                System.out.println(GAME_MESSAGES[2]);
            }
        } while (Coordinates2D.checkCoordinate(shotLiteralCoordinate));
        return shotLiteralCoordinate;
    }

}

class Coordinates2D {
    private final int raw;
    private final int column;

    //constructor 1
    Coordinates2D(int raw, int column) {
        this.raw = raw;
        this.column = column;
    }

    //constructor 2
    Coordinates2D(String literalCoordinate2D) {
        this.raw = literalCoordinate2D.charAt(0) - 'A';
        if (literalCoordinate2D.length() == 3) {
            this.column = 9;
        } else {
            this.column = literalCoordinate2D.charAt(1) - '1';
        }
    }

    public int getRaw() {
        return this.raw;
    }

    public int getColumn() {
        return this.column;
    }

    public static Coordinates2D getCoordinates2D(String literalCoordinates2D) {
        return new Coordinates2D(literalCoordinates2D);
    }

    public boolean equals(Coordinates2D point2) {
        return this.raw == point2.getRaw() && this.column == point2.getColumn();
    }

    /*
    Method checks if passed coordinate2D is inside the battlefield
     */
    static boolean isInsideField(Coordinates2D point) {
        return point.getRaw() >= 0 && point.getRaw() <= 9 && point.getColumn() >= 0 && point.getColumn() <= 9;
    }

    /*
    Method checks the correctness of literal format coordinate and returns.
    true - if format is correct
    false - incorrect format
     */
    static boolean checkCoordinate(String literalCoordinate) {
        boolean isCorrectChar = false;
        boolean isCorrectNumber = true;
        char raw = literalCoordinate.charAt(0);
        if (raw >= 'A' && raw <= 'J') {
            isCorrectChar = true;
        }

        if (literalCoordinate.length() > 3 || literalCoordinate.length() < 2) {
            isCorrectNumber = false;
        } else if (literalCoordinate.length() == 3 && literalCoordinate.charAt(1) != '1') {
            isCorrectNumber = false;
        } else if (literalCoordinate.length() == 3 && literalCoordinate.charAt(1) == '1' && literalCoordinate.charAt(2) != '0') {
            isCorrectNumber = false;
        } else if (literalCoordinate.length() == 2 &&
                (literalCoordinate.charAt(1) - '1' < 0 || literalCoordinate.charAt(1) - '1' > 9)) {
            isCorrectNumber = false;
        }

        return !isCorrectChar || !isCorrectNumber;

    }

    /*
    Method returns numeric 2D coordinates in intervals 0 - 9
    of the ship's ends entered in string format by user
        coordinates2D[0][0] - first point's raw
        coordinates2D[0][1] - first point's column
        coordinates2D[1][0] - second point's raw
        coordinates2D[1][1] - second point's column
    */
    static int[][] get2DCoordinates(String firstCoord, String secondCoord) {

        int[][] coordinates2D = new int[2][2];

        coordinates2D[0][0] = firstCoord.charAt(0) - 'A';
        coordinates2D[1][0] = secondCoord.charAt(0) - 'A';

        if (firstCoord.length() == 3) {
            coordinates2D[0][1] = 9;
        } else {
            coordinates2D[0][1] = firstCoord.charAt(1) - '1';
        }

        if (secondCoord.length() == 3) {
            coordinates2D[1][1] = 9;
        } else {
            coordinates2D[1][1] = secondCoord.charAt(1) - '1';
        }
        return coordinates2D;
    }

}

class Ship {

    static final int[] SHIP_LENGTH = {5, 4, 3, 3, 2};

    private final ArrayList<Coordinates2D> shipCoordinates2D;

    //constructor
    Ship(int[][] coordinatesOfEnds, int isHorizontal) {

        this.shipCoordinates2D = new ArrayList<>();

        int startPoint;
        int endPoint;
        int constantDimension;

        if (isHorizontal == 1) {
            startPoint = Math.min(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            endPoint = Math.max(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            constantDimension = coordinatesOfEnds[1][0];
            for (int j = startPoint; j <= endPoint; j++) {
                this.shipCoordinates2D.add(new Coordinates2D(constantDimension, j));
            }
        } else {
            startPoint = Math.min(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            endPoint = Math.max(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            constantDimension = coordinatesOfEnds[0][1];
            for (int j = startPoint; j <= endPoint; j++) {
                this.shipCoordinates2D.add(new Coordinates2D(j, constantDimension));
            }
        }
    }

    public boolean containCell(Coordinates2D cell) {
        boolean result = false;
        for (Coordinates2D cellToCheck : this.shipCoordinates2D) {
            if (cell.equals(cellToCheck)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void excludeCell(Coordinates2D cell) {
        int index = 0;
        for (Coordinates2D cellToExclude : this.shipCoordinates2D) {
            if (cell.equals(cellToExclude)) {
                break;
            }
            index++;
        }
        //Coordinates2D excludedCell =
        this.shipCoordinates2D.remove(index);
    }

    public boolean isSunk() {
        return this.shipCoordinates2D.isEmpty();
    }

    /*
       Method checks positioning correctness of coordinates entered by user
       and returns:
       0 - if positioning is not correct;
       1 - ship is horizontal
       2 - vertical
     */
    static byte checkLocation(int[][] coordinatesOfEnds) {
        byte result = 0;
        if (coordinatesOfEnds[0][0] == coordinatesOfEnds[1][0]) {
            result = 1;
        } else if (coordinatesOfEnds[0][1] == coordinatesOfEnds[1][1]) {
            result = 2;
        }
        return result;
    }

    /*
    Method checks the correctness of ship length when user tries to place it
    on the battlefield.
     */
    static boolean checkShipLength(int i, int[][] coordinatesOfEnds, byte isHorizontal) {
        boolean result = false;
        int lengthReal;
        if (isHorizontal == 1) {
            lengthReal = Math.abs(coordinatesOfEnds[0][1] - coordinatesOfEnds[1][1]) + 1;
        } else {
            lengthReal = Math.abs(coordinatesOfEnds[0][0] - coordinatesOfEnds[1][0]) + 1;
        }

        if (lengthReal == SHIP_LENGTH[i]) {
            result = true;
        }
        return result;
    }

}

class Player {
    private final String name;
    private final char[][] battleField = new char[10][10]; //game field 10x10

    //ArrayList with numeric coordinates of ship's cells
    private final ArrayList<Coordinates2D> shipsCoordinates = new ArrayList<>();

     //ArrayList with numeric coordinates of neighbor cells of ships
    private final ArrayList<Coordinates2D> shipsNeighborhood = new ArrayList<>();

    private final Ship[] ship = new Ship[5];

    private final boolean[] isShipSet = new boolean[5]; //flags of ships placement success

    private static final char[] rawLetters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    static final boolean withoutFogOfWar = false;
    static final boolean withFogOfWar = true;
    private int numberOfShipsAlive;
    private boolean isMyTurn;

    byte[] isHorizontal = new byte[5]; //1 - ship is horizontal, 2 - vertical, 0 - wrong location

    //constructor
    public Player(String name, boolean isMyTurn) {
        this.name = name;
        this.numberOfShipsAlive = Main.SHIPS_QUANTITY;
        this.isMyTurn = isMyTurn;
    }

    String getName() {
        return this.name;
    }

    boolean getTurn() {
        return this.isMyTurn;
    }

    void setTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    /*
    Method fills battleField and enemyBattleField arrays with '~' chars.
    */
    void initBattleField() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                battleField[i][j] = '~';
            }
        }
    }

    /*
    Method outputs current state of battleField array.
    It also prints horizontal line with numeric coordinates
    and first left column with literal coordinates
    */
    void printField() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print(rawLetters[i]);
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + battleField[i][j]);
            }
            System.out.println();
        }
    }

    /*
    Method outputs current state of battleField array with Fog of war or without it.
    It also prints horizontal line with numeric coordinates
    and first left column with literal coordinates.
    */
    void printField(boolean isFog) {
        char valueToPrint;
        if (isFog) {
            System.out.println("  1 2 3 4 5 6 7 8 9 10");
            for (int i = 0; i < 10; i++) {
                System.out.print(rawLetters[i]);
                for (int j = 0; j < 10; j++) {

                    valueToPrint = battleField[i][j] == 'O' ? '~' : battleField[i][j];
                    System.out.print(" " + valueToPrint);
                }
                System.out.println();
            }
        } else {
            printField();
        }
    }

    int getNumberOfShipsAlive() {
        return numberOfShipsAlive;
    }

    void changeNumberOfShipsAlive(int change) {
        this.numberOfShipsAlive += change;
    }

    /*
    Method checks shots of user and display new state of battlefield with Fog of war
    with appropriate game message. Returns
    0 - if quantity of alive ships didn't change after current shot
    -1 - if one of ships was sunk after current shot
    */
    int checkShot(Coordinates2D shot, int numberOfShipsAlive) {
        int result = 0;
        boolean isHit = false; //false - miss, true - hit
        boolean isShipSunk;

        for (Coordinates2D shipsCoordinate : shipsCoordinates) {
            if (shot.equals(shipsCoordinate)) {
                isHit = true;
                break;
            }
        }

        if (isHit) {
            if (battleField[shot.getRaw()][shot.getColumn()] == 'O') {
                battleField[shot.getRaw()][shot.getColumn()] = 'X';
                isShipSunk = reduceHittedShip(shot);

                if (isShipSunk && numberOfShipsAlive > 1) {
                    System.out.println(Main.GAME_MESSAGES[5]);
                    result = -1;
                } else if (isShipSunk && numberOfShipsAlive == 1) {
                    System.out.println(Main.GAME_MESSAGES[6]);
                    result = -1;
                }

                if (!isShipSunk) {
                    System.out.println(Main.GAME_MESSAGES[4]);
                }

            } else {
                //case when user made a shot in already hitted cell
                System.out.println(Main.GAME_MESSAGES[4]);
            }
        } else {
            battleField[shot.getRaw()][shot.getColumn()] = 'M';
            System.out.println(Main.GAME_MESSAGES[3]);
        }
        return result;
    }

    /*
    Method update values of battleField array placing a ships on the field after
    all checks and print new state of the battleField array.
    Ship is marked by chars 'O'.
    */
    void placeShipOnField(int i, int[][] coordinatesOfEnds, byte isHorizontal) {
        int firstRaw;
        int firstColumn;
        if (isHorizontal == 1) {
            firstColumn = Math.min(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            firstRaw = coordinatesOfEnds[0][0];
            for (int j = 0; j < Ship.SHIP_LENGTH[i]; j++) {
                battleField[firstRaw][firstColumn + j] = 'O';
            }
        } else {
            firstColumn = coordinatesOfEnds[0][1];
            firstRaw = Math.min(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            for (int j = 0; j < Ship.SHIP_LENGTH[i]; j++) {
                battleField[firstRaw + j][firstColumn] = 'O';
            }
        }
        printField();
    }

    /*
    Method fills ArrayList shipsCoordinates with points of ships.
    */
    void fillShipsCoordinates(int[][] coordinatesOfEnds, byte isHorizontal) {
        int startPoint;
        int endPoint;
        int constantDimension;

        if (isHorizontal == 1) {
            startPoint = Math.min(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            endPoint = Math.max(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            constantDimension = coordinatesOfEnds[1][0];
            for (int j = startPoint; j <= endPoint; j++) {
                shipsCoordinates.add(new Coordinates2D(constantDimension, j));
            }
        } else {
            startPoint = Math.min(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            endPoint = Math.max(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            constantDimension = coordinatesOfEnds[0][1];
            for (int j = startPoint; j <= endPoint; j++) {
                shipsCoordinates.add(new Coordinates2D(j, constantDimension));
            }
        }

        fillShipsNeighborhood(coordinatesOfEnds, isHorizontal);
    }

    /*
    Method fills ArrayList shipsNeighborhood with points of ships and cells near ships.
    */
    void fillShipsNeighborhood(int[][] coordinatesOfEnds, byte isHorizontal) {

        int startPoint;
        int endPoint;
        int constantDimension;

        if (isHorizontal == 1) {
            startPoint = Math.min(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            endPoint = Math.max(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            constantDimension = coordinatesOfEnds[1][0];

            for (int level = -1; level < 2; level++) {
                for (int j = startPoint - 1; j <= endPoint + 1; j++) {
                    Coordinates2D currentPoint = new Coordinates2D(constantDimension + level, j);
                    if (Coordinates2D.isInsideField(currentPoint)) {
                        shipsNeighborhood.add(currentPoint);
                    }
                }
            }
        } else {
            startPoint = Math.min(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            endPoint = Math.max(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            constantDimension = coordinatesOfEnds[0][1];
            for (int level = -1; level < 2; level++) {
                for (int j = startPoint - 1; j <= endPoint + 1; j++) {
                    Coordinates2D currentPoint = new Coordinates2D(j, constantDimension + level);
                    if (Coordinates2D.isInsideField(currentPoint)) {
                        shipsNeighborhood.add(currentPoint);
                    }
                }
            }
        }

    }

    /*
    Method checks if the ship crosses or is near one of other ships and returns
    true - if ship does not cross and touches other ships
    false - if ship cross or touches other ships.
    */
    boolean noCross(int[][] coordinatesOfEnds, byte isHorizontal) {
        boolean result = true;
        int startPoint;
        int endPoint;
        int constantDimension;
        Coordinates2D pointForCheck;

        if (isHorizontal == 1) {
            startPoint = Math.min(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            endPoint = Math.max(coordinatesOfEnds[0][1], coordinatesOfEnds[1][1]);
            constantDimension = coordinatesOfEnds[1][0];
            for (int j = startPoint; j <= endPoint; j++) {
                pointForCheck = new Coordinates2D(constantDimension, j);
                for (Coordinates2D neighbor : shipsNeighborhood) {
                    if (neighbor.equals(pointForCheck)) {
                        result = false;
                        //System.out.println("The cross or touch at point: " + constantDimension + " " + j);
                        break;
                    }
                }
            }
        } else {
            startPoint = Math.min(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            endPoint = Math.max(coordinatesOfEnds[0][0], coordinatesOfEnds[1][0]);
            constantDimension = coordinatesOfEnds[0][1];
            for (int j = startPoint; j <= endPoint; j++) {
                pointForCheck = new Coordinates2D(j, constantDimension);
                for (Coordinates2D neighbor : shipsNeighborhood) {
                    if (neighbor.equals(pointForCheck)) {
                        result = false;
                        //System.out.println("The cross or touch at point: " + constantDimension + " " + j);
                        break;
                    }
                }
            }
        }

        return result;
    }

    /*
    Method reduces the quantity of ship cells in appropriate Ship object and checks
    if there are any cells left in the hitted ship. Returns
    false - if at least 1 cell has left
    true - if there is no unhitted cells anymore.
    */
    boolean reduceHittedShip(Coordinates2D hittedCell) {
        boolean result = false;
        for (Ship checkedShip : ship) {
            if (checkedShip.containCell(hittedCell)) {
                checkedShip.excludeCell(hittedCell);
                result = checkedShip.isSunk();
                break;
            }
        }
        return result;
    }

    /*
    Method add new ship to ship array of Player object.
     */
    void addShip(int i, int[][] coordinatesOfEnds, int isHorizontal) {
        this.ship[i] = new Ship(coordinatesOfEnds, isHorizontal);
    }

    void placeShipsOnBattleField() {
        String firstCoordinate;  //literal format coordinate of ship's first end
        String secondCoordinate; //literal format coordinate of ship's second end
        int[][] coordinatesOfEnds; //numeric coordinates of ship's ends [2][2]

        Scanner scanner = new Scanner(System.in);

        //loop for ships positioning
        for (int i = 0; i < Main.SHIPS_QUANTITY; i++) {

            System.out.println(Main.INIT_MESSAGES[i]);

            while (!isShipSet[i]) {

                firstCoordinate = scanner.next();
                secondCoordinate = scanner.next();

                //check the correctness of coordinates format entered by user
                if (Coordinates2D.checkCoordinate(firstCoordinate) ||
                        Coordinates2D.checkCoordinate(secondCoordinate)) {
                    System.out.println(Main.INIT_ERRORS[7]);
                    continue;
                }

                //get numeric coordinates of ship's ends
                coordinatesOfEnds = Coordinates2D.get2DCoordinates(firstCoordinate, secondCoordinate);

                //check the correctness of ship position and determine if it is horizontal or vertical
                isHorizontal[i] = Ship.checkLocation(coordinatesOfEnds);
                if (isHorizontal[i] == 0) {
                    System.out.println(Main.INIT_ERRORS[5]);
                    continue;
                }

                //check the correctness of ship length
                if (!Ship.checkShipLength(i, coordinatesOfEnds, isHorizontal[i])) {
                    System.out.println(Main.INIT_ERRORS[i]);
                    continue;
                }

                //check if the ship crosses or touches other ships
                if (i > 0 && !this.noCross(coordinatesOfEnds, isHorizontal[i])) {
                    System.out.println(Main.INIT_ERRORS[6]);
                    continue;
                }

                isShipSet[i] = true;
                this.addShip(i, coordinatesOfEnds, isHorizontal[i]);
                //it is possible to optimize method fillShipsCoordinates by using Ship object
                this.fillShipsCoordinates(coordinatesOfEnds, isHorizontal[i]);
                this.placeShipOnField(i, coordinatesOfEnds, isHorizontal[i]);
            }
        }
    }

}