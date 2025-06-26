package ar.edu.utn.frc.tup.lc.iv.controllers;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargosResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoDto;
import ar.edu.utn.frc.tup.lc.iv.services.ExternalApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cargos")
public class CargosController {
    private final ExternalApiService externalApiService;

    public CargosController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping
    public CargosResponseDto getCargos(@RequestParam(name = "distrito_id") int distritoId) {
        DistritoDto distrito = externalApiService.getDistritoById(distritoId);
        List<CargoDto> cargos = externalApiService.getCargosPorDistrito(distritoId);

        CargosResponseDto response = new CargosResponseDto();
        response.setDistrito(distrito);
        response.setCargos(cargos);

        return response;
    }
}
