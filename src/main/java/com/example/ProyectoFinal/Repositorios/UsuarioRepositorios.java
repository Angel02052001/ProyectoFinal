
package com.example.ProyectoFinal.Repositorios;
import com.example.ProyectoFinal.Entidades.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorios extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuario(String usuario);
    Optional<Usuario> findByUsuarioAndContrasena(String usuario, String contrasena);
}
