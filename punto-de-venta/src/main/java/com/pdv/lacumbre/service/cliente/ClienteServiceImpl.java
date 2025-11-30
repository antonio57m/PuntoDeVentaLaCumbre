package com.pdv.lacumbre.service.cliente;


import com.pdv.lacumbre.model.entity.ClienteEntity;
import com.pdv.lacumbre.model.entity.PagoCreditoEntity;
import com.pdv.lacumbre.model.entity.UsuarioEntity;

import com.pdv.lacumbre.model.entity.VentaEntity;
import com.pdv.lacumbre.model.enums.EstadoVenta;
import com.pdv.lacumbre.model.enums.MetodoPagoAbono;
import com.pdv.lacumbre.repository.ClienteRepository;
import com.pdv.lacumbre.repository.PagoCreditoRepository;
import com.pdv.lacumbre.repository.UsuarioRepository;
import com.pdv.lacumbre.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PagoCreditoRepository pagoCreditoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private VentaRepository ventaRepository;
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
    @Override
    @Transactional
    public void registrarAbono(Long idCliente, BigDecimal monto, MetodoPagoAbono metodo, String usernameCajero) {

        // 1. Validaciones
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto del abono debe ser mayor a 0.");
        }

        ClienteEntity cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        UsuarioEntity cajero = usuarioRepository.findByNombreUsuario(usernameCajero)
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));

        // 2. Actualizar Saldo Global (La deuda baja)
        BigDecimal nuevoSaldo = cliente.getSaldoActual().subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            nuevoSaldo = BigDecimal.ZERO;
        }
        cliente.setSaldoActual(nuevoSaldo);
        clienteRepository.save(cliente);

        // 3. Guardar Registro del Pago (Historial)
        PagoCreditoEntity pago = PagoCreditoEntity.builder()
                .cliente(cliente)
                .usuario(cajero)
                .monto(monto)
                .metodoPagoAbono(metodo)
                .build();
        pagoCreditoRepository.save(pago);

        // 4. NUEVO: Actualizar Estado de Ventas Pendientes (Matar deuda vieja)
        // Buscamos todas las ventas de este cliente
        List<VentaEntity> ventasPendientes = ventaRepository.findByClienteOrderByFechaDesc(cliente).stream()
                .filter(v -> v.getEstadoVenta() == EstadoVenta.PENDIENTE)
                .sorted(Comparator.comparing(VentaEntity::getFecha)) // Ordenamos: Las más viejas primero
                .collect(Collectors.toList());

        BigDecimal dineroDisponible = monto; // Usamos el dinero del abono para ir pagando tickets

        for (VentaEntity venta : ventasPendientes) {
            if (dineroDisponible.compareTo(BigDecimal.ZERO) <= 0) {
                break; // Se acabó el dinero del abono
            }

            // Si el dinero alcanza para pagar TODA esta venta
            if (dineroDisponible.compareTo(venta.getTotal()) >= 0) {
                venta.setEstadoVenta(EstadoVenta.PAGADA);
                ventaRepository.save(venta);

                // Restamos lo que costó esta venta al dinero disponible
                dineroDisponible = dineroDisponible.subtract(venta.getTotal());
            } else {
                // Si el dinero NO alcanza para liquidar esta venta completa:
                // Opción A: Dejarla pendiente (Implementación actual simple)
                // Opción B: Crear lógica de "pagos parciales" (Muy complejo, requiere cambiar BD)
                // Por ahora, simplemente dejamos de pagar tickets porque el saldo no cubre el siguiente completo.
                break;
            }
        }
    }
}