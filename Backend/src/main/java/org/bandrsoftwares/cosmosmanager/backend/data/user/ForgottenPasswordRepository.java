package org.bandrsoftwares.cosmosmanager.backend.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForgottenPasswordRepository extends JpaRepository<ForgottenPassword, Integer> {

    List<ForgottenPassword> findByUser(User user);

    ForgottenPassword findByNonce(Long nonce);

    void deleteAllByUser(User user);
}