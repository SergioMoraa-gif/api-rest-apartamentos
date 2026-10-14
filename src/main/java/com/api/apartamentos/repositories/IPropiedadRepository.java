package com.api.apartamentos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.apartamentos.models.PropiedadModel;

public interface IPropiedadRepository extends JpaRepository<PropiedadModel, Long> {

    // @EntityGraph carga al propietario en la misma consulta.
    // El propietario es LAZY y open-in-view esta en false, sin esto
    // al convertir a JSON truena con error 500 (igual que en IUnidadRentableRepository)
    @Override
    @EntityGraph(attributePaths = "propietario")
    List<PropiedadModel> findAll();

    @Override
    @EntityGraph(attributePaths = "propietario")
    Optional<PropiedadModel> findById(Long id);

    // buscar propiedades por tipo (casa, departamento, etc.) sin importar mayusculas
    @EntityGraph(attributePaths = "propietario")
    List<PropiedadModel> findByTipoIgnoreCase(String tipo);

    // buscar las propiedades de un propietario (cliente)
    @EntityGraph(attributePaths = "propietario")
    List<PropiedadModel> findByPropietario_Id(Long idPropietario);
}
