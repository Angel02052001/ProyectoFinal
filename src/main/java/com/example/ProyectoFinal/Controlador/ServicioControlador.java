package com.example.ProyectoFinal.Controlador;

import com.example.ProyectoFinal.Entidades.Servicios;
import com.example.ProyectoFinal.Servicios.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/servicios")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://proyectoweb-63512.web.app")

public class ServicioControlador {

    @Autowired
    private ServicioService servicioService;

    @Value("${directorio.imagenes}")
    private String directorioImagenes;

    // ✅ Subir servicio con imagen
    @PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> guardar(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") double precio,
            @RequestParam("imagen") MultipartFile archivo
    ) {
        Map<String, String> response = new HashMap<>();

        try {
            if (archivo == null || archivo.isEmpty()) {
                response.put("mensaje", "Debe seleccionar un archivo");
                return ResponseEntity.ok(response);
            }

            if (nombre == null || nombre.trim().isEmpty()) {
                response.put("mensaje", "El nombre es obligatorio");
                return ResponseEntity.ok(response);
            }

            Servicios servicio = servicioService.guardarServicio(archivo, nombre, descripcion, precio);
            response.put("mensaje", "Servicio guardado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al guardar el servicio");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ Obtener todos los servicios (sin URL de imagen completa)
    @GetMapping("/obtener")
    public ResponseEntity<List<Servicios>> obtenerTodos() {
        try {
            List<Servicios> servicios = servicioService.obtenerTodosLosServicios();
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Obtener todos los servicios con URL completa para imagen
    @GetMapping("/conimagenes")
    public ResponseEntity<List<Servicios>> conImagenes() {
        try {
            List<Servicios> servicios = servicioService.obtenerTodosLosServicios();
            List<Servicios> respuesta = new ArrayList<>();

            for (Servicios s : servicios) {
                String urlImagen = "https://proyectofinal-rbki.onrender.com/uploads/" + s.getNombreArchivo();

                Servicios nuevo = new Servicios(
                        s.getId(),
                        s.getNombre(),
                        s.getDescripcion(),
                        s.getPrecio(),
                        urlImagen, // se envía la URL
                        s.getNombreArchivo(),
                        s.getRutaArchivo()
                );
                respuesta.add(nuevo);
            }

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Eliminar servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarServicio(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            servicioService.eliminarServicio(id);
            response.put("mensaje", "Servicio eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al eliminar el servicio");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ Actualizar servicio
    @PutMapping("/{id}")
    public ResponseEntity<Servicios> actualizarServicio(
            @PathVariable int id,
            @RequestBody Servicios servicioActualizado
    ) {
        try {
            Servicios actualizado = servicioService.actualizarServicio(id, servicioActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Obtener imagen por nombre
    @GetMapping("/imagen/{nombreArchivo:.+}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreArchivo) {
        try {
            Path rutaArchivo = Paths.get(directorioImagenes).resolve(nombreArchivo).normalize().toAbsolutePath();
            Resource recurso = new UrlResource(rutaArchivo.toUri());

            if (recurso.exists() && recurso.isReadable()) {
                String contentType = Files.probeContentType(rutaArchivo);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}