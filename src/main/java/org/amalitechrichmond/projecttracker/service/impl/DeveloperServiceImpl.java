package org.amalitechrichmond.projecttracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.DeveloperDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.mapper.DeveloperMapper;
import org.amalitechrichmond.projecttracker.model.Developer;
import org.amalitechrichmond.projecttracker.repository.DeveloperRepository;
import org.amalitechrichmond.projecttracker.service.DeveloperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    @Override
    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
        Developer developer = DeveloperMapper.toEntity(developerDTO);
        Developer saved = developerRepository.save(developer);
        return DeveloperMapper.toDTO(saved);
    }

    @Override
    public List<DeveloperDTO> getAllDevelopers() {
        return developerRepository.findAll().stream()
                .map(DeveloperMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeveloperDTO getDeveloperById(long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with ID: " + id));
        return DeveloperMapper.toDTO(developer);
    }

    @Override
    public DeveloperDTO updateDeveloper(DeveloperDTO developerDTO) {
        Developer developer = developerRepository.findById(developerDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with ID: " + developerDTO.getId()));

        developer.setName(developerDTO.getName());
        developer.setEmail(developerDTO.getEmail());
        developer.setSkills(developerDTO.getSkills());

        Developer updated = developerRepository.save(developer);
        return DeveloperMapper.toDTO(updated);
    }

    @Override
    public DeveloperDTO deleteDeveloper(long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with ID: " + id));
        developerRepository.delete(developer);
        return DeveloperMapper.toDTO(developer);
    }
}
