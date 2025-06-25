package org.amalitechrichmond.projecttracker.service.impl;


import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.model.Developer;
import org.amalitechrichmond.projecttracker.model.Project;
import org.amalitechrichmond.projecttracker.model.Task;
import org.amalitechrichmond.projecttracker.repository.DeveloperRepository;
import org.amalitechrichmond.projecttracker.repository.ProjectRepository;
import org.amalitechrichmond.projecttracker.repository.TaskRepository;
import org.amalitechrichmond.projecttracker.util.AccessChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock private TaskRepository taskRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private DeveloperRepository developerRepository;
    @Mock private AccessChecker accessChecker;

    @InjectMocks private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_ShouldReturnCreatedTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setProjectId(1L);

        Project project = new Project();
        project.setId(1L);

        Task savedTask = new Task();
        savedTask.setId(10L);
        savedTask.setTitle("Test Task");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_NotFound_ShouldThrowException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    void deleteTask_ShouldReturnDeletedDTO() {
        Task task = new Task();
        task.setId(5L);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));

        TaskDTO dto = taskService.deleteTask(5L);

        assertNotNull(dto);
        verify(taskRepository).delete(task);
    }

    @Test
    void updateTask_ShouldUpdateAndReturnDTO() {
        Task existing = new Task();
        existing.setId(1L);
        existing.setDevelopers(Set.of(new Developer()));

        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setId(1L);
        updateDTO.setTitle("Updated");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(accessChecker.isOwner(any())).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(existing);

        TaskDTO result = taskService.updateTask(updateDTO);

        assertNotNull(result);
        verify(taskRepository).save(existing);
    }

    @Test
    void updateTask_Unauthorized_ShouldThrowException() {
        Task task = new Task();
        task.setId(2L);
        task.setDevelopers(Set.of(new Developer()));

        TaskDTO dto = new TaskDTO();
        dto.setId(2L);

        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));
        when(accessChecker.isOwner(any())).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> taskService.updateTask(dto));
    }
}
