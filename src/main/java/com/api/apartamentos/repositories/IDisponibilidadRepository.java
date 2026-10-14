package com.api.apartamentos.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.apartamentos.models.DisponibilidadModel;

public interface IDisponibilidadRepository extends JpaRepository<DisponibilidadModel, Long> {
    // @EntityGraph carga las relaciones LAZY en la misma consulta para poder convertirlas a JSON
    @Override
    @EntityGraph(attributePaths = "unidad")
    List<DisponibilidadModel> findAll();

    @Override
    @EntityGraph(attributePaths = "unidad")
    Optional<DisponibilidadModel> findById(Long id);

    @EntityGraph(attributePaths = "unidad")
    List<DisponibilidadModel> findByUnidad_IdUnidad(Long idUnidad);
}
