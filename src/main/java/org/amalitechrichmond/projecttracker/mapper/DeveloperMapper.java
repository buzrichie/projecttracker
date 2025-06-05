package org.amalitechrichmond.projecttracker.mapper;

import org.amalitechrichmond.projecttracker.DTO.DeveloperDTO;
import org.amalitechrichmond.projecttracker.model.Developer;

import java.util.ArrayList;

public class DeveloperMapper {

    public static DeveloperDTO toDTO(Developer dev) {
        DeveloperDTO dto = new DeveloperDTO();
        dto.setId(dev.getId());
        dto.setName(dev.getName());
        dto.setEmail(dev.getEmail());
        dto.setSkills(new ArrayList<>(dev.getSkills()));
        return dto;
    }

    public static Developer toEntity(DeveloperDTO dto) {
        Developer dev = new Developer();
        dev.setId(dto.getId());
        dev.setName(dto.getName());
        dev.setEmail(dto.getEmail());
        dev.setSkills(new ArrayList<>(dto.getSkills()));
        return dev;
    }
}

