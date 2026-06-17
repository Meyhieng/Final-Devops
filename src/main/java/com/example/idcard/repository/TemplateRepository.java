package com.example.idcard.repository;

import com.example.idcard.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByCode(String code);
    Optional<Template> findByName(String name);
    boolean existsByCode(String code);
    boolean existsByName(String name);
    List<Template> findByLayout(String layout);
}