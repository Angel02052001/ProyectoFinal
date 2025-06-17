
package com.example.ProyectoFinal.Servicios;
import com.example.ProyectoFinal.Entidades.Reserva;
import com.example.ProyectoFinal.Repositorios.ReservaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepositorio reservaRepositorio;

    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepositorio.save(reserva);
    }

    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepositorio.findAll();
    }

    public void eliminarReserva(int id) {
        reservaRepositorio.deleteById(id);
    }

    public Reserva actualizarReserva(int id, Reserva reservaActualizada) {
        Reserva existente = reservaRepositorio.findById(id).orElseThrow(() ->
                new RuntimeException("Reserva no encontrada con ID: " + id));

        existente.setNombreCompleto(reservaActualizada.getNombreCompleto());
        existente.setCorreoElectronico(reservaActualizada.getCorreoElectronico());
        existente.setTelefono(reservaActualizada.getTelefono());
        existente.setServicio(reservaActualizada.getServicio());
        existente.setBarbero(reservaActualizada.getBarbero());
        existente.setFecha(reservaActualizada.getFecha());
        existente.setHora(reservaActualizada.getHora());

        return reservaRepositorio.save(existente);
    }
}
