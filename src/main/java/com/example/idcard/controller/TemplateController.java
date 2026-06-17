package com.example.idcard.controller;

import com.example.idcard.model.Template;
import com.example.idcard.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("templates", templateService.findAll());
        return "template/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("template", new Template());
        return "template/form";
    }

    @PostMapping
    public String save(@ModelAttribute Template template) {
        templateService.save(template);
        return "redirect:/templates";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("template", templateService.findById(id).orElseThrow());
        return "template/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Template template) {
        template.setId(id);
        templateService.save(template);
        return "redirect:/templates";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        templateService.delete(id);
        return "redirect:/templates";
    }
}