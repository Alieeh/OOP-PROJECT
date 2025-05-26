package it.polito.extgol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Repository for Board entity providing persistence operations.
 * Extends the generic repository with Board-specific loading and querying capabilities.
 */
public class BoardRepository extends GenericExtGOLRepository<Board, Long> {
    
    /**
     * Constructs a new BoardRepository.
     * Initializes the superclass with Board.class for type-safe operations.
     */
    public BoardRepository() {
        super(Board.class);
    }
    
    /**
     * Loads a Board entity with its complete state, including all Tiles and Cells.
     * 
     * @param id the unique identifier of the Board to load
     * @param em the EntityManager to use for database operations
     * @return the fully loaded Board with all its components, or null if not found
     */
    public Board load(Long id, EntityManager em) {
        // First load the board entity
        Board board = em.find(Board.class, id);
        if (board == null) {
            return null;
        }
        
        // Load all tiles associated with this board
        TypedQuery<Tile> tileQuery = em.createQuery(
            "SELECT t FROM Tile t WHERE t.board.id = :boardId", 
            Tile.class
        );
        tileQuery.setParameter("boardId", id);
        List<Tile> tiles = tileQuery.getResultList();
        
        // Load all cells associated with this board
        TypedQuery<Cell> cellQuery = em.createQuery(
            "SELECT c FROM Cell c WHERE c.board.id = :boardId", 
            Cell.class
        );
        cellQuery.setParameter("boardId", id);
        List<Cell> cells = cellQuery.getResultList();
        
        // Force initialization of lazy collections
        tiles.size();
        cells.size();
        
        return board;
    }
    
    /**
     * Finds boards by width and height dimensions.
     * 
     * @param width the board width to search for
     * @param height the board height to search for
     * @param em the EntityManager to use for database operations
     * @return a list of boards matching the specified dimensions
     */
    public List<Board> findByDimensions(int width, int height, EntityManager em) {
        TypedQuery<Board> query = em.createQuery(
            "SELECT b FROM Board b WHERE b.width = :width AND b.height = :height",
            Board.class
        );
        query.setParameter("width", width);
        query.setParameter("height", height);
        return query.getResultList();
    }
    
    /**
     * Counts the total number of tiles on a board.
     * 
     * @param boardId the ID of the board to count tiles for
     * @param em the EntityManager to use for database operations
     * @return the count of tiles on the specified board
     */
    public Long countTiles(Long boardId, EntityManager em) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(t) FROM Tile t WHERE t.board.id = :boardId",
            Long.class
        );
        query.setParameter("boardId", boardId);
        return query.getSingleResult();
    }
    
    /**
     * Counts the number of alive cells on a board.
     * 
     * @param boardId the ID of the board to count alive cells for
     * @param em the EntityManager to use for database operations
     * @return the count of alive cells on the specified board
     */
    public Long countAliveCells(Long boardId, EntityManager em) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Cell c WHERE c.board.id = :boardId AND c.isAlive = true",
            Long.class
        );
        query.setParameter("boardId", boardId);
        return query.getSingleResult();
    }
}