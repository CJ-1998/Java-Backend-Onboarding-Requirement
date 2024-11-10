package com.java.backend.onboarding_requirement.user.repository;

import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
