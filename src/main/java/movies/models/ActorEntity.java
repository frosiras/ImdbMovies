package movies.models;

import movies.utils.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Actor",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"FirstName", "LastName"})
        })
public class ActorEntity {
    private String firstName;
    private String lastName;
    private int actorId;
    private List<MovieEntity> movies;

    public ActorEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ActorEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ActorId", unique = true)
    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    @Column(name = "FirstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "actors")
    public List<MovieEntity> getMovies() {
        try {
            Session session = Connection.getSessionFactory().openSession();
            Query query = session.createQuery("select movies FROM ActorEntity act where act.fullName = :fullName");
            query.setParameter("fullName", this.getFullName());
            List<MovieEntity> list = query.getResultList();
            session.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setMovies(List<MovieEntity> movies) {
        this.movies = movies;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        if (fullName.contains(" ")) {
            setFirstName(fullName.substring(0, fullName.indexOf(" ")));
            setLastName(fullName.substring(fullName.indexOf(" ") + 1));
        } else {
            setFirstName(fullName);
            setLastName("N/A");
        }
    }

    @Override
    public String toString() {
        return "ActorEntity {" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", actorId=" + actorId +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        String fullname;
        if (lastName.equals("N/A"))
            fullname = this.firstName;
        else
            fullname = this.firstName + this.lastName;
        String objFullname = (String) obj;
        return fullname.equals(objFullname);
    }
}
