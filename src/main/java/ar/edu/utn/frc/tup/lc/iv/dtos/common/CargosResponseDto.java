package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import lombok.Data;

import java.util.List;

@Data
public class CargosResponseDto {
    private DistritoDto distrito;
    private List<CargoDto> cargos;
}
