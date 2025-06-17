
package com.example.ProyectoFinal.Servicios;
import com.example.ProyectoFinal.Entidades.Servicios;
import com.example.ProyectoFinal.Repositorios.ServicioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
@Service
public class ServicioService {
    @Autowired
    private ServicioRepositorio servicioRepositorio;
    @Value("${directorio.imagenes}")
    private String directorioImagenes;
    // ✅ Guardar servicio con imagen
    public Servicios guardarServicio(MultipartFile archivo, String nombre, String descripcion, double precio) throws IOException {
        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = Paths.get(directorioImagenes).resolve(nombreArchivo);
        Files.createDirectories(rutaArchivo.getParent()); // crea el directorio si no existe
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        Servicios servicio = new Servicios();
        servicio.setNombre(nombre);
        servicio.setDescripcion(descripcion);
        servicio.setPrecio(precio);
        servicio.setNombreArchivo(nombreArchivo);
        servicio.setRutaArchivo(rutaArchivo.toString());
        return servicioRepositorio.save(servicio);
    }
    // ✅ Obtener todos
    public List<Servicios> obtenerTodosLosServicios() {
        return servicioRepositorio.findAll();
    }
    // ✅ Eliminar por ID
    public void eliminarServicio(int id) {
        servicioRepositorio.deleteById(id);
    }
    // ✅ Actualizar (sin actualizar imagen)
    public Servicios actualizarServicio(int id, Servicios servicioActualizado) {
        Servicios existente = servicioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
        existente.setNombre(servicioActualizado.getNombre());
        existente.setDescripcion(servicioActualizado.getDescripcion());
        existente.setPrecio(servicioActualizado.getPrecio());
        // Solo actualizar imagen si se proporciona nueva ruta o archivo
        if (servicioActualizado.getNombreArchivo() != null) {
            existente.setNombreArchivo(servicioActualizado.getNombreArchivo());
            existente.setRutaArchivo(servicioActualizado.getRutaArchivo());
        }
        return servicioRepositorio.save(existente);
    }
}