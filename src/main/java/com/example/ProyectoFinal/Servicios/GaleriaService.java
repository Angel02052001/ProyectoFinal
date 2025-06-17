package com.example.ProyectoFinal.Servicios;

import com.example.ProyectoFinal.Entidades.Galeria;
import com.example.ProyectoFinal.Repositorios.GaleriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class GaleriaService {

    @Autowired
    private GaleriaRepositorio galeriaRepositorio;

    @Value("${directorio.imagenes}")
    private String directorioImagenes;

    // ✅ Guardar imagen con archivo
    public Galeria guardarGaleria(MultipartFile archivo, String titulo, String nombre) throws IOException {
        // Validar archivo
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        // Validar tipo de archivo
        String contentType = archivo.getContentType();
        if (contentType == null || (!contentType.startsWith("image/"))) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // Generar nombre único para el archivo
        String extension = getFileExtension(archivo.getOriginalFilename());
        String nombreArchivo = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
        
        // Crear ruta del archivo
        Path rutaArchivo = Paths.get(directorioImagenes).resolve(nombreArchivo);
        
        // Crear directorio si no existe
        Files.createDirectories(rutaArchivo.getParent());
        
        // Guardar archivo físico
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

        // Crear entidad
        Galeria galeria = new Galeria();
        galeria.setTitulo(titulo.trim());
        galeria.setNombre(nombre.trim());
        galeria.setNombreArchivo(nombreArchivo);
        galeria.setRutaArchivo(rutaArchivo.toString());

        return galeriaRepositorio.save(galeria);
    }

    // ✅ Obtener todas las imágenes
    public List<Galeria> obtenerTodas() {
        return galeriaRepositorio.findAll();
    }

    // ✅ Obtener por ID
    public Galeria obtenerPorId(int id) {
        return galeriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + id));
    }

    // ✅ Eliminar por ID
    public void eliminarGaleria(int id) {
        Optional<Galeria> galeriaOpt = galeriaRepositorio.findById(id);
        if (galeriaOpt.isPresent()) {
            Galeria galeria = galeriaOpt.get();
            
            // Eliminar archivo físico si existe
            try {
                if (galeria.getRutaArchivo() != null) {
                    Path rutaArchivo = Paths.get(galeria.getRutaArchivo());
                    Files.deleteIfExists(rutaArchivo);
                }
            } catch (IOException e) {
                System.err.println("Error al eliminar archivo físico: " + e.getMessage());
                // Continúa con la eliminación de la base de datos aunque falle el archivo
            }
            
            // Eliminar de la base de datos
            galeriaRepositorio.deleteById(id);
        } else {
            throw new RuntimeException("Imagen no encontrada con ID: " + id);
        }
    }

    // ✅ Actualizar datos (sin archivo)
    public Galeria actualizarGaleria(int id, Galeria galeriaActualizada) {
        Galeria existente = galeriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + id));

        // Actualizar solo los campos de texto
        existente.setTitulo(galeriaActualizada.getTitulo().trim());
        existente.setNombre(galeriaActualizada.getNombre().trim());

        return galeriaRepositorio.save(existente);
    }

    // ✅ NUEVO: Actualizar con archivo
    public Galeria actualizarGaleriaConArchivo(int id, MultipartFile archivo, String titulo, String nombre) throws IOException {
        Galeria existente = galeriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + id));

        // Validar archivo
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        // Validar tipo de archivo
        String contentType = archivo.getContentType();
        if (contentType == null || (!contentType.startsWith("image/"))) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // Eliminar archivo anterior si existe
        try {
            if (existente.getRutaArchivo() != null) {
                Path rutaAnterior = Paths.get(existente.getRutaArchivo());
                Files.deleteIfExists(rutaAnterior);
            }
        } catch (IOException e) {
            System.err.println("Error al eliminar archivo anterior: " + e.getMessage());
        }

        // Generar nuevo nombre de archivo
        String extension = getFileExtension(archivo.getOriginalFilename());
        String nombreArchivo = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
        
        // Crear nueva ruta
        Path rutaArchivo = Paths.get(directorioImagenes).resolve(nombreArchivo);
        
        // Crear directorio si no existe
        Files.createDirectories(rutaArchivo.getParent());
        
        // Guardar nuevo archivo
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

        // Actualizar entidad
        existente.setTitulo(titulo.trim());
        existente.setNombre(nombre.trim());
        existente.setNombreArchivo(nombreArchivo);
        existente.setRutaArchivo(rutaArchivo.toString());

        return galeriaRepositorio.save(existente);
    }

    // ✅ Método auxiliar para obtener extensión de archivo
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return ".jpg"; // extensión por defecto
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ".jpg"; // extensión por defecto
        }
        
        return filename.substring(lastDotIndex);
    }
}
