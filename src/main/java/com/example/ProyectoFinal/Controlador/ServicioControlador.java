
package com.example.ProyectoFinal.Controlador;
import com.example.ProyectoFinal.Entidades.Servicios;
import com.example.ProyectoFinal.Servicios.ServicioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "*")
public class ServicioControlador {
    @Autowired
    private ServicioService servicioService;

    @PostMapping("/guardar")
    public Servicios guardar(@RequestBody Servicios servicio) {
        return servicioService.guardarServicio(servicio);
    }
    @GetMapping("/obtener")
    public List<Servicios> obtenerTodos() {
    return servicioService.obtenerTodosLosServicios();
    }
    @DeleteMapping("/{id}")
    public void eliminarServicio(@PathVariable int id) {
    servicioService.eliminarServicio(id);
   }
    @PutMapping("/{id}")
    public Servicios actualizarServicio(@PathVariable int id, @RequestBody Servicios servicioActualizado) {
    return servicioService.actualizarServicio(id, servicioActualizado);
}

}
