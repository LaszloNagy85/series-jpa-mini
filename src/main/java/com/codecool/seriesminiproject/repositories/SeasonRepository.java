package com.codecool.seriesminiproject.repositories;

import com.codecool.seriesminiproject.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
