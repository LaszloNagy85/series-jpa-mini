package com.codecool.seriesminiproject;

import com.codecool.seriesminiproject.entity.Episode;
import com.codecool.seriesminiproject.entity.GenreType;
import com.codecool.seriesminiproject.entity.Season;
import com.codecool.seriesminiproject.entity.Series;
import com.codecool.seriesminiproject.repositories.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class SeriesMiniProjectApplication {

    @Autowired
    private SeriesRepository seriesRepository;

    public static void main(String[] args) {
        SpringApplication.run(SeriesMiniProjectApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init() {
        return args -> {

            Season season1 = Season.builder()
                    .seasonCount(1L)
                    .releaseDate(1994L)
                    .build();

            Season season2 = Season.builder()
                    .seasonCount(2L)
                    .releaseDate(1995L)
                    .build();
//
//            Episode s1e01 = Episode.builder()
//                    .title("1st of 1st")
//                    .season(season1)
//                    .episodeCount(1)
//                    .build();
//
//            Episode s1e02= Episode.builder()
//                    .title("2nd of 1st")
//                    .season(season1)
//                    .episodeCount(2)
//                    .build();
//
//            Episode s2e01= Episode.builder()
//                    .title("1st of 2nd")
//                    .season(season2)
//                    .episodeCount(1)
//                    .build();
//
//            Episode s2e02= Episode.builder()
//                    .title("2nd of 2nd")
//                    .season(season2)
//                    .episodeCount(2)
//                    .build();
//
//            season1.setEpisodes(Set.of(s1e01, s1e02));
//            season2.setEpisodes(Set.of(s2e01, s2e02));

            Set<Episode> s1Episodes = IntStream.range(1, 25)
                    .boxed()
                    .map(integer ->
                            Episode.builder()
                                    .title(integer + ". episode of Season I.")
                                    .episodeCount(integer)
                                    .season(season1)
                                    .build())
                    .collect(Collectors.toSet());

            Set<Episode> s2Episodes = IntStream.range(1, 25)
                    .boxed()
                    .map(integer ->
                            Episode.builder()
                                    .title(integer + ". episode of Season II.")
                                    .episodeCount(integer)

                                    .season(season2)
                                    .build())
                    .collect(Collectors.toSet());

            season1.setEpisodes(s1Episodes);
            season2.setEpisodes(s2Episodes);

            Series friends = Series.builder()
                    .name("Friends")
                    .genre(GenreType.COMEDY)
                    .releaseDate(LocalDate.of(1994, 9, 22))
                    .season(season1)
                    .season(season2)
                    .build();

            season1.setSeries(friends);
            season2.setSeries(friends);

            seriesRepository.save(friends);
            List<Series> test = seriesRepository.findAll();
            System.out.println(test);
        };
    }
}
