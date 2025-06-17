
package com.example.ProyectoFinal.Servicios;
import com.example.ProyectoFinal.Entidades.Carrusel;
import com.example.ProyectoFinal.Repositorios.CarruselRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service

public class CarruselService {
    
   @Autowired
    private CarruselRepositorio carruselRepositorio;

    @Value("${directorio.imagenes}")
    private String directorioImagenes;

    public Carrusel guardarCarrusel(MultipartFile archivo, String descripcion) throws IOException {
        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = Paths.get(directorioImagenes).resolve(nombreArchivo);
        Files.createDirectories(rutaArchivo.getParent());  // Crea directorios si no existen
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        System.out.println(nombreArchivo);

        Carrusel carrusel = new Carrusel();
        carrusel.setNombreArchivo(nombreArchivo);
        carrusel.setDescripcion(descripcion);
        carrusel.setRutaArchivo(rutaArchivo.toString());
        
        return carruselRepositorio.save(carrusel);
    }

    public List<Carrusel> obtenerTodos() {
        return carruselRepositorio.findAll();
    }

    public void eliminarCarrusel(int id) {
        carruselRepositorio.deleteById(id);
    }

    public Carrusel actualizarCarrusel(int id, Carrusel carruselActualizado) {
        Carrusel existente = carruselRepositorio.findById(id).orElseThrow(() ->
                new RuntimeException("Imagen de carrusel no encontrada con ID: " + id));

        existente.setDescripcion(carruselActualizado.getDescripcion());
        existente.setNombreArchivo(carruselActualizado.getNombreArchivo());
        existente.setRutaArchivo(carruselActualizado.getRutaArchivo());

        return carruselRepositorio.save(existente);
    }
}