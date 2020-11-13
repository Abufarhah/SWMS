package edu.birzeit.swms.services.implementations;

import edu.birzeit.swms.dtos.BinDto;
import edu.birzeit.swms.enums.Status;
import edu.birzeit.swms.exceptions.ResourceNotFoundException;
import edu.birzeit.swms.mappers.BinMapper;
import edu.birzeit.swms.models.Area;
import edu.birzeit.swms.models.Bin;
import edu.birzeit.swms.repositories.AreaRepository;
import edu.birzeit.swms.repositories.BinRepository;
import edu.birzeit.swms.services.AreaService;
import edu.birzeit.swms.services.BinService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log
@Service
public class BinServiceImpl implements BinService {

    @Autowired
    BinRepository binRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    BinMapper binMapper;

    @Override
    public List<BinDto> getBins() {
        List<BinDto> binDtoList = new ArrayList<>();
        binRepository.findAll().forEach(bin -> {
            BinDto binDto = binMapper.binToDto(bin);
            binDto.setAreaId(bin.getArea().getId());
            binDtoList.add(binDto);
        });
        return binDtoList;
    }

    @Override
    public BinDto getBin(int id) {
        Bin bin = binRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bin", "id", id));
        BinDto binDto = binMapper.binToDto(bin);
        binDto.setAreaId(bin.getArea().getId());
        return binDto;
    }

    @Override
    public BinDto addBin(BinDto binDto) {
        Bin bin = binMapper.dtoToBin(binDto);
        if (binDto.getAreaId() != 0) {
            bin.setArea(areaRepository.findById(binDto.getAreaId()).orElseThrow(
                    () -> new ResourceNotFoundException("Area", "id", binDto.getAreaId())));
        }
        Bin savedBin = binRepository.save(bin);
        BinDto savedBinDto = binMapper.binToDto(savedBin);
        return savedBinDto;
    }

    @Override
    public BinDto updateBin(BinDto binDto, int id) {
        Bin bin = binRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Bin", "id", id));
        bin.setLatitude(binDto.getLatitude());
        bin.setLongitude(binDto.getLongitude());
        if (binDto.getAreaId() != 0) {
            bin.setArea(areaRepository.findById(binDto.getAreaId()).orElseThrow(
                    () -> new ResourceNotFoundException("Area", "id", binDto.getAreaId())));
        }
        Bin savedBin = binRepository.save(bin);
        BinDto savedBinDto = binMapper.binToDto(savedBin);
        return savedBinDto;
    }

    @Override
    public void deleteBin(int id) {
        if (binRepository.existsById(id)) {
            binRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Bin", "id", id);
        }
    }

    @Override
    public BinDto updateBinStatus(Status status, int id) {
        Bin bin = binRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bin", "id", id));
        bin.setStatus(status);
        Bin savedBin = binRepository.save(bin);
        BinDto savedBinDto = binMapper.binToDto(savedBin);
        return savedBinDto;
    }

    @Override
    public List<BinDto> findByStatus(Status status) {
        List<BinDto> binDtoList = new ArrayList<>();
        binRepository.findByStatus(status).forEach(bin -> binDtoList.add(binMapper.binToDto(bin)));
        return binDtoList;
    }


}
