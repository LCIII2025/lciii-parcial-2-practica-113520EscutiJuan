package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.*;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadosResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import ar.edu.utn.frc.tup.lc.iv.services.ExternalApiService;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resultados")

public class ResultadosController {
    private final ExternalApiService externalApiService;

    public ResultadosController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping
    public ResultadosResponseDto getResultados(
            @RequestParam("distrito_id") int distritoId,
            @RequestParam("seccion_id") int seccionId
    ) {
        DistritoDto distrito = externalApiService.getDistritoById(distritoId);
        SeccionDto seccion = externalApiService.getSeccionesPorDistrito(distritoId).stream()
                .filter(s -> s.getId() == seccionId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        List<Map<String, Object>> raw = externalApiService.getResultados(distritoId, seccionId);

        int totalVotos = raw.stream()
                .mapToInt(r -> (int) r.get("votos"))
                .sum();

        List<ResultadoDto> resultados = raw.stream()
                .sorted((a, b) -> Integer.compare((int) b.get("votos"), (int) a.get("votos")))
                .map(r -> {
                    ResultadoDto dto = new ResultadoDto();
                    dto.setNombre((String) r.get("nombre"));
                    dto.setVotos((int) r.get("votos"));
                    dto.setPorcentaje(totalVotos == 0 ? 0.0 :
                            Math.round(((double) dto.getVotos() / totalVotos) * 10000.0) / 10000.0);
                    return dto;
                })
                .collect(Collectors.toList());

        for (int i = 0; i < resultados.size(); i++) {
            resultados.get(i).setOrden(i + 1);
        }

        ResultadosResponseDto response = new ResultadosResponseDto();
        response.setDistrito(distrito.getNombre());
        response.setSeccion(seccion.getNombre());
        response.setResultados(resultados);

        return response;
    }
}
