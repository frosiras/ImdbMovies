package movies.models;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name="Movie")
public class MovieEntity {
    private String imdbId;
    private String title;
    private int year;
    private String plot;
    private String posterURL;
    private List<ActorEntity> actors;
    private int awardWins;
    private int awardNominations;
    private double tomatoMeter;

    @Id
    @Column(name = "imdbId", unique = true, nullable = false, length = 9)
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    @Column(name = "Title", nullable = false)
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @Column(name = "Year")
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    @Column(name = "Plot")
    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }

    @Column(name = "PosterURL")
    public String getPosterURL() { return posterURL; }
    public void setPosterURL(String posterURL) { this.posterURL = posterURL; }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "Movie_Actor",
            joinColumns = {@JoinColumn(name = "imdb_id", referencedColumnName = "imdbId",updatable = false)},
            inverseJoinColumns = { @JoinColumn(name = "actor_id", referencedColumnName = "actorId")})
    public List<ActorEntity> getActors() { return actors; }
    public void setActors(List<ActorEntity> actors) { this.actors = actors; }

    @Column(name = "AwardWins")
    public int getAwardWins() { return awardWins; }
    public void setAwardWins(int awardWins) { this.awardWins = awardWins; }

    @Column(name = "AwardNominations")
    public int getAwardNominations() { return awardNominations; }
    public void setAwardNominations(int awardNominations) { this.awardNominations = awardNominations; }

    @Column(name = "TomatoMeter")
    public double getTomatoMeter() { return tomatoMeter; }
    public void setTomatoMeter(double tomatoMeter) { this.tomatoMeter = tomatoMeter; }

    @Override
    public String toString() {
        return  "\n{ imdbID : " + imdbId + "\n" +
                " Title : " + title + "\n" +
                " Year : " + year + "\n" +
                " Plot : " + plot + "\n" +
                " PosterURL : " + posterURL + "\n" +
                " AwardWins : " + awardWins + "\n" +
                " AwardNominations : " + awardNominations + "\n" +
                " TomatoMeter : " + tomatoMeter + " }\n";
    }
}
