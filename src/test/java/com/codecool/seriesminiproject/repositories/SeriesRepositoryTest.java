package com.codecool.seriesminiproject.repositories;

import com.codecool.seriesminiproject.entity.GenreType;
import com.codecool.seriesminiproject.entity.Season;
import com.codecool.seriesminiproject.entity.Series;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class SeriesRepositoryTest {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void saveSingleSeries() {
        Series series = Series.builder()
                .name("Friends")
                .releaseDate(LocalDate.of(1994, 9, 22))
                .genre(GenreType.COMEDY)
                .build();

        seriesRepository.save(series);

        assertThat (seriesRepository.findAll())
            .hasSize(1)
            .containsExactly(series);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void doesNotBreakUniqueFieldRules() {
        Series series1 = Series.builder()
                .name("Friends")
                .releaseDate(LocalDate.of(1994, 9, 22))
                .genre(GenreType.COMEDY)
                .build();

        seriesRepository.save(series1);

        Series series2 = Series.builder()
                .name("Friends")
                .releaseDate(LocalDate.of(1994, 9, 22))
                .genre(GenreType.COMEDY)
                .build();

        seriesRepository.saveAndFlush(series2);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void nameShouldNotBeNull() {
        Series series = Series.builder()
                .releaseDate(LocalDate.of(1994, 9, 22))
                .genre(GenreType.COMEDY)
                .build();

        seriesRepository.save(series);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void releaseDateShouldNotBeNull() {
        Series series = Series.builder()
                .name("Friends")
                .genre(GenreType.COMEDY)
                .build();

        seriesRepository.save(series);
    }

    @Test
    public void transientShouldNotBeSaved() {
        Series series = Series.builder()
                .name("Friends")
                .releaseDate(LocalDate.of(1994, 9, 22))
                .totalLength(234L)
                .genre(GenreType.COMEDY)
                .build();

        seriesRepository.save(series);
        entityManager.clear();

        assertThat(seriesRepository.findAll())
                .allMatch(series1 -> series1.getTotalLength() == 0L);
    }

    @Test
    public void persistsSeasons() {
        Season season1 = Season.builder()
                .releaseDate(1994L)
                .seasonCount(1L)
                .build();

        Series series = Series.builder()
                .name("Friends")
                .releaseDate(LocalDate.of(1994, 9, 22))
                .genre(GenreType.COMEDY)
                .season(season1)
                .build();

        season1.setSeries(series);

        seriesRepository.save(series);

        assertThat(seasonRepository.findAll())
                .hasSize(1)
                .containsExactly(season1);
    }

}