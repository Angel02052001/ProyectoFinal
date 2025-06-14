
package com.example.ProyectoFinal.Servicios;
import com.example.ProyectoFinal.Entidades.Usuario;
import com.example.ProyectoFinal.Repositorios.UsuarioRepositorios;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UsuarioService {
    @Autowired
		private UsuarioRepositorios usuarioRepositorio;

		public List<Usuario> obtenerUsuarios() {
			return usuarioRepositorio.findAll();
		}

	public Usuario getUsuarioById(Integer id){
		return usuarioRepositorio.findById(id).orElse(null);
	}
	
	public Optional<Usuario> autenticarUsuario(String usuario, String contrasena) {
        return usuarioRepositorio.findByUsuarioAndContrasena(usuario, contrasena);
    }
}

