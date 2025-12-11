package com.example.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WikiPageRepository extends JpaRepository<WikiPage, Long> {
}
