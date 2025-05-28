package it.polito.extgol;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Setup (mimics @Before in JUnit)
        ExtendedGameOfLife facade = new ExtendedGameOfLife();
        Game game = Game.createExtended("TestGame", 5, 4);
        Board board = game.getBoard();

        // --- Begin testR1R2R3BloodMoonEvent logic ---
        // Same stable block:
        Generation init = Generation.createInitial(game, board,
            List.of(new Coord(1,1), new Coord(1,2),
                    new Coord(2,1), new Coord(2,2),
                    new Coord(2,3))
        );
        // Turn a cell into a Vampire
        game.setMoods(CellMood.VAMPIRE, List.of(new Coord(1,1)));
        init.snapCells();

        init.getAliveCells().stream().forEach(e-> System.out.println(e.getMood() + " lifePoints:" + e.getLifePoints() + " coord:" + e.getX() + "," + e.getY() ) );
        System.out.println(init.getBoard().visualize(init));


        // BLOOD_MOON at step 1
        Game result = facade.run(game, 1, Map.of(0, EventType.BLOOD_MOON));

        Generation secondGeneration = result.getGenerations().get(1);
        System.out.println("\n Generation 2:");;
        secondGeneration.getAliveCells().stream().forEach(e-> System.out.println(e.getMood() + " lifePoints:" + e.getLifePoints()+ " coord:" + e.getX() + "," + e.getY() ) );
        System.out.println(secondGeneration.getBoard().visualize(secondGeneration));
        


        Map<Cell,Integer> lp1  = secondGeneration.getEnergyStates();
        Cell vamp1 = board.getTile(new Coord(1,1)).getCell();

        int energy = lp1.get(vamp1);
        System.out.println("Energy at (1,1) [should be 4]: " + energy);

        // Its neighbors should have turned Vampire
        Cell naive1 = board.getTile(new Coord(1,2)).getCell();
        System.out.println("Mood at (1,2) [should be VAMPIRE]: " + naive1.getMood());

        // Far‐away (2,3) remains Naive with 0 LP
        Cell naive2 = board.getTile(new Coord(2,3)).getCell();
        System.out.println("Mood at (2,3) [should be NAIVE]: " + naive2.getMood());
    }
}