package com.pdv.lacumbre.service.categoria;

import com.pdv.lacumbre.model.entity.CategoriaEntity;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<CategoriaEntity> buscarTodasLasCategorias();
    List<CategoriaEntity> filtrarCategorias(String coincidencia);
    Optional<CategoriaEntity> buscarPorId(Long id);
    CategoriaEntity crearCategoria(CategoriaEntity categoria);
    CategoriaEntity actualizarCategoria(Long id, CategoriaEntity categoria);
    void eliminarCategoria(Long id);
}
