package sn.sonatel.dsi.ins.imoc.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "reservation_id")
    private Long reservationId;

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @NotNull
    @Column(name = "jhi_read", nullable = false)
    private Boolean read;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getReservationId() {
        return this.reservationId;
    }

    public Notification reservationId(Long reservationId) {
        this.setReservationId(reservationId);
        return this;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public Notification userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Notification deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getRead() {
        return this.read;
    }

    public Notification read(Boolean read) {
        this.setRead(read);
        return this;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", reservationId=" + getReservationId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", read='" + getRead() + "'" +
            "}";
    }
}
