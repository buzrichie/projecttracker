package org.amalitechrichmond.projecttracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.ProjectDTO;
import org.amalitechrichmond.projecttracker.DTO.ProjectSummaryDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.model.Project;
import org.amalitechrichmond.projecttracker.model.User;
import org.amalitechrichmond.projecttracker.repository.ProjectRepository;
import org.amalitechrichmond.projecttracker.mapper.ProjectMapper;
import org.amalitechrichmond.projecttracker.service.ProjectService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final AuditLogServiceImpl auditService;

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "projectById"}, allEntries = true)
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = ProjectMapper.toEntity(projectDTO);
        project.setDeadline(java.time.LocalDate.now());
        Project saved = projectRepository.save(project);
        return ProjectMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "projects", key = "'page_' + #page + '_size_' + #size + '_sort_' + #sortBy + '_' + #sortDir")
    public List<ProjectDTO> getAllProjects(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.stream().map(ProjectMapper::toDTO).toList();
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "projectById", key = "#id")
    public ProjectDTO getProjectById(long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        return ProjectMapper.toDTO(project);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "projectById"}, key = "#projectDTO.id", allEntries = true)
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        if (projectDTO.getId() == null) {
            throw new IllegalArgumentException("Project ID must be provided for update.");
        }

        Project existing = projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectDTO.getId()));

        existing.setName(projectDTO.getName());
        existing.setDescription(projectDTO.getDescription());
        existing.setDeadline(projectDTO.getDeadline());
        existing.setStatus(projectDTO.getStatus());

        Project updated = projectRepository.save(existing);
        return ProjectMapper.toDTO(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "projectById"}, key = "#id", allEntries = true)
    public ProjectDTO deleteProject(long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        projectRepository.delete(project);
        return ProjectMapper.toDTO(project);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "projectsWithoutTasks")
    public List<ProjectDTO> getProjectsWithoutTasks() {
        return projectRepository.findProjectsWithoutTasks()
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectSummaryDTO getProjectSummary(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectSummaryDTO summary = new ProjectSummaryDTO();
        summary.setProjectId(project.getId());
        summary.setProjectName(project.getName());
        summary.setDescription(project.getDescription());
        summary.setStatus(project.getStatus().name());

        int totalTasks = project.getTasks().size();
        int completedTasks = (int) project.getTasks().stream()
                .filter(task -> task.getStatus().name().equals("COMPLETED"))
                .count();

        summary.setTotalTasks(totalTasks);
        summary.setCompletedTasks(completedTasks);

        return summary;
    }


}
