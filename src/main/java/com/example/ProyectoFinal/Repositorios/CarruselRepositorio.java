
package com.example.ProyectoFinal.Repositorios;

import com.example.ProyectoFinal.Entidades.Carrusel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CarruselRepositorio extends JpaRepository<Carrusel, Integer> {
}
