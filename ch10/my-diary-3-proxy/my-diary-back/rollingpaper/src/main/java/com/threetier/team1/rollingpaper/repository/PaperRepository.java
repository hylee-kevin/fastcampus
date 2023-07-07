package com.threetier.team1.rollingpaper.repository;

import com.threetier.team1.rollingpaper.domain.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepository extends JpaRepository<Paper,Long> {
}
