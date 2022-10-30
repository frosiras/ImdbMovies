package movies.dao;

import movies.models.ActorEntity;
import movies.models.MovieEntity;
import movies.utils.Connection;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieDao {
    private static Logger logger = Logger.getLogger("MovieLibrary");

    public void save(MovieEntity movie) throws Exception {
        Session session = Connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        ActorDao actorDao = new ActorDao();
        List<ActorEntity> newActors = new ArrayList<>();
        movie.getActors().forEach(actorEntity -> {
            try {
                if (actorDao.findAllFilmsByFullName(actorEntity).size() == 0)
                    newActors.add(actorEntity);
                else newActors.add(actorDao.findByFullName(actorEntity).get(0));
            } catch (Exception e) {
                logger.error(e);
            }
        });
        movie.setActors(newActors);
        session.save(movie);
        tx.commit();
        session.close();
    }

    public void delete(MovieEntity movie) throws Exception {
        List<ActorEntity> filmActors = getActors(movie.getImdbId());
        Session session = Connection.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        movie.setActors(filmActors
                .stream()
                .filter(actor -> actor.getMovies().size() == 1)
                .collect(Collectors.toList()));
        session.delete(movie);
        tx1.commit();
        session.close();
    }

    public MovieEntity findById(String id) throws Exception {
        Session session = Connection.getSessionFactory().openSession();
        MovieEntity movie = session.get(MovieEntity.class, id);
        session.close();
        return movie;
    }

    public List<MovieEntity> getAllMovies() throws Exception {
        List<MovieEntity> list;
        Session session = Connection.getSessionFactory().openSession();
        Query query = session.createQuery("from MovieEntity ");
        list = query.list();
        session.close();
        return list;
    }

    public List<ActorEntity> getActors(String id) throws Exception {
        Session session = Connection.getSessionFactory().openSession();
        Query query = session.createQuery("select actors FROM MovieEntity mov where mov.id = :id");
        query.setParameter("id", id);
        List<ActorEntity> list = query.getResultList();
        session.close();
        return list;
    }
}
