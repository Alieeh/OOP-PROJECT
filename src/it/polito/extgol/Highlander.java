package it.polito.extgol;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HIGHLANDER")
public class Highlander extends Cell {
    private int deathTurns = 0;

    public Highlander() {
        super();
        this.cellType = CellType.HIGHLANDER;
    }

    public Highlander(Coord coord, Tile tile, Board board, Game game) {
        super(coord, tile, board, game);
        this.cellType = CellType.HIGHLANDER;
    }

    @Override
    public Boolean evolve(int aliveNeighbors) {
        Boolean baseEvolve = super.evolve(aliveNeighbors);
        if (!baseEvolve && this.isAlive) {
            deathTurns++;
            if (deathTurns <= 2){
                this.lifepoints++;
                return true;
            }
            this.lifepoints--;
            return false;
            
        } else {
            deathTurns = 0;
            this.lifepoints++;
            return baseEvolve;
        }
    }
}
