
package com.example.ProyectoFinal.Controlador;

import com.example.ProyectoFinal.Entidades.Usuario;
import com.example.ProyectoFinal.Servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")

public class UsuarioControlador {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/guardar")
    public Usuario guardar(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }
}
