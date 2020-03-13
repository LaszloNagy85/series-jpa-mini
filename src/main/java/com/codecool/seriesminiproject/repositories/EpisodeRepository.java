package com.codecool.seriesminiproject.repositories;

import com.codecool.seriesminiproject.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findByEpisodeCountBetween(int startEpisodeCount, int endEpisodeCount);

    List<Episode> findBySeasonCountEqualsAndEpisodeCountLessThan(int seasonCount, int episodeCount);

    @Query("select e from Episode e where e.title like %:chars%")
    List<Episode> findEpisodesWhereTitleContainingChars(@Param ("chars") String chars);

    @Query("update Episode e set e.seasonCount = 2 where e.title like :startingChar%")
    @Modifying(clearAutomatically = true)
    int updateSeasonCountWhereEpisodeTitleStartsWith(@Param ("startingChar") String startingChar);



}
