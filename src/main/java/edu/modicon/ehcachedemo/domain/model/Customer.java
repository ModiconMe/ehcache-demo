package edu.modicon.ehcachedemo.domain.model;

import javax.persistence.*;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer",
        uniqueConstraints = {@UniqueConstraint(name = "customer_email_unique", columnNames = "email")}
)
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 7467498459944585785L;

    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private int age;

}
