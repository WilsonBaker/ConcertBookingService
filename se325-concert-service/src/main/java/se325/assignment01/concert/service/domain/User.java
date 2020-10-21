package se325.assignment01.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    //Version ensures Optimistic Concurrency Control (OCC)
    @Version
    @Column(name = "VERSION")
    private int version;

    //One user can have many bookings but a booking can only be linked to one user
    @OneToMany(mappedBy = "user")
    private Set<Booking> bookingList = new HashSet<>();

    public User() {}

    public User(Long id, String username, String password, int version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.version = version;
    }

    /*
	Getters and setters
	 */

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getVersion() {
        return version;
    }

    public Set<Booking> getBookingList() {
        return bookingList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setBookingList(Set<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public void newBooking(Booking booking) {
        this.bookingList.add(booking);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;

        User rhs = (User) obj;
        return new EqualsBuilder().
                append(username, rhs.username).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(username).hashCode();
    }
}
