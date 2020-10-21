package se325.assignment01.concert.service.domain;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

//Create new table with name CONCERTS
@Entity
@Table(name = "CONCERTS")
public class Concert {

    //Identify this value as primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "BLURB", length = 1024)
    private String blurb;

    //Create new join table which uses the id as foreign key for both concerts and performers.
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "CONCERT_PERFORMER", joinColumns = @JoinColumn(name = "CONCERT_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "PERFORMER_ID", referencedColumnName = "id"))
    @Column(name = "PERFORMER")
    private Set<Performer> performers;

    //Create collection table with lazy fetch strategy
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "CONCERT_DATES")
    @Column(name = "DATE")
    private Set<LocalDateTime> dates;

    public Concert() {}

    public  Concert(Long id, String title, String blurb, String imageName) {
        this.id = id;
        this.title = title;
        this.blurb = blurb;
        this.imageName = imageName;
    }

    /*
	Getters and setters
	 */

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBlurb() {
        return blurb;
    }

    public String getImageName() {
        return imageName;
    }

    public Set<Performer> getPerformers() {
        return performers;
    }

    public Set<LocalDateTime> getDates() {
        return dates;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public void setImageName(String imgUrl) {
        this.imageName = imgUrl;
    }

    public void setPerformers(Set<Performer> performers) {
        this.performers = performers;
    }

    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }

    @Override
    public boolean equals(Object obj) {
        // Implement value-equality based on a Concert's title alone. ID isn't
        // included in the equality check because two Concert objects could
        // represent the same real-world Concert, where one is stored in the
        // database (and therefore has an ID - a primary key) and the other
        // doesn't (it exists only in memory).
        if (!(obj instanceof Concert))
            return false;
        if (obj == this)
            return true;

        Concert rhs = (Concert) obj;
        return new EqualsBuilder().
                append(title, rhs.title).
                isEquals();
    }

    @Override
    public int hashCode() {
        // Hash-code value is derived from the value of the title field. It's
        // good practice for the hash code to be generated based on a value
        // that doesn't change.
        return new HashCodeBuilder(17, 31).
                append(title).hashCode();
    }
}
