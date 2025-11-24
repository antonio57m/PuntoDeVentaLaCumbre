package com.pdv.lacumbre.service.producto;
import com.pdv.lacumbre.model.entity.CategoriaEntity;
import com.pdv.lacumbre.model.entity.ProductoEntity;
import com.pdv.lacumbre.model.enums.UnidadMedida;
import com.pdv.lacumbre.repository.CategoriaRepository;
import com.pdv.lacumbre.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoEntity> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoEntity> buscarProductoPorCodigo(String codigo) {
        return productoRepository.findBySkuOrCodigoProveedor(codigo, codigo);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> buscarProductosPorNombre(String termino) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(termino);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> buscarPorCategoria(CategoriaEntity categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> encontrarProductosConBajoStock() {
        return productoRepository.encontrarProductosConBajoStock();
    }
    @Override
    @Transactional
    public ProductoEntity crearProducto(ProductoEntity producto) {
        if (producto.getSku() != null && !producto.getSku().isEmpty()) {
            if (productoRepository.existsBySku(producto.getSku())) {
                throw new RuntimeException("El SKU " + producto.getSku() + " ya existe.");
            }
        }
        if (producto.getCodigoProveedor() != null && !producto.getCodigoProveedor().isEmpty()) {
            if (productoRepository.existsByCodigoProveedor(producto.getCodigoProveedor())) {
                throw new RuntimeException("El código de barras " + producto.getCodigoProveedor() + " ya está asignado a otro producto.");
            }
        }
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            CategoriaEntity categoriaReal = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("La categoría especificada no existe"));
            producto.setCategoria(categoriaReal);
        } else {
            producto.setCategoria(null);
        }
        if (producto.getStock() == null) producto.setStock(BigDecimal.ZERO);
        if (producto.getPrecioCompra() == null) producto.setPrecioCompra(BigDecimal.ZERO);
        if (producto.getUnidadMedida() == null) producto.setUnidadMedida(UnidadMedida.PIEZA);
        if (producto.getStockMinimo() == null) producto.setStockMinimo(BigDecimal.valueOf(5));
        if (producto.getActivo() == null) producto.setActivo(true);
        return productoRepository.save(producto);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoEntity> obtenerPorSku(String sku) {
        return productoRepository.findBySku(sku);
    }
    @Override
    @Transactional
    public ProductoEntity actualizarProducto(Long id, ProductoEntity productoDetalles) {
        ProductoEntity productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        if (!productoExistente.getSku().equals(productoDetalles.getSku())
                && productoRepository.existsBySku(productoDetalles.getSku())) {
            throw new RuntimeException("No se puede actualizar: El nuevo SKU ya existe en otro producto.");
        }
        if (productoDetalles.getCodigoProveedor() != null
                && !productoDetalles.getCodigoProveedor().equals(productoExistente.getCodigoProveedor())
                && productoRepository.existsByCodigoProveedor(productoDetalles.getCodigoProveedor())) {
            throw new RuntimeException("No se puede actualizar: El código de barras ya existe en otro producto.");
        }
        productoExistente.setSku(productoDetalles.getSku());
        productoExistente.setCodigoProveedor(productoDetalles.getCodigoProveedor());
        productoExistente.setNombre(productoDetalles.getNombre());
        productoExistente.setDescripcion(productoDetalles.getDescripcion());
        productoExistente.setPrecioCompra(productoDetalles.getPrecioCompra());
        productoExistente.setPrecioVenta(productoDetalles.getPrecioVenta());
        productoExistente.setStock(productoDetalles.getStock()); // Ajuste manual de stock
        productoExistente.setStockMinimo(productoDetalles.getStockMinimo());
        productoExistente.setUnidadMedida(productoDetalles.getUnidadMedida());
        productoExistente.setCategoria(productoDetalles.getCategoria());
        productoExistente.setActivo(productoDetalles.getActivo());
        return productoRepository.save(productoExistente);
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarStock(Long idProducto, double cantidadRequerida) {
        ProductoEntity producto = productoRepository.findById(idProducto)
                .orElse(null);
        if (producto == null || !producto.getActivo()) {
            return false;
        }
        BigDecimal requerida = BigDecimal.valueOf(cantidadRequerida);
        return producto.getStock().compareTo(requerida) >= 0;
    }
}