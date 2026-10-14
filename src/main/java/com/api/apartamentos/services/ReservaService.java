package com.api.apartamentos.services;

import java.math.BigDecimal;
import java.time.Duration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.apartamentos.models.ClienteModel;
import com.api.apartamentos.models.ReservaModel;
import com.api.apartamentos.models.UnidadRentableModel;
import com.api.apartamentos.repositories.IClienteRepository;
import com.api.apartamentos.repositories.IReservaRepository;
import com.api.apartamentos.repositories.IUnidadRentableRepository;

@Service
public class ReservaService extends BaseCrudService<ReservaModel> {
    private final IReservaRepository reservaRepository;
    private final IUnidadRentableRepository unidadRepository;
    private final IClienteRepository clienteRepository;

    public ReservaService(IReservaRepository repository, IUnidadRentableRepository unidadRepository, IClienteRepository clienteRepository) {
        super(repository);
        reservaRepository = repository;
        this.unidadRepository = unidadRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ReservaModel save(ReservaModel reserva) {
        if (reserva.getUnidad() == null || reserva.getUnidad().getIdUnidad() == null) throw new IllegalArgumentException("La unidad es obligatoria");
        if (reserva.getCliente() == null || reserva.getCliente().getId() == null) throw new IllegalArgumentException("El cliente es obligatorio");

        // desde la API solo llega el id, por eso se buscan la unidad y el cliente completos en la BD
        // (sin esto el precio de la unidad llega en null y no se puede calcular el importe)
        UnidadRentableModel unidad = unidadRepository.findById(reserva.getUnidad().getIdUnidad())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe"));
        ClienteModel cliente = clienteRepository.findById(reserva.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));
        reserva.setUnidad(unidad);
        reserva.setCliente(cliente);

        if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null || !reserva.getFechaSalida().isAfter(reserva.getFechaEntrada())) throw new IllegalArgumentException("El rango de fechas no es válido");
        if (!"CANCELADA".equals(reserva.getEstado()) && reservaRepository.existeSolapamiento(reserva.getUnidad().getIdUnidad(), reserva.getIdReservacion(), reserva.getFechaEntrada(), reserva.getFechaSalida())) throw new IllegalArgumentException("La unidad ya está reservada en ese rango");
        if (reserva.getImporteTotal() == null && reserva.getUnidad().getPrecioNoche() != null) {
            long noches = Math.max(1, Duration.between(reserva.getFechaEntrada(), reserva.getFechaSalida()).toDays());
            reserva.setImporteTotal(reserva.getUnidad().getPrecioNoche().multiply(BigDecimal.valueOf(noches)));
        }
        return reservaRepository.save(reserva);
    }
}
