package com.example.idcard.repository;

import com.example.idcard.enums.ProfileType;
import com.example.idcard.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByType(ProfileType type);
    Optional<Profile> findByRegistrationNumber(String registrationNumber);
    Optional<Profile> findByUuid(String uuid);
    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByEmail(String email);
    List<Profile> findByFullNameContainingIgnoreCase(String name);
    List<Profile> findByDepartment(String department);
}