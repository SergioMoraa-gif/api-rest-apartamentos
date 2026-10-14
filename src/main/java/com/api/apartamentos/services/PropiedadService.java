package com.api.apartamentos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.apartamentos.models.PropiedadModel;
import com.api.apartamentos.repositories.IPropiedadRepository;

@Service
public class PropiedadService extends BaseCrudService<PropiedadModel> {

    private final IPropiedadRepository propiedadRepository;

    public PropiedadService(IPropiedadRepository propiedadRepository) {
        super(propiedadRepository);
        this.propiedadRepository = propiedadRepository;
    }

    public List<PropiedadModel> getAllPropiedades() {
        return findAll();
    }

    public Optional<PropiedadModel> getPropiedadById(Long id) {
        return findById(id);
    }

    public PropiedadModel savePropiedad(PropiedadModel propiedad) {
        return save(propiedad);
    } 
    
    public void deletePropiedad(Long id) {
        deleteById(id);
    }

    // ACTIVIDAD: Recuperar propiedades por tipo
    public List<PropiedadModel> getPropiedadesByTipo(String tipo) {
        return propiedadRepository.findByTipoIgnoreCase(tipo);
    }

    // ACTIVIDAD: Recuperar todas las propiedades de un propietario
    public List<PropiedadModel> getPropiedadesByPropietario(Long idPropietario) {
        return propiedadRepository.findByPropietario_Id(idPropietario);
    }
}
