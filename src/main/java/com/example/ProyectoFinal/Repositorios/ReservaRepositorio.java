
package com.example.ProyectoFinal.Repositorios;
import com.example.ProyectoFinal.Entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservaRepositorio extends JpaRepository<Reserva, Integer> {
    
}
