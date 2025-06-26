package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoDto;
import ar.edu.utn.frc.tup.lc.iv.services.ExternalApiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/distritos")
public class DistritosController {

    private final ExternalApiService externalApiService;

    public DistritosController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping
    public List<DistritoDto> getDistritos(@RequestParam(name = "distrito_nombre", required = false) String nombre) {
        return externalApiService.getDistritos(nombre);
    }
}
