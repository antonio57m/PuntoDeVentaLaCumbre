package com.pdv.lacumbre.service.cliente;

import com.pdv.lacumbre.model.entity.ClienteEntity;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<ClienteEntity> obtenerTodosLosClientes();
    List<ClienteEntity> buscarClientes(String termino);
    Optional<ClienteEntity> obtenerPorId(Long id);
    List<ClienteEntity> obtenerClientesDeudores();
    Optional<ClienteEntity> obtenerPorRfc(String rfc);
    ClienteEntity crearCliente(ClienteEntity cliente);
    ClienteEntity actualizarCliente(Long id, ClienteEntity clienteDetalles);
    void eliminarCliente(Long id);
}
