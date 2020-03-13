package com.codecool.seriesminiproject.repositories;

import com.codecool.seriesminiproject.entity.Episode;
import com.codecool.seriesminiproject.entity.Season;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class SeasonRepositoryTest {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Test
    public void singleSeasonSaved() {
        Season season = Season.builder()
                .releaseDate(1994L)
                .seasonCount(1L)
                .build();

        seasonRepository.save(season);
        assertThat(seasonRepository.findAll())
                .hasSize(1)
                .containsOnlyOnce(season);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void doesNotBreakUniqueFieldRules() {
        Season season1 = Season.builder()
                .releaseDate(1994L)
                .seasonCount(1L)
                .build();

        seasonRepository.save(season1);

        Season season2 = Season.builder()
                .releaseDate(1994L)
                .seasonCount(1L)
                .build();

        seasonRepository.saveAndFlush(season2);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void doesNotAllowNullOnReleaseDate() {
        Season season = Season.builder()
                .seasonCount(1L)
                .build();

        seasonRepository.save(season);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void doesNotAllowNullOnSeasonCount() {
        Season season = Season.builder()
                .releaseDate(1994L)
                .build();

        seasonRepository.save(season);
    }

    @Test
    public void persistsEpisodes() {
        Episode episode1 = Episode.builder()
                .title("1st episode")
                .episodeCount(1)
                .length(50)
                .build();

        Episode episode2 = Episode.builder()
                .title("2nd episode")
                .episodeCount(2)
                .length(45)
                .build();

        Season season1 = Season.builder()
                .releaseDate(1994L)
                .seasonCount(1L)
                .episode(episode1)
                .episode(episode2)
                .build();

        seasonRepository.save(season1);
        assertThat(episodeRepository.findAll())
                .hasSize(2)
                .containsExactlyInAnyOrder(episode1, episode2);
    }





}