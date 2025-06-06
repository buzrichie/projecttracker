package org.amalitechrichmond.projecttracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.ProjectDTO;
import org.amalitechrichmond.projecttracker.model.Project;
import org.amalitechrichmond.projecttracker.repository.ProjectRepository;
import org.amalitechrichmond.projecttracker.mapper.ProjectMapper;
import org.amalitechrichmond.projecttracker.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final AuditLogServiceImpl auditService;

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = ProjectMapper.toEntity(projectDTO);
        project.setDeadline(java.time.LocalDate.now());
        Project saved = projectRepository.save(project);
        auditService.saveLog("CREATE","Project",String.valueOf(saved.getId()), saved,"developer");
        return ProjectMapper.toDTO(saved);
    }

    @Override
    public List<ProjectDTO> getAllProjects(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.stream().map(ProjectMapper::toDTO).toList();
    }


    @Override
    public ProjectDTO getProjectById(long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        return ProjectMapper.toDTO(project);
    }

    @Override
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
        auditService.saveLog("UPDATE","Project",String.valueOf(updated.getId()), String.valueOf(updated),"developer");
        return ProjectMapper.toDTO(updated);
    }

    @Override
    public ProjectDTO deleteProject(long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        projectRepository.delete(project);
        auditService.saveLog("DELETE","Project",String.valueOf(id), String.valueOf(project),"developer");
        return ProjectMapper.toDTO(project);
    }
}
