package it.polito.extgol;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

/**
 * Entity representing a single square on the Game of Life board.
 * Holds coordinate position, occupying Cell, and link back to its Board.
 */
@Entity
public class Tile implements Interactable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Coordinates of the tile on the board. */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "x",
            column = @Column(name = "tile_x", nullable = false)),
        @AttributeOverride(name = "y",
            column = @Column(name = "tile_y", nullable = false))
    })
    private Coord tileCoord;

    /** Reference to the board containing this tile. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    /** Reference to the owning game. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false, updatable = false)
    private Game game;

    /**
     * The cell occupying this tile.
     */
    @OneToOne(
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
    )
    @JoinColumn(name = "cell_id", nullable = false, unique = true)
    private Cell cell;

    /** Neighboring tiles for interactions. Not persisted. */
    @Transient
    private Set<Tile> neighbors = new HashSet<Tile>();


    @Column(name = "life_modifier", nullable = false)
    private int lifePointModifier = 0;

    /**
     * Default constructor required by JPA.
     */
    public Tile() {
    }
    
    /**
     * Constructs a tile at the given coordinates,
     * and the respective cell initialized as not alive
     * @param x column index (0-based)
     * @param y row index (0-based)
     */
    public Tile(int x, int y, Board b, Game g) {
        this.tileCoord = new Coord(x,y);
        this.board = b;
        this.game = g;
        this.cell = new Cell(this.tileCoord, this, b, g);
    }

    @Override
    public void interact(Cell cell) { //do both unrollevent and lifepointmodifier operations here
        if(cell.isAlive()){
            cell.setLifePoints(cell.getLifePoints() + this.getLifePointModifier());
            EventType event = this.getCurrentEvent();
            if (event == null) {
                return;
            }
            switch (event) { //we can use a switch case as the events are enumerated
                case CATACLYSM:
                    cell.setLifePoints(0);
                    break;
                case FAMINE:
                    cell.setLifePoints(cell.getLifePoints() - 1);
                    break;
                case BLOOM:
                    cell.setLifePoints(cell.getLifePoints() + 2);
                    break;
                case BLOOD_MOON:
                    if(cell.getMood() == CellMood.VAMPIRE){
                        for (Tile tile : cell.getNeighbors()){
                            Cell neighbor = tile.getCell();
                            if (neighbor != null && neighbor.getMood() == CellMood.HEALER) {
                                neighbor.setMood(CellMood.VAMPIRE); //convert the neighbor to a vampire
                            }
                        }
                    }
                    break;
                case SANCTUARY:
                    if(cell.getMood() == CellMood.HEALER){
                        cell.setLifePoints(cell.getLifePoints() + 1);
                    }
                    else if(cell.getMood() == CellMood.VAMPIRE){
                        cell.setMood(CellMood.NAIVE); //convert the vampire to a naive
                    }
                    break;
        }
            this.setCurrentEvent(null); //resetting so that it doesn't affect future gens 
        }
    }

    /**
     * Retrieves the Board to which this tile belongs.
     *
     * @return the parent Board instance
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Associates this tile with the given Board.
     *
     * @param board the Board on which this tile resides
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Retrieves the Cell currently occupying this tile.
     *
     * @return the Cell on this tile, or null if the tile is empty
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Places or removes a Cell on this tile.
     *
     * @param cell the Cell to assign to this tile, or null to clear it
     */
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    /**
     * Checks whether this tile currently hosts a live cell.
     *
     * @return true if a non-null, alive Cell occupies this tile; false otherwise
     */
    public boolean hasCell() {
        return cell != null && cell.isAlive();
    }

    /**
     * Initializes the neighbor relationships for this tile.
     *
     * @param neighborsList the Set of tiles adjacent to this one
     */
    public void initializeNeighbors(Set<Tile> neighborsList) {
        this.neighbors = neighborsList;
    }

    /**
     * Provides the set of neighboring tiles around this tile.
     *
     * @return a Set of adjacent Tile instances
     */
    public Set<Tile> getNeighbors() {
        return this.neighbors;
    }

    /**
     * Returns the database identifier for this Tile.
     *
     * @return the unique ID of this tile
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the coordinate object representing this tile's position.
     *
     * @return a Coord containing the tile’s x and y values
     */
    public Coord getCoordinates() {
        return this.tileCoord;
    }

    /**
     * Returns the X‐coordinate (column index) of this tile.
     *
     * @return the x value of this tile’s coordinate
     */
    public int getX() {
        return this.tileCoord.getX();
    }

    /**
     * Returns the Y‐coordinate (row index) of this tile.
     *
     * @return the y value of this tile’s coordinate
     */
    public int getY() {
        return this.tileCoord.getY();
    }

    // EXTENDED BEHAVIORS
    
    /**
     * Retrieves the life point modifier that this tile applies to any cell occupying it.
     * 
     * A positive value grants extra energy to the cell each generation, while
     * a negative value drains energy. A zero value has no effect.
     *
     * @return the integer modifier to a cell’s lifePoints when evolving
     */
    public Integer getLifePointModifier() {
        return lifePointModifier;
    }

    public void setLifePointModifier(int modifier) {
    this.lifePointModifier = modifier;
}
    @Transient
    private EventType currentEvent = null;

    public void setCurrentEvent(EventType event) {
        this.currentEvent = event;
    }
    public EventType getCurrentEvent() {
        return this.currentEvent;
    }
}