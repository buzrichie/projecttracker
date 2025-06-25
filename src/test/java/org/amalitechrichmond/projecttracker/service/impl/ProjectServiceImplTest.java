package org.amalitechrichmond.projecttracker.service.impl;

import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.amalitechrichmond.projecttracker.DTO.ProjectDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.mapper.ProjectMapper;
import org.amalitechrichmond.projecttracker.model.Project;
import org.amalitechrichmond.projecttracker.repository.ProjectRepository;
import org.amalitechrichmond.projecttracker.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() {
        ProjectDTO dto = new ProjectDTO();
        dto.setName("New Project");

        Project entity = ProjectMapper.toEntity(dto);
        entity.setId(1L);

        when(projectRepository.save(any(Project.class))).thenReturn(entity);

        ProjectDTO result = projectService.createProject(dto);

        assertNotNull(result);
        assertEquals("New Project", result.getName());
        verify(auditLogService).saveLog(eq("CREATE"), eq("Project"), eq("1"), any(), any());
    }

    @Test
    void testGetAllProjects() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Demo");

//        when(projectRepository.findAll(any())).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(project)));

        List<ProjectDTO> result = projectService.getAllProjects(0, 10, "name", "asc");

        assertEquals(1, result.size());
        assertEquals("Demo", result.get(0).getName());
    }

    @Test
    void testGetProjectById_found() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Found Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectDTO result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Found Project", result.getName());
    }

    @Test
    void testGetProjectById_notFound() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> projectService.getProjectById(999L));
    }

    @Test
    void testDeleteProject() {
        Project project = new Project();
        project.setId(5L);
        project.setName("To Delete");

        when(projectRepository.findById(5L)).thenReturn(Optional.of(project));

        ProjectDTO result = projectService.deleteProject(5L);

        assertNotNull(result);
        verify(projectRepository).delete(project);
        verify(auditLogService).saveLog(eq("DELETE"), eq("Project"), eq("5"), any(), any());
    }

    @Test
    void testUpdateProject() {
        Project existing = new Project();
        existing.setId(3L);
        existing.setName("Old");

        ProjectDTO updateDto = new ProjectDTO();
        updateDto.setId(3L);
        updateDto.setName("Updated");

        when(projectRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectDTO result = projectService.updateProject(updateDto);

        assertEquals("Updated", result.getName());
        verify(auditLogService).saveLog(eq("UPDATE"), eq("Project"), eq("3"), any(), any());
    }
}
