package edu.birzeit.swms.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.birzeit.swms.enums.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(notes = "The database generates bin ID")
    private int id;
    @ApiModelProperty(notes = "defines the location of the bin",required = true)
    @NotNull
    private PointDto location;
    @ApiModelProperty(notes = "defines the status of the bin in terms of fullness",required = true)
    @Enumerated(EnumType.STRING)
    private Status status;
    @ApiModelProperty(notes = "defines the id of the area")
    private int areaId;

}
