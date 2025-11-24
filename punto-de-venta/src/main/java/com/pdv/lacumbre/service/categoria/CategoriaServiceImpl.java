package com.pdv.lacumbre.service.categoria;
import com.pdv.lacumbre.model.entity.CategoriaEntity;
import com.pdv.lacumbre.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaEntity> buscarTodasLasCategorias() {
        return categoriaRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaEntity> filtrarCategorias(String coincidencia) {
        return categoriaRepository.findByNombreContainingIgnoreCase(coincidencia);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaEntity> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    @Override
    @Transactional
    public CategoriaEntity crearCategoria(CategoriaEntity categoria) {
        if(categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }
    @Override
    @Transactional
    public CategoriaEntity actualizarCategoria(Long id, CategoriaEntity categoriaDetalles) {
        CategoriaEntity categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoria no existe"));
        if (!categoriaExistente.getNombre().equalsIgnoreCase(categoriaDetalles.getNombre()) &&
                categoriaRepository.existsByNombre(categoriaDetalles.getNombre())) {
            throw new RuntimeException("No se puede renombrar porque ya existe otra categoría con ese nombre");
        }
        categoriaExistente.setNombre(categoriaDetalles.getNombre());
        categoriaExistente.setDescripcion(categoriaDetalles.getDescripcion());
        return categoriaRepository.save(categoriaExistente);
    }
    @Override
    @Transactional
    public void eliminarCategoria(Long id) {
        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoria no existe"));
        categoriaRepository.delete(categoria);
    }
}