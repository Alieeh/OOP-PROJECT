package it.polito.extgol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class GameRepository extends GenericExtGOLRepository<Game, Long> {
    public GameRepository() {
        super(Game.class);
    }

    public void save(Game game) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (game.getId() == null) {
                em.persist(game);
            } else {
                em.merge(game);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()){
                tx.rollback(); //we undo any changes in case that there was an error
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public Game load(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Game game = em.find(Game.class, id);
            if (game == null){
                return null; //the game wasn't found
            }
            return game;
        } finally {
            em.close();
        }
    }
}