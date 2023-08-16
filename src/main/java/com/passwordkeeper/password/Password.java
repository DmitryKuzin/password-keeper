package com.passwordkeeper.password;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Password {

    @Id
    private String id;
    private String userId;
    private String websiteName;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Password password = (Password) o;
        return id != null && Objects.equals(id, password.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
