package edu.birzeit.swms.services.implementations;

import edu.birzeit.swms.dtos.CitizenDto;
import edu.birzeit.swms.dtos.EmployeeDto;
import edu.birzeit.swms.enums.UserRole;
import edu.birzeit.swms.exceptions.ResourceNotFoundException;
import edu.birzeit.swms.mappers.CitizenMapper;
import edu.birzeit.swms.models.Citizen;
import edu.birzeit.swms.models.Employee;
import edu.birzeit.swms.repositories.CitizenRepository;
import edu.birzeit.swms.repositories.ReportRepository;
import edu.birzeit.swms.services.CitizenService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log
@Service
public class CitizenServiceImpl implements CitizenService {

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    CitizenMapper citizenMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<CitizenDto> getCitizens() {
        List<CitizenDto> citizenDtoList = new ArrayList<>();
        citizenRepository.findAll().forEach(citizen -> citizenDtoList.add(citizenMapper.citizenToDto(citizen)));
        return citizenDtoList;
    }

    @Override
    public CitizenDto getCitizen(int id) {
        Citizen citizen = citizenRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Citizen", "id", id));
        CitizenDto citizenDto = citizenMapper.citizenToDto(citizen);
        return citizenDto;
    }

    @Override
    public CitizenDto addCitizen(CitizenDto citizenDto) {
        Citizen citizen = citizenMapper.dtoToCitizen(citizenDto);
        citizen.setRole(UserRole.CITIZEN);
        citizen.setPassword(passwordEncoder.encode("swms" + citizen.getUsername()));
        citizen.setEnabled(true);
        Citizen savedCitizen = citizenRepository.save(citizen);
        CitizenDto savedCitizenDto = citizenMapper.citizenToDto(savedCitizen);
        return savedCitizenDto;
    }

    @Override
    public CitizenDto updateCitizen(CitizenDto citizenDto, int id) {
        Citizen citizen = citizenRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Citizen", "id", id));
        citizen.setFirstName(citizenDto.getFirstName());
        citizen.setLastName(citizenDto.getLastName());
        citizen.setPhone(citizenDto.getPhone());
        citizen.setAddress(citizenDto.getAddress());
        citizen.setRole(UserRole.CITIZEN);
        citizen.setEnabled(true);
        Citizen savedCitizen = citizenRepository.save(citizen);
        CitizenDto savedCitizenDto = citizenMapper.citizenToDto(savedCitizen);
        return savedCitizenDto;
    }

    @Override
    public void deleteCitizen(int id) {
        Citizen citizen = citizenRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Citizen", "id", id));
        citizen.getReportList().forEach(report -> reportRepository.deleteById(report.getId()));
        citizenRepository.deleteById(id);
    }

}
