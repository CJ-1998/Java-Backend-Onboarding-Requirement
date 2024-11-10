package com.java.backend.onboarding_requirement.user.repository;

import com.java.backend.onboarding_requirement.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
