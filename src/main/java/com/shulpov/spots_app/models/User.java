package com.shulpov.spots_app.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 2, max = 30, message = "Длина имени должна быть от 2 до 30 символов")
    private String name;

    @Column(name = "email")
    @NotNull(message = "Email не должен быть пустой")
    @Email(message = "Email должен быть валидным")
    @Size(min = 5, max = 50, message = "Длина почты должна быть от 5 до 50 символов")
    private String email;

    @Column(name = "pass_hash")
    private String passHash;

    @Column(name = "phone_number")
    @NotEmpty(message = "Номер не должен быть пустым")
    private String phoneNumber;

    @Column(name = "birthday")
    @NotNull(message = "Дата дня рождения не должна быть пустой")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthday;

    @Column(name = "reg_date")
    private Date regDate;

    @Transient
    @NotNull(message = "Пароль не должен быть пустой")
    @Size(min = 6, max = 50, message = "Длина пароля должна быть от 6 до 50 символов")
    private String password;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "photographedUser",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private List<ImageInfo> imageInfos;

    @OneToMany(mappedBy = "userActor",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private List<SpotUser> spotUsers;

    @OneToMany(mappedBy = "creatorUser",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private List<Spot> createdSpots;

    @OneToMany(mappedBy = "moderUser",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private List<Spot> acceptedSpots;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRoleCodeName() {
        return role.getNameCode();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ImageInfo> getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(List<ImageInfo> imageInfos) {
        this.imageInfos = imageInfos;
    }

    public List<SpotUser> getSpotUsers() {
        return spotUsers;
    }

    public void setSpotUsers(List<SpotUser> spotUsers) {
        this.spotUsers = spotUsers;
    }

    public List<Spot> getCreatedSpots() {
        return createdSpots;
    }

    public void setCreatedSpots(List<Spot> createdSpots) {
        this.createdSpots = createdSpots;
    }

    public List<Spot> getAcceptedSpots() {
        return acceptedSpots;
    }

    public void setAcceptedSpots(List<Spot> acceptedSpots) {
        this.acceptedSpots = acceptedSpots;
    }
}
