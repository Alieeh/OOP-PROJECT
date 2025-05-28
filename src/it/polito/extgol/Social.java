package it.polito.extgol;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SOCIAL")
public class Social extends Cell {
    public Social() {
        super();
        this.cellType = CellType.SOCIAL;
    }

    public Social(Coord coord, Tile tile, Board board, Game game) {
        super(coord, tile, board, game);
        this.cellType = CellType.SOCIAL;
    }


    @Override
    public Boolean evolve(int aliveNeighbors) {
        Boolean willLive = this.isAlive;
        if (aliveNeighbors > 8) {
            willLive = false;
        }
        else if (aliveNeighbors < 2) {
            willLive = false;
        }

        return willLive;
    }
}
