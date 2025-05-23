package it.polito.extgol;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LONER")
public class Loner extends Cell{
    public Loner() {
        super();
        this.cellType = CellType.LONER;
    }

    public Loner(Coord coord, Tile tile, Board board, Game game) {
        super(coord, tile, board, game);
        this.cellType = CellType.LONER;
    }
}
