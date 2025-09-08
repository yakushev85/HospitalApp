package org.oiakushev.hospital.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "h_blood")
@Getter
@Setter
public class Blood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Male: 13.5-17.5 g/dL Female: 12.0-16.0 g/dL
    private Double hemoglobin;

    // Male: 41%-53% Female: 36%-46%
    private Double hematocrit;

    // 80-100 µm3
    private Double meanCorpuscularVolume;

    // 25.4-34.6 pg/cell
    private Double meanCorpuscularHemoglobin;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "personal_id", nullable = false)
    private Personal personal;

    @CreationTimestamp
    private Date createdAt;
}
