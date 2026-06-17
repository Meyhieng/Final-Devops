package com.example.idcard.service;

import com.example.idcard.model.Template;
import com.example.idcard.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public List<Template> findAll() {
        return templateRepository.findAll();
    }

    public Optional<Template> findById(Long id) {
        return templateRepository.findById(id);
    }

    public Template save(Template template) {
        return templateRepository.save(template);
    }

    public void delete(Long id) {
        templateRepository.deleteById(id);
    }

    public boolean existsByCode(String code) {
        return templateRepository.existsByCode(code);
    }
}