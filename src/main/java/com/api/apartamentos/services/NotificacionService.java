package com.api.apartamentos.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.api.apartamentos.models.NotificacionModel;
import com.api.apartamentos.repositories.IClienteRepository;
import com.api.apartamentos.repositories.INotificacionRepository;

@Service
public class NotificacionService extends BaseCrudService<NotificacionModel> {
    private final INotificacionRepository notificacionRepository;
    private final IClienteRepository clienteRepository;

    public NotificacionService(INotificacionRepository repository, IClienteRepository clienteRepository) {
        super(repository);
        notificacionRepository = repository;
        this.clienteRepository = clienteRepository;
    }

    public List<NotificacionModel> findByDestinatario(Long id) { return notificacionRepository.findByDestinatario_Id(id); }

    @Override
    public NotificacionModel save(NotificacionModel notificacion) {
        if (notificacion.getRemitente() == null || notificacion.getRemitente().getId() == null) throw new IllegalArgumentException("El remitente es obligatorio");
        if (notificacion.getDestinatario() == null || notificacion.getDestinatario().getId() == null) throw new IllegalArgumentException("El destinatario es obligatorio");
        // se buscan remitente y destinatario completos en la BD
        notificacion.setRemitente(clienteRepository.findById(notificacion.getRemitente().getId())
                .orElseThrow(() -> new IllegalArgumentException("El remitente no existe")));
        notificacion.setDestinatario(clienteRepository.findById(notificacion.getDestinatario().getId())
                .orElseThrow(() -> new IllegalArgumentException("El destinatario no existe")));
        return super.save(notificacion);
    }
}
