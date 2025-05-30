package it.polito.extgol;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Setup (mimics @Before in JUnit)
        // ExtendedGameOfLife facade = new ExtendedGameOfLife();
        // Game game = Game.createExtended("TestGame", 5, 4);
        // Board board = game.getBoard();

        // // --- Begin testR1R2R3BloodMoonEvent logic ---
        // // Same stable block:
        // Generation init = Generation.createInitial(game, board,
        //     List.of(new Coord(1,1), new Coord(1,2),
        //             new Coord(2,1), new Coord(2,2),
        //             new Coord(2,3))
        // );
        // // Turn a cell into a Vampire
        // game.setMoods(CellMood.VAMPIRE, List.of(new Coord(1,1)));
        // init.snapCells();

        // init.getAliveCells().stream().forEach(e-> System.out.println(e.getMood() + " lifePoints:" + e.getLifePoints() + " coord:" + e.getX() + "," + e.getY() ) );
        // System.out.println(init.getBoard().visualize(init));


        // // BLOOD_MOON at step 1
        // Game result = facade.run(game, 1, Map.of(0, EventType.BLOOD_MOON));

        // Generation secondGeneration = result.getGenerations().get(1);
        // System.out.println("\n Generation 2:");;
        // secondGeneration.getAliveCells().stream().forEach(e-> System.out.println(e.getMood() + " lifePoints:" + e.getLifePoints()+ " coord:" + e.getX() + "," + e.getY() ) );
        // System.out.println(secondGeneration.getBoard().visualize(secondGeneration));
        


        // Map<Cell,Integer> lp1  = secondGeneration.getEnergyStates();
        // Cell vamp1 = board.getTile(new Coord(1,1)).getCell();

        // int energy = lp1.get(vamp1);
        // System.out.println("Energy at (1,1) [should be 4]: " + energy);

        // // Its neighbors should have turned Vampire
        // Cell naive1 = board.getTile(new Coord(1,2)).getCell();
        // System.out.println("Mood at (1,2) [should be VAMPIRE]: " + naive1.getMood());

        // // Far‐away (2,3) remains Naive with 0 LP
        // Cell naive2 = board.getTile(new Coord(2,3)).getCell();
        // System.out.println("Mood at (2,3) [should be NAIVE]: " + naive2.getMood());


        // Setup 
        ExtendedGameOfLife facade = new ExtendedGameOfLife();
        Game game = Game.createExtended("TestGame", 5, 4);
        Board board = game.getBoard();

        // --- Begin test ---
        Generation init = Generation.createInitial(game, board,
            List.of(new Coord(1,1), new Coord(1,2), new Coord(2,1), new Coord(2,2))
        );

        Game result = facade.run(game, 5, Map.of());
        Generation Generation1 = result.getGenerations().get(0);
        Cell c1 = facade.getAliveCells(Generation1).get(new Coord(1,1));
        int lp1 = Generation1.getEnergyStates().get(c1);


        // --- Second generation: after BLOOM ---
        Generation Generation2 = result.getGenerations().get(1);
        Cell c2 = facade.getAliveCells(Generation2).get(new Coord(1,1));
        int lp2 = Generation2.getEnergyStates().get(c2);

        System.out.println("Second generation LP at (1,1) [should be 1]: " + lp2);
        // But here it is 5 which is the final LP after all ding all generations!

        // --- Fifth generation: after CATACLYSM ---
        Generation fifthGeneration = result.getGenerations().get(4);
        Cell c5 = facade.getAliveCells(fifthGeneration).get(new Coord(1,1));
        int lp5 = fifthGeneration.getEnergyStates().get(c5);
        System.out.println("Fifth generation LP at (1,1) [should be 5: " + lp5);
        // here it is 5, which is correct!

        // Optional: simple pass/fail checks
        if (lp1 == 3) {
            System.out.println("PASS: BLOOM event applied correctly.");
        } else {
            System.out.println("FAIL: BLOOM event not applied as expected.");
        }
        if (lp5 == 1) {
            System.out.println("PASS: CATACLYSM event applied correctly.");
        } else {
            System.out.println("FAIL: CATACLYSM event not applied as expected.");
        }


    }
}