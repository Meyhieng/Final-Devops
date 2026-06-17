package com.example.idcard.controller;

import com.example.idcard.model.Profile;
import com.example.idcard.service.ProfileService;
import com.example.idcard.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final TemplateService templateService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("profiles", profileService.findAll());
        return "profile/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("profileTypes", com.example.idcard.enums.ProfileType.values());
        return "profile/form";
    }

    @PostMapping
    public String save(@ModelAttribute Profile profile,
                       @RequestParam(value = "photo", required = false) MultipartFile photo,
                       @RequestParam(value = "template.id", required = false) Long templateId) throws Exception {
        if (templateId == null) {
            profile.setTemplate(null);
        }
        profileService.save(profile, photo);
        return "redirect:/profiles";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("profile", profileService.findById(id).orElseThrow());
        return "profile/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("profile", profileService.findById(id).orElseThrow());
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("profileTypes", com.example.idcard.enums.ProfileType.values());
        return "profile/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Profile profile,
                         @RequestParam(value = "photo", required = false) MultipartFile photo,
                         @RequestParam(value = "template.id", required = false) Long templateId) throws Exception {
        if (templateId == null) {
            profile.setTemplate(null);
        }
        profile.setId(id);
        profileService.save(profile, photo);
        return "redirect:/profiles";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        profileService.delete(id);
        return "redirect:/profiles";
    }

    @GetMapping("/{id}/preview")
    public String preview(@PathVariable Long id, Model model) {
        model.addAttribute("profile", profileService.findById(id).orElseThrow());
        return "profile/preview";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) throws Exception {
        Profile p = profileService.findById(id).orElseThrow();
        byte[] pdf = com.example.idcard.util.PDFUtil.generate(p);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=idcard.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/batch-pdf")
    public void batchPdf(@RequestParam List<Long> ids,
                         jakarta.servlet.http.HttpServletResponse response) throws Exception {
        List<Profile> profiles = profileService.findByIds(ids);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=batch-idcards.pdf");
        com.example.idcard.util.PDFUtil.generateBatch(profiles, response.getOutputStream());
    }
}