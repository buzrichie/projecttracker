package org.amalitechrichmond.projecttracker.service.impl;

import org.amalitechrichmond.projecttracker.DTO.DeveloperDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.mapper.DeveloperMapper;
import org.amalitechrichmond.projecttracker.model.Developer;
import org.amalitechrichmond.projecttracker.repository.DeveloperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeveloperServiceImplTest {

    private DeveloperRepository developerRepository;
    private DeveloperServiceImpl developerService;

    @BeforeEach
    void setUp() {
        developerRepository = mock(DeveloperRepository.class);
        developerService = new DeveloperServiceImpl(developerRepository);
    }

    @Test
    void getDeveloperById_shouldReturnDTO_whenFound() {
        Developer developer = Developer.builder()
                .id(1L)
                .name("Jane Smith")
                .email("jane@example.com")
                .skills(List.of("Python"))
                .build();

        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));

        DeveloperDTO result = developerService.getDeveloperById(1L);

        assertNotNull(result);
        assertEquals("Jane Smith", result.getName());
        verify(developerRepository, times(1)).findById(1L);
    }

    @Test
    void getDeveloperById_shouldThrowException_whenNotFound() {
        when(developerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> developerService.getDeveloperById(99L));
    }


    @Test
    void deleteDeveloper_shouldDeleteAndReturnDTO() {
        Developer dev = Developer.builder()
                .id(1L)
                .name("To Delete")
                .email("del@example.com")
                .skills(List.of("SQL"))
                .build();

        when(developerRepository.findById(1L)).thenReturn(Optional.of(dev));
        doNothing().when(developerRepository).delete(dev);

        DeveloperDTO result = developerService.deleteDeveloper(1L);

        assertEquals("To Delete", result.getName());
        verify(developerRepository).delete(dev);
    }

    @Test
    void getTop5Developers_shouldReturnList() {
        Developer dev1 = Developer.builder().id(1L).name("Dev One").email("dev1@example.com").build();
        Developer dev2 = Developer.builder().id(2L).name("Dev Two").email("dev2@example.com").build();

        when(developerRepository.findTop5ByOrderByTasksSizeDesc())
                .thenReturn(List.of(dev1, dev2));

        List<DeveloperDTO> result = developerService.getTop5Developers();

        assertEquals(2, result.size());
        verify(developerRepository).findTop5ByOrderByTasksSizeDesc();
    }
}