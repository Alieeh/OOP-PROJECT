package it.polito.extgol;
import java.util.List;

public class CellRepository extends GenericExtGOLRepository<Cell, Long> {

    public CellRepository() {
        super(Cell.class);
    }

    /**
     * Loads all cells in the database.
     * 
     * @return list of all Cell entities
     */
    public List<Cell> loadAll() {
        return findAll();
    }

    /**
     * Loads all alive cells.
     * 
     * @return list of alive cells
     */
    public List<Cell> loadAliveCells() {
        return JPAUtil.getEntityManager().createQuery(
                "SELECT c FROM Cell c WHERE c.isAlive = true", Cell.class)
                .getResultList();
    }

    /**
     * Loads all cells belonging to a specific board.
     * 
     * @param boardId ID of the board
     * @return list of cells on that board
     */
    // public List<Cell> loadByBoardId(Long boardId) {
    //     return JPAUtil.getEntityManager().createQuery(
    //             "SELECT c FROM Cell c WHERE c.board.id = :boardId", Cell.class)
    //             .setParameter("boardId", boardId)
    //             .getResultList();
    // }
}
