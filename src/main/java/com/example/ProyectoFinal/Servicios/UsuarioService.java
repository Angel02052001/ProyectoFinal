
package com.example.ProyectoFinal.Servicios;
import com.example.ProyectoFinal.Entidades.Usuario;
import com.example.ProyectoFinal.Repositorios.UsuarioRepositorios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UsuarioService {
    @Autowired
    private UsuarioRepositorios usuarioRepositorio;

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }
}
