package pl.kamil.zielinski.bookcentrerest.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.kamil.zielinski.bookcentrerest.purchase.PurchaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "client", schema = "bookcentre")
@JsonIgnoreProperties({"purchases"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    private int clientId;
    private String email;
    private String password;
    private String forename;
    private String surname;
    private String phone;
    private String address;
    private Collection<PurchaseEntity> purchases;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Basic
    @Email
    @Column(name = "email", length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "forename", length = 30)
    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    @Basic
    @Column(name = "surname", length = 30)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "phone", length = 9)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "address", length = 150)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientEntity that = (ClientEntity) o;
        return clientId == that.clientId &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(forename, that.forename) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, email, password, forename, surname, phone, address);
    }

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    public Collection<PurchaseEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(Collection<PurchaseEntity> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "clientId=" + clientId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
