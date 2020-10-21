package se325.assignment01.concert.service.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class AuthToken {

    //Only one user can be associated to one token
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Id
    public String tokenId;

    //Time when token will expire (set as 30 mins after time of successful login)
    private LocalDateTime expiry;

    public AuthToken() {}

    public AuthToken(User user, String tokenId, LocalDateTime expiry) {
        this.user = user;
        this.tokenId = tokenId;
        this.expiry = expiry;

    }

    public User getUser() {
        return user;
    }

    public String getTokenId() {
        return tokenId;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }
}
