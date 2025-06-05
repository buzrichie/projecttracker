package org.amalitechrichmond.projecttracker.service;

import org.amalitechrichmond.projecttracker.DTO.DeveloperDTO;

import java.util.List;

public interface DeveloperService {
    DeveloperDTO createDeveloper(DeveloperDTO developerDTO);
    List<DeveloperDTO> getAllDevelopers();
    DeveloperDTO getDeveloperById(long id);
    DeveloperDTO updateDeveloper(DeveloperDTO developerDTO);
    DeveloperDTO deleteDeveloper(long id);
}
