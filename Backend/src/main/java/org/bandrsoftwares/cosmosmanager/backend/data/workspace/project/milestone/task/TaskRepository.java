package org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}