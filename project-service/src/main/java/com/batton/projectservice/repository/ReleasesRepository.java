package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Releases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReleasesRepository extends JpaRepository<Releases, Long> {
}
