package com.api.apartamentos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.apartamentos.models.PropiedadModel;

public interface IPropiedadRepository extends JpaRepository<PropiedadModel, Long> {
    //aqui no lleva codigo

    // buscar propiedades por tipo (casa, departamento, etc.) sin importar mayusculas
    List<PropiedadModel> findByTipoIgnoreCase(String tipo);

    // buscar las propiedades de un propietario (cliente)
    List<PropiedadModel> findByPropietario_Id(Long idPropietario);
}
