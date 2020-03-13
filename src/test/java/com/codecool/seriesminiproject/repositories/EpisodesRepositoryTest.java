package com.codecool.seriesminiproject.repositories;

import com.codecool.seriesminiproject.entity.Episode;
import com.codecool.seriesminiproject.entity.Season;
import com.codecool.seriesminiproject.entity.Series;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EpisodesRepositoryTest {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void episodesSaved() {
        Episode episode = Episode.builder()
                .title("1st episode of the series")
                .length(50)
                .build();

        Episode episode2 = Episode.builder()
                .title("2nd episode of the series")
                .length(50)
                .build();

        episodeRepository.save(episode);

        assertThat(episodeRepository.findAll())
                .hasSize(1)
                .containsExactly(episode);
    }

    @Test
    public void multipleEpisodesSaved() {
        Episode episode = Episode.builder()
                .title("1st episode of the series")
                .length(50)
                .build();

        Episode episode2 = Episode.builder()
                .title("2nd episode of the series")
                .length(50)
                .build();

        episodeRepository.saveAll(Lists.newArrayList(episode, episode2));

        assertThat(episodeRepository.findAll())
                .hasSize(2)
                .containsExactlyInAnyOrder(episode, episode2);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void episodeSaveRequiresTitle() {
        Episode episode = Episode.builder()
                .length(50)
                .build();

        episodeRepository.save(episode);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void cannotSaveEpisodeWithUniqueFieldTwice() {
        Episode episode = Episode.builder()
                .title("1st episode of the series")
                .length(50)
                .build();

        episodeRepository.save(episode);

        Episode episode2 = Episode.builder()
                .title("1st episode of the series")
                .length(60)
                .build();

        episodeRepository.saveAndFlush(episode2);
    }

    @Test
    public void findsEpisodesBetweenEpisodeCounts() {
        Set<Episode> testEpisodes = IntStream.range(1, 25)
                .boxed()
                .map(integer ->
                        Episode.builder()
                                .title(integer + ". episode of Season Test.")
                                .episodeCount(integer)
                                .build())
                .collect(Collectors.toSet());

        episodeRepository.saveAll(testEpisodes);

        assertThat(episodeRepository.findByEpisodeCountBetween(2, 10))
            .hasSize(9);
    }

    @Test
    public void findEpisodesFromSeasonUnderCount() {

        Set<Episode> s1Episodes = IntStream.range(1, 25)
                .boxed()
                .map(integer ->
                        Episode.builder()
                                .title(integer + ". episode of Season I.")
                                .episodeCount(integer)
                                .seasonCount(1)
                                .build())
                .collect(Collectors.toSet());

        Set<Episode> s2Episodes = IntStream.range(1, 25)
                .boxed()
                .map(integer ->
                        Episode.builder()
                                .title(integer + ". episode of Season II.")
                                .episodeCount(integer)
                                .seasonCount(2)
                                .build())
                .collect(Collectors.toSet());

        episodeRepository.saveAll(s1Episodes);
        episodeRepository.saveAll(s2Episodes);

        assertThat(episodeRepository.findBySeasonCountEqualsAndEpisodeCountLessThan(1, 5))
                .hasSize(4);
    }

    @Test
    public void findEpisodesByTitleContainingCharacters() {
        Set<Episode> s1Episodes = IntStream.range(1, 25)
                .boxed()
                .map(integer ->
                        Episode.builder()
                                .title(integer + ". episode of Season I.")
                                .episodeCount(integer)
                                .seasonCount(1)
                                .build())
                .collect(Collectors.toSet());

        episodeRepository.saveAll(s1Episodes);

        Set<Episode> foundEpisodes = episodeRepository.findAll().stream()
                .filter(episode -> episode.getTitle().contains("1"))
                .collect(Collectors.toSet());
        System.out.println(foundEpisodes.size());
        System.out.println(foundEpisodes);

        assertThat(episodeRepository.findEpisodesWhereTitleContainingChars("1"))
                .hasSize(12);
    }

    @Test
    public void updateSeasonCountWhereEpisodeTitleStartsWithSelectedChar() {
        Set<Episode> s1Episodes = IntStream.range(1, 25)
                .boxed()
                .map(integer ->
                        Episode.builder()
                                .title(integer + ". episode of Season I.")
                                .episodeCount(integer)
                                .seasonCount(1)
                                .build())
                .collect(Collectors.toSet());

        episodeRepository.saveAll(s1Episodes);

        int updatedRows = episodeRepository.updateSeasonCountWhereEpisodeTitleStartsWith("2");

        assertThat(updatedRows)
                .isEqualTo(6);

        assertThat(episodeRepository.findAll())
                .anyMatch(episode -> episode.getSeasonCount() == 2);
    }

}