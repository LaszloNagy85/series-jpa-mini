package com.codecool.seriesminiproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Episode {

    @Id
    @GeneratedValue
    private Long id;


    private int episodeCount;

    @Column(nullable = false, unique = true)
    private String title;

    private int length;

    private int seasonCount;

//    @Column(nullable = false)
    @ManyToOne
    private Season season;

    @ElementCollection
    @Singular
    @EqualsAndHashCode.Exclude
    private List<String> actors;
}
