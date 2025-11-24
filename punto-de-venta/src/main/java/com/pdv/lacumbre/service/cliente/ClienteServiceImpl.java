package com.pdv.lacumbre.service.cliente;


import com.pdv.lacumbre.model.entity.ClienteEntity;
import com.pdv.lacumbre.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> buscarClientes(String termino) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrRfcContainingIgnoreCaseOrTelefonoContainingIgnoreCase(termino, termino, termino);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteEntity> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> obtenerClientesDeudores() {
        return clienteRepository.findBySaldoActualGreaterThan(BigDecimal.ZERO);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteEntity> obtenerPorRfc(String rfc) {
        return clienteRepository.findByRfc(rfc);
    }
    @Override
    @Transactional
    public ClienteEntity crearCliente(ClienteEntity cliente) {
        if (cliente.getRfc() != null && !cliente.getRfc().isEmpty()) {
            if (clienteRepository.existsByRfc(cliente.getRfc())) {
                throw new RuntimeException("El RFC ya está registrado con otro cliente.");
            }
        }
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                throw new RuntimeException("El correo electrónico ya está registrado.");
            }
        }
        if (cliente.getSaldoActual() == null) cliente.setSaldoActual(BigDecimal.ZERO);
        if (cliente.getLimiteCredito() == null) cliente.setLimiteCredito(BigDecimal.ZERO);
        if (cliente.getTieneCredito() == null) cliente.setTieneCredito(false);
        return clienteRepository.save(cliente);
    }
    @Override
    @Transactional
    public ClienteEntity actualizarCliente(Long id, ClienteEntity clienteDetalles) {
        ClienteEntity clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El cliente no existe"));
        if (clienteDetalles.getRfc() != null && !clienteDetalles.getRfc().equals(clienteExistente.getRfc())) {
            if (clienteRepository.existsByRfc(clienteDetalles.getRfc())) {
                throw new RuntimeException("El nuevo RFC ya pertenece a otro cliente.");
            }
        }
        if (clienteDetalles.getEmail() != null && !clienteDetalles.getEmail().equals(clienteExistente.getEmail())) {
            if (clienteRepository.existsByEmail(clienteDetalles.getEmail())) {
                throw new RuntimeException("El nuevo correo ya pertenece a otro cliente.");
            }
        }
        clienteExistente.setNombre(clienteDetalles.getNombre());
        clienteExistente.setRfc(clienteDetalles.getRfc());
        clienteExistente.setTelefono(clienteDetalles.getTelefono());
        clienteExistente.setEmail(clienteDetalles.getEmail());
        clienteExistente.setDireccion(clienteDetalles.getDireccion());
        clienteExistente.setTieneCredito(clienteDetalles.getTieneCredito());
        clienteExistente.setLimiteCredito(clienteDetalles.getLimiteCredito());
        return clienteRepository.save(clienteExistente);
    }
    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.delete(cliente);
    }
}