
package com.example.ProyectoFinal.Controlador;
import com.example.ProyectoFinal.Entidades.Carrusel;
import com.example.ProyectoFinal.Servicios.CarruselService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/carrusel")
//@CrossOrigin(origins = "http://localhost:4200") local
@CrossOrigin(origins = "https://proyectoweb-63512.web.app")
public class CarruselControlador {
     @Autowired
    private CarruselService carruselService;
     @Value("${directorio.imagenes}")
    private String directorioImagenes;
    // ✅ Subida de imagen y descripción (form-data)
    @PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> guardar(
            @RequestParam("imagen") MultipartFile archivo, // Cambiado de "archivo" a "imagen"
            @RequestParam("descripcion") String descripcion
    ) {
        try {
            Map<String, String> response = new HashMap<>();
            // Validaciones
            if (archivo == null || archivo.isEmpty()) {
                //return ResponseEntity.badRequest().body("Debe seleccionar un archivo");
                response.put("mensaje", "Debe seleccionar un archivo");
                return ResponseEntity.ok(response);
            }
            
            if (descripcion == null || descripcion.trim().isEmpty()) {
                //return ResponseEntity.badRequest().body("La descripción es obligatoria");
                 response.put("mensaje", "La descripción es obligatoria");
                return ResponseEntity.ok(response);
            }
            
            Carrusel carrusel = carruselService.guardarCarrusel(archivo, descripcion);
            //return ResponseEntity.ok("Archivo guardado correctamente con nombre: " + archivo.getOriginalFilename());
             
    response.put("mensaje", "Archivo guardado correctamente con nombre: Corte15.jpeg");
    return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<Map<String, String>>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
                    //.body((T) ("Error al guardar el archivo: " + e.getMessage()));
        }
    }
    
    // ✅ Obtener todas las imágenes
    @GetMapping("/obtener")
    public ResponseEntity<List<Carrusel>> obtenerTodos() {
        try {
            List<Carrusel> carruseles = carruselService.obtenerTodos();
            return ResponseEntity.ok(carruseles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ✅ Eliminar una imagen por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarCarrusel(@PathVariable int id) {
    Map<String, String> response = new HashMap<>();
    try {
        carruselService.eliminarCarrusel(id);
        response.put("mensaje", "Carrusel eliminado correctamente");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        response.put("mensaje", "Error al eliminar el carrusel");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
    @GetMapping("/conimagenes")
public ResponseEntity<List<Carrusel>> conImagenes() {
    try {
        List<Carrusel> carruseles = carruselService.obtenerTodos();
        List<Carrusel> respuesta = new ArrayList<>();

        for (Carrusel carrusel : carruseles) {
            // Construir URL completa para acceder a la imagen
            String urlImagen = "http://localhost:8080/api/carrusel/imagen/" + carrusel.getNombreArchivo();
            
            Carrusel nuevoCarrusel = new Carrusel(
                    carrusel.getId(),
                    carrusel.getDescripcion(),
                    urlImagen  // Usar la URL completa aquí
            );
            respuesta.add(nuevoCarrusel);
        }

        return ResponseEntity.ok(respuesta);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
    
    // ✅ Actualizar imagen y/o descripción por ID
    @PutMapping("/{id}")
public ResponseEntity<Carrusel> actualizarCarrusel(
        @PathVariable int id,
        @RequestBody Carrusel carruselActualizado) {
    try {
        Carrusel actualizado = carruselService.actualizarCarrusel(id, carruselActualizado);
        return ResponseEntity.ok(actualizado);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
@GetMapping("/imagen/{nombreArchivo:.+}")
public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreArchivo) {
    try {
        Path rutaArchivo = Paths.get(directorioImagenes).resolve(nombreArchivo).normalize().toAbsolutePath();
        Resource recurso = new UrlResource(rutaArchivo.toUri());
        
        if (recurso.exists() && recurso.isReadable()) {
            // Detectar el tipo de contenido
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