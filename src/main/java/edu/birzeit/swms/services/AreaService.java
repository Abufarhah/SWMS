package edu.birzeit.swms.services;

import edu.birzeit.swms.dtos.AreaDto;

import java.util.List;

public interface AreaService {

    List<AreaDto> getAreas();

    AreaDto getArea(int id);

    AreaDto addArea(AreaDto areaDto);

    AreaDto updateArea(AreaDto areaDto, int id);

    void deleteArea(int id);

    void assignBin(int areaId, int binId);

    void unAssignBin(int areaId, int binId);

}
