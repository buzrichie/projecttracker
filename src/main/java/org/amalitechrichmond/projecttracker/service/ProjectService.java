package org.amalitechrichmond.projecttracker.service;

import org.amalitechrichmond.projecttracker.DTO.ProjectDTO;
import org.amalitechrichmond.projecttracker.DTO.ProjectSummaryDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO);
    List<ProjectDTO> getAllProjects(int page, int size, String sortBy, String sortDir);
    ProjectDTO getProjectById(long id);
    ProjectDTO updateProject(ProjectDTO projectDTO);
    ProjectDTO deleteProject(long id);
    List<ProjectDTO> getProjectsWithoutTasks();

    ProjectSummaryDTO getProjectSummary(Long id);
}
