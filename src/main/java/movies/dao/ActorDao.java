package movies.dao;

import movies.models.ActorEntity;
import movies.models.MovieEntity;
import movies.utils.Connection;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActorDao {
    private static Logger logger = Logger.getLogger("MovieLibrary");

    public List<ActorEntity> findByFullName(ActorEntity actor) throws Exception {
        Session session = Connection.getSessionFactory().openSession();
        Query query = session.createQuery("from ActorEntity as act where act.firstName = :firstName and act.lastName = :lastName");
        query.setParameter("firstName", actor.getFirstName());
        query.setParameter("lastName", actor.getLastName());
        List<ActorEntity> list = query.list();
        session.close();
        return list;
    }

    public List<MovieEntity> findAllFilmsByFullName(ActorEntity actor) {
        try {
            Session session = Connection.getSessionFactory().openSession();
            Query query = session.createQuery("select  mv from MovieEntity as mv inner join mv.actors as actr with actr.firstName = :fname and actr.lastName = :lname ");
            query.setParameter("fname", actor.getFirstName());
            query.setParameter("lname", actor.getLastName());
            List<MovieEntity> movies = query.list();
            session.close();
            return movies;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public List<MovieEntity> findAllFilmsByFullName(ActorEntity actor1, ActorEntity actor2) throws Exception {
        Session session = Connection.getSessionFactory().openSession();
        Query<MovieEntity> query = session.createQuery("select mv from MovieEntity as mv " +
                "join mv.actors as actr1 " +
                "join mv.actors as actr2 " +
                "where actr1.firstName = :fname1 and actr1.lastName = :lname1 " +
                "and actr2.firstName = :fname2 and actr2.lastName = :lname2 ");
        query.setParameter("fname1", actor1.getFirstName());
        query.setParameter("lname1", actor1.getLastName());
        query.setParameter("fname2", actor2.getFirstName());
        query.setParameter("lname2", actor2.getLastName());
        return query.list();
    }
}

