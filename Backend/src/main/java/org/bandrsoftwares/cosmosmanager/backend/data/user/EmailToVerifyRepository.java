package org.bandrsoftwares.cosmosmanager.backend.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailToVerifyRepository extends JpaRepository<EmailToVerify, Integer> {

    EmailToVerify findByEmail(String email);
}