
package com.example.ProyectoFinal.Controlador;
import com.example.ProyectoFinal.Entidades.Reserva;
import com.example.ProyectoFinal.Servicios.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://proyectoweb-63512.web.app")

public class ReservaControlador {
    @Autowired
    private ReservaService reservaService;

    @PostMapping("/guardar")
    public Reserva guardar(@RequestBody Reserva reserva) {
        return reservaService.guardarReserva(reserva);
    }

    @GetMapping("/obtener")
    public List<Reserva> obtenerTodas() {
        return reservaService.obtenerTodasLasReservas();
    }

    @DeleteMapping("/{id}")
    public void eliminarReserva(@PathVariable int id) {
        reservaService.eliminarReserva(id);
    }

    @PutMapping("/{id}")
    public Reserva actualizarReserva(@PathVariable int id, @RequestBody Reserva reservaActualizada) {
        return reservaService.actualizarReserva(id, reservaActualizada);
    }
}
