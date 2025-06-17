package com.example.ProyectoFinal.Controlador;

import com.example.ProyectoFinal.Entidades.Galeria;
import com.example.ProyectoFinal.Servicios.GaleriaService;
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
@RequestMapping("/api/galeria")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://proyectoweb-63512.web.app")

public class GaleriaControlador {

    @Autowired
    private GaleriaService galeriaService;

    @Value("${directorio.imagenes}")
    private String directorioImagenes;

    // ✅ Subir imagen de galería
    @PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> guardar(
            @RequestParam("titulo") String titulo,
            @RequestParam("nombre") String nombre,
            @RequestParam("imagen") MultipartFile archivo
    ) {
        Map<String, String> response = new HashMap<>();

        try {
            if (archivo == null || archivo.isEmpty()) {
                response.put("mensaje", "Debe seleccionar un archivo");
                return ResponseEntity.badRequest().body(response);
            }

            if (titulo == null || titulo.trim().isEmpty()) {
                response.put("mensaje", "El título es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }

            if (nombre == null || nombre.trim().isEmpty()) {
                response.put("mensaje", "El nombre es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }

            Galeria galeria = galeriaService.guardarGaleria(archivo, titulo, nombre);
            response.put("mensaje", "Imagen de galería guardada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al guardar la imagen de galería: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ Obtener todas las imágenes (sin URLs)
    @GetMapping("/obtener")
    public ResponseEntity<List<Galeria>> obtenerTodas() {
        try {
            List<Galeria> lista = galeriaService.obtenerTodas();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Obtener todas las imágenes con URL completa
    @GetMapping("/conimagenes")
    public ResponseEntity<List<Galeria>> obtenerConImagenes() {
        try {
            List<Galeria> galeriaList = galeriaService.obtenerTodas();
            List<Galeria> respuesta = new ArrayList<>();

            for (Galeria g : galeriaList) {
                String urlImagen = "http://localhost:8080/api/galeria/imagen/" + g.getNombreArchivo();

                Galeria nueva = new Galeria(
                        g.getId(),
                        g.getTitulo(),
                        g.getNombre(),
                        urlImagen,
                        g.getNombreArchivo(),
                        g.getRutaArchivo()
                );
                respuesta.add(nueva);
            }

            return ResponseEntity.ok(respuesta);
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

    // ✅ Eliminar imagen de galería
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarGaleria(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            galeriaService.eliminarGaleria(id);
            response.put("mensaje", "Imagen de galería eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al eliminar la imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ Actualizar galería (solo datos, sin archivo)
    @PutMapping("/{id}")
    public ResponseEntity<Galeria> actualizarGaleria(@PathVariable int id, @RequestBody Galeria galeriaActualizada) {
        try {
            Galeria actualizado = galeriaService.actualizarGaleria(id, galeriaActualizada);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ NUEVO: Actualizar galería con archivo
    @PutMapping(value = "/{id}/con-imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> actualizarGaleriaConImagen(
            @PathVariable int id,
            @RequestParam("titulo") String titulo,
            @RequestParam("nombre") String nombre,
            @RequestParam("imagen") MultipartFile archivo
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            if (archivo == null || archivo.isEmpty()) {
                response.put("mensaje", "Debe seleccionar un archivo");
                return ResponseEntity.badRequest().body(response);
            }

            if (titulo == null || titulo.trim().isEmpty()) {
                response.put("mensaje", "El título es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }

            if (nombre == null || nombre.trim().isEmpty()) {
                response.put("mensaje", "El nombre es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }

            Galeria actualizada = galeriaService.actualizarGaleriaConArchivo(id, archivo, titulo, nombre);
            response.put("mensaje", "Imagen actualizada correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("mensaje", "Imagen no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); 
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al actualizar la imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ NUEVO: Obtener imagen por ID
    @GetMapping("/{id}")
    public ResponseEntity<Galeria> obtenerPorId(@PathVariable int id) {
        try {
            Galeria galeria = galeriaService.obtenerPorId(id);
            return ResponseEntity.ok(galeria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}