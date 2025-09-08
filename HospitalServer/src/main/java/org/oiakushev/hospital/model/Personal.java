package org.oiakushev.hospital.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "h_personal")
@Getter
@Setter
public class Personal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    private String password;

    // 0 - read only; 1 - create patient and read only; 2 - write; 3 - all rights.
    private Integer role;

    @JsonIgnore
    private String token;

    @CreationTimestamp
    private Date createdAt;

    private String firstName;
    private String lastName;
    private String phone;
    private String description;
}
