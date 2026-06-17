package com.example.idcard.service;

import com.example.idcard.enums.ProfileType;
import com.example.idcard.model.Profile;
import com.example.idcard.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    private static final String UPLOAD_DIR = "uploads/photos/";

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Optional<Profile> findById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> findByUuid(String uuid) {
        return profileRepository.findByUuid(uuid);
    }

    public List<Profile> findByType(ProfileType type) {
        return profileRepository.findByType(type);
    }

    public List<Profile> findByIds(List<Long> ids) {
        return profileRepository.findAllById(ids);
    }

    public List<Profile> search(String name) {
        return profileRepository.findByFullNameContainingIgnoreCase(name);
    }

    public Profile save(Profile profile, MultipartFile photo) throws IOException {

        if (profile.getTemplate() != null && profile.getTemplate().getId() == null) {
            profile.setTemplate(null);
        }

        if (profile.getUuid() == null || profile.getUuid().isBlank()) {
            profile.setUuid(UUID.randomUUID().toString());
        }

        if (profile.getRegistrationNumber() == null || profile.getRegistrationNumber().isBlank()) {
            profile.setRegistrationNumber(generateRegNumber(profile));
        }

        if (photo != null && !photo.isEmpty()) {
            validatePhoto(photo);
            String filename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);
            Files.write(uploadPath.resolve(filename), photo.getBytes());
            profile.setPhotoFileName(filename);
            profile.setPhotoContentType(photo.getContentType());
        }

        return profileRepository.save(profile);
    }

    public void delete(Long id) {
        profileRepository.deleteById(id);
    }


    private String generateRegNumber(Profile profile) {
        int year = Year.now().getValue();
        String dept = (profile.getDepartment() != null && !profile.getDepartment().isBlank())
                ? profile.getDepartment().substring(0, Math.min(3, profile.getDepartment().length())).toUpperCase()
                : "GEN";
        long count = profileRepository.count() + 1;
        return String.format("%d-%s-%03d", year, dept, count);
    }

    private void validatePhoto(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Only JPEG/PNG images are allowed.");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be under 5MB.");
        }
    }
}