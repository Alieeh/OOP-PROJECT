package it.polito.extgol;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Prepare game and board
        ExtendedGameOfLife facade = new ExtendedGameOfLife();
        Game game = Game.createExtended("TestGame", 6, 6);
        Board board = game.getBoard();

        // --- R3 Famine and Bloom scenario ---
        Generation.createInitial(game, board, List.of(
            new Coord(1, 1),
            new Coord(2, 1),
            new Coord(1, 2),
            new Coord(2, 2)
        ));

        Board.setInteractableTile(game.getBoard(), new Coord(1,2), -1);
        board.getTile(new Coord(2, 2)).getCell().setLifePoints(3);
        board.getTile(new Coord(1, 1)).getCell().setLifePoints(3);
        board.getTile(new Coord(2, 1)).getCell().setLifePoints(3);

        // Apply FAMINE at gen 0, then evolve one generation, then BLOOM at gen 1 
        Game result = facade.run(game, 2, Map.of(0, EventType.FAMINE, 1, EventType.BLOOM));
        Generation afterFamine = result.getGenerations().get(1);
        Generation afterBloom = result.getGenerations().get(2);

        // After FAMINE, (1,2) is dead
        Map<Cell, Integer> lp1 = afterFamine.getEnergyStates();
        Cell cell12 = board.getTile(new Coord(1, 2)).getCell();
        System.out.println("After FAMINE: (1,2) life = " + lp1.get(cell12) + ", alive = " + afterFamine.getAliveCells().contains(cell12));

        // After BLOOM, (1,2) is born again, life is 0
        Map<Cell, Integer> lp2 = afterBloom.getEnergyStates();
        System.out.println("After BLOOM: (1,2) life = " + lp2.get(cell12) + ", alive = " + afterBloom.getAliveCells().contains(cell12));

        // --- R3 Event Don't Affect Dead scenario ---
        // Re-initialize game and board for the second scenario
        game = Game.createExtended("TestGame2", 6, 6);
        board = game.getBoard();

        Generation.createInitial(game, board, List.of(
            new Coord(1, 1),
            new Coord(2, 1),
            new Coord(1, 2),
            new Coord(2, 2)
        ));

        board.getTile(new Coord(2, 2)).getCell().setLifePoints(3);
        board.getTile(new Coord(1, 1)).getCell().setLifePoints(3);
        board.getTile(new Coord(2, 1)).getCell().setLifePoints(3);

        // Apply BLOOM at gen 0
        Game result2 = facade.run(game, 1, Map.of(0, EventType.BLOOM));
        Generation afterBloom2 = result2.getGenerations().get(1);

        Map<Cell, Integer> lp3 = afterBloom2.getEnergyStates();
        Cell cell12_2 = board.getTile(new Coord(1, 2)).getCell();
        Cell cell00 = board.getTile(new Coord(0, 0)).getCell();

        System.out.println("After BLOOM: (1,2) life = " + lp3.get(cell12_2));
        System.out.println("After BLOOM: (0,0) life = " + lp3.get(cell00) + ", alive = " + afterBloom2.getAliveCells().contains(cell00));
    }
}