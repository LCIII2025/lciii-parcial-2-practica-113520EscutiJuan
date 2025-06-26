package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExternalApiService {
    private final RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8081"; // la API del contenedor

    @Autowired
    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<DistritoDto> getDistritos(String filtroNombre) {
        DistritoDto[] distritos = restTemplate.getForObject(baseUrl + "/distritos", DistritoDto[].class);

        if (filtroNombre == null || filtroNombre.isBlank()) {
            return Arrays.asList(distritos);
        }

        String filtro = filtroNombre.toLowerCase();

        return Arrays.stream(distritos)
                .filter(d -> d.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
    }
    public List<CargoDto> getCargosPorDistrito(int distritoId) {
        // Supongamos que la API externa tiene un endpoint como este:
        String url = baseUrl + "/cargos?distrito_id=" + distritoId;
        CargoDto[] cargos = restTemplate.getForObject(url, CargoDto[].class);
        return Arrays.asList(cargos);
    }

    public DistritoDto getDistritoById(int distritoId) {
        String url = baseUrl + "/distritos";
        DistritoDto[] distritos = restTemplate.getForObject(url, DistritoDto[].class);
        return Arrays.stream(distritos)
                .filter(d -> d.getId() == distritoId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado"));
    }

    public List<SeccionDto> getSeccionesPorDistrito(int distritoId) {
        String url = baseUrl + "/secciones?distrito_id=" + distritoId;
        SeccionDto[] secciones = restTemplate.getForObject(url, SeccionDto[].class);
        return Arrays.asList(secciones);
    }
    public List<Map<String, Object>> getResultados(int distritoId, int seccionId) {
        String url = baseUrl + "/resultados?distrito_id=" + distritoId + "&seccion_id=" + seccionId;

        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        return response.getBody();
    }
}
