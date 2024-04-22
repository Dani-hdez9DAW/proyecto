/*package org.proyect.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class FilmaffinityService {

    private final String apiUrl = "https://api.filmaffinity.com/..."; // URL de la API de Filmaffinity

    private final RestTemplate restTemplate;

    public FilmaffinityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object getInformacionPelicula(String idPelicula) {
        String url = apiUrl + "/peliculas/" + idPelicula;
        return restTemplate.getForObject(url, Object.class);
    }
}*/
