package org.amalitechrichmond.projecttracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.DeveloperDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.mapper.DeveloperMapper;
import org.amalitechrichmond.projecttracker.model.Developer;
import org.amalitechrichmond.projecttracker.repository.DeveloperRepository;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.amalitechrichmond.projecttracker.service.DeveloperService;
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
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    @CacheEvict(value = {"developers", "developerById", "top5Developers"}, allEntries = true)
    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
        Developer developer = DeveloperMapper.toEntity(developerDTO);
        Developer saved = developerRepository.save(developer);
        auditLogService.saveLog("CREATE","Developer",String.valueOf(saved.getId()), saved,"developer");
        return DeveloperMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "developers", key = "#page + '_' + #size + '_' + #sortBy + '_' + #sortDir")
    public List<DeveloperDTO> getAllDevelopers(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Developer> developerPage = developerRepository.findAll(pageable);
        return developerPage.stream()
                .map(DeveloperMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "developerById", key = "#id")
    public DeveloperDTO getDeveloperById(long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with ID: " + id));
        return DeveloperMapper.toDTO(developer);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"developers", "developerById", "top5Developers"}, key = "#developerDTO.id", allEntries = true)
    public DeveloperDTO updateDeveloper(DeveloperDTO developerDTO) {
        Developer developer = developerRepository.findById(developerDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with ID: " + developerDTO.getId()));

        developer.setName(developerDTO.getName());
        developer.setEmail(developerDTO.getEmail());
        developer.setSkills(developerDTO.getSkills());

        Developer updated = developerRepository.save(developer);
        auditLogService.saveLog("CREATE","Developer",String.valueOf(updated.getId()), updated,"developer");
        return DeveloperMapper.toDTO(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"developers", "developerById", "top5Developers"}, key = "#id", allEntries = true)
    public DeveloperDTO deleteDeveloper(long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with ID: " + id));
        developerRepository.delete(developer);
        auditLogService.saveLog("DELETE","Developer",String.valueOf(id), developer,"developer");
        return DeveloperMapper.toDTO(developer);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "top5Developers")
    public List<DeveloperDTO> getTop5Developers() {
        return developerRepository.findTop5ByOrderByTasksSizeDesc()
                .stream()
                .map(DeveloperMapper::toDTO)
                .toList();
    }

}
