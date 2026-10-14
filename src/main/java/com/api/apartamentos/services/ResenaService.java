package com.api.apartamentos.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.api.apartamentos.models.ResenaModel;
import com.api.apartamentos.repositories.IClienteRepository;
import com.api.apartamentos.repositories.IResenaRepository;
import com.api.apartamentos.repositories.IReservaRepository;

@Service
public class ResenaService extends BaseCrudService<ResenaModel> {
    private final IResenaRepository resenaRepository;
    private final IReservaRepository reservaRepository;
    private final IClienteRepository clienteRepository;

    public ResenaService(IResenaRepository repository, IReservaRepository reservaRepository, IClienteRepository clienteRepository) {
        super(repository);
        resenaRepository = repository;
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<ResenaModel> findByReserva(Long id) { return resenaRepository.findByReserva_IdReservacion(id); }

    @Override
    public ResenaModel save(ResenaModel resena) {
        if (resena.getReserva() == null || resena.getReserva().getIdReservacion() == null) throw new IllegalArgumentException("La reserva es obligatoria");
        if (resena.getCliente() == null || resena.getCliente().getId() == null) throw new IllegalArgumentException("El cliente es obligatorio");
        // las calificaciones van del 1 al 5
        if (resena.getCalificacionGeneral() < 1 || resena.getCalificacionGeneral() > 5
                || resena.getCalificacionComunicacion() < 1 || resena.getCalificacionComunicacion() > 5) {
            throw new IllegalArgumentException("Las calificaciones deben ser del 1 al 5");
        }
        // desde la API solo llegan los ids, se buscan la reserva y el cliente completos en la BD
        resena.setReserva(reservaRepository.findById(resena.getReserva().getIdReservacion())
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe")));
        resena.setCliente(clienteRepository.findById(resena.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe")));
        return super.save(resena);
    }
}
