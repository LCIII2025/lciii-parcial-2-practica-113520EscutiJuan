package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import ar.edu.utn.frc.tup.lc.iv.services.ExternalApiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/secciones")
public class SeccionesController {
    private final ExternalApiService externalApiService;

    public SeccionesController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping
    public List<SeccionDto> getSecciones(
            @RequestParam(name = "distrito_id") int distritoId,
            @RequestParam(name = "seccion_id", required = false) Integer seccionId
    ) {
        List<SeccionDto> todas = externalApiService.getSeccionesPorDistrito(distritoId);

        if (seccionId != null) {
            return todas.stream()
                    .filter(s -> s.getId() == seccionId)
                    .collect(Collectors.toList());
        }

        return todas;
    }
}
