package se325.assignment01.concert.service.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private Long concertId;

    /*
    We use persist cascading so that there is consistency between tables. No duplicates
    and added if they don't already exist in database. Lazy loading because of
    Many to Many relationship.
     */
    @OneToMany(mappedBy = "booking")
    private List<Seat> seatList;

    @ManyToOne
    private User user;

    public Booking(Long concertId, LocalDateTime date, List<Seat> seatList, User user) {
        this.concertId = concertId;
        this.date = date;
        this.seatList = seatList;
        this.user = user;
    }

    public Booking(Long concertId, LocalDateTime date) {
        this.concertId = concertId;
        this.date = date;
    }

    public Booking() {}

    /*
	Getters and setters
	 */
    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getConcertId() {
        return concertId;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
