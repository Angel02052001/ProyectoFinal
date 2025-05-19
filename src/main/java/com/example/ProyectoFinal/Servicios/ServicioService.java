
package com.example.ProyectoFinal.Servicios;
import com.example.ProyectoFinal.Entidades.Servicios;
import com.example.ProyectoFinal.Repositorios.ServicioRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioService {
    @Autowired
    private ServicioRepositorio servicioRepositorio;

    public Servicios guardarServicio(Servicios servicio) {
        return servicioRepositorio.save(servicio);
    }
    public List<Servicios> obtenerTodosLosServicios() {
    return servicioRepositorio.findAll();
    }
    public void eliminarServicio(int id) {
    servicioRepositorio.deleteById(id);
}
    public Servicios actualizarServicio(int id, Servicios servicioActualizado) {
    Servicios existente = servicioRepositorio.findById(id).orElseThrow(() -> 
        new RuntimeException("Servicio no encontrado con ID: " + id));
    
    existente.setNombre(servicioActualizado.getNombre());
    existente.setDescripcion(servicioActualizado.getDescripcion());
    existente.setPrecio(servicioActualizado.getPrecio());
    existente.setImagen(servicioActualizado.getImagen());

    return servicioRepositorio.save(existente);
}


}
