package com.shulpov.spots_app.authentication_management.tokens;

import com.shulpov.spots_app.users.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
    @Schema(description = "Id токена в БД", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Schema(description = "Значение токена",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGluZ0RhdGUiOjE2OTkyNTU3NzUyODAsInN1YiI6ImFsZXhfZ3JlZW5AZ21haWwuY" +
                    "29tIiwiaWF0IjoxNjk5MjU1Nzc1LCJleHAiOjE2OTk4NjA1NzV9.AErZPqLTbVykO11Ro16c_BmvSiCPJuglNRpcPybi7sY")
    @Column(unique = true)
    public String value;

    @Schema(description = "Тип токена (используется только REFRESH)", example = "REFRESH")
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
