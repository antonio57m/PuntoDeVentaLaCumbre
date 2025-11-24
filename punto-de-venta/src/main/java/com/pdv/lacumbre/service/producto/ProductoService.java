package com.pdv.lacumbre.service.producto;

import com.pdv.lacumbre.model.entity.CategoriaEntity;
import com.pdv.lacumbre.model.entity.ProductoEntity;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<ProductoEntity> obtenerTodosLosProductos();
    Optional<ProductoEntity> obtenerPorId(Long id);
    Optional<ProductoEntity> buscarProductoPorCodigo(String codigo);
    List<ProductoEntity> buscarProductosPorNombre(String termino);
    List<ProductoEntity> buscarPorCategoria(CategoriaEntity categoria);
    List<ProductoEntity> encontrarProductosConBajoStock();
    ProductoEntity crearProducto(ProductoEntity producto);
    ProductoEntity actualizarProducto(Long id, ProductoEntity productoDetalles);
    void eliminarProducto(Long id);
    boolean verificarStock(Long idProducto, double cantidadRequerida);
    Optional<ProductoEntity> obtenerPorSku(String sku);
}
