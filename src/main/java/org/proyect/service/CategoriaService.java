package org.proyect.service;

import java.util.List;

import org.proyect.domain.Categoria;
import org.proyect.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public Categoria save(String nombre) {
        return categoriaRepository.save(new Categoria(nombre));
    }

    public Categoria findById(Long idCategoria) {
        return categoriaRepository.findById(idCategoria).get();
    }

    public void update(Long idCategoria, String nombre) {
        Categoria categoria = categoriaRepository.findById(idCategoria).get();
        categoria.setNombre(nombre);
        categoriaRepository.save(categoria);
    }

    public void delete(Long idCategoria) {
        categoriaRepository.delete(categoriaRepository.getReferenceById(idCategoria));
    }
}
