package org.bandrsoftwares.cosmosmanager.backend.data.workspace;

import org.bandrsoftwares.cosmosmanager.backend.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {

    Workspace findByUserOwnerAndName(User owner, String workspaceName);
}