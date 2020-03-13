package com.codecool.seriesminiproject.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Series {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Transient
    private Long totalLength;

    @Enumerated(EnumType.STRING)
    private GenreType genre;


    @OneToMany(mappedBy = "series" , cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Singular
    @EqualsAndHashCode.Exclude
    private Set<Season> seasons;


    public void calculateTotalLength() {};
}
