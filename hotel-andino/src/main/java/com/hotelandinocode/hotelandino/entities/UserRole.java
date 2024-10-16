package com.hotelandinocode.hotelandino.entities;

import com.hotelandinocode.hotelandino.enums.UserRoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "users")
@Entity
@Table(name = "rol_usuario")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol")
    private UserRoleEnum roleName;

    // Relación inversa con la entidad User
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public UserRole(@NotNull UserRoleEnum userRoleEnum) {
        this.roleName = userRoleEnum;
    }
}
