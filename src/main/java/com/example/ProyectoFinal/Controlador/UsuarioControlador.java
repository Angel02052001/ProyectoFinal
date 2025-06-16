
package com.example.ProyectoFinal.Controlador;

import com.example.ProyectoFinal.Entidades.Usuario;
import com.example.ProyectoFinal.Servicios.UsuarioService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://proyectoweb-63512.web.app")
public class UsuarioControlador {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario usuario) {
		Optional<Usuario> usuarioOpt = usuarioService.autenticarUsuario(usuario.getUsuario(), usuario.getContrasena());

		if (usuarioOpt.isPresent()) {
		return ResponseEntity.ok(usuarioOpt.get());
		} else {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
		}
	}
}
