package com.shulpov.spots_app.user;

import com.shulpov.spots_app.auth_management.token.Token;
import com.shulpov.spots_app.location.models.City;
import com.shulpov.spots_app.models.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
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
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "pass_hash")
    private String passHash;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "reg_date")
    private Date regDate;

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
