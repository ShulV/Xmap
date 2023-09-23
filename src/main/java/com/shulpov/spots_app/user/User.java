package com.shulpov.spots_app.user;

import com.shulpov.spots_app.auth.token.Token;
import com.shulpov.spots_app.models.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
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
    private Date birthday;

    @Column(name = "reg_date")
    private Date regDate;

    @Transient
    @NotNull(message = "Пароль не должен быть пустой")
    @Size(min = 6, max = 50, message = "Длина пароля должна быть от 6 до 50 символов")
    private String password;

    @Enumerated(EnumType.STRING)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Token> tokens;

    @OneToMany(mappedBy = "commentator")
    private List<Comment> comments;

    @OneToMany(mappedBy = "userActor")
    private List<SpotUser> spotUserList;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    /**
     * @return
     */
    //возвращаем hash, т.к. под капотом Security использует
    //passEncoder.matches(presentedPassword, userDetails.getPassword())
    //где getPassword должен возвращать хэш из БД
    @Override
    public String getPassword() {
        return passHash;
    }

    /**
     * Получить логин пользователя (email). P.S. такое название метода у интерфейса...
     * @return email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * @return
     */
    //нет функционала, связанного со временем жизни аккаунта
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return
     */
    //нет функционала, связанного с блокировкой пользователя
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return
     */
    //нет функционала, связанного со временем жизни аутентификационных данных
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return
     */
    //нет функционала, связанного с выключением аккаунта
    @Override
    public boolean isEnabled() {
        return true;
    }

}
