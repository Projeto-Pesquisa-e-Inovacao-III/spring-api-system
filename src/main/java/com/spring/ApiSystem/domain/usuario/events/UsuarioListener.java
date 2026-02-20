package com.spring.ApiSystem.eventos.usuario;

import com.spring.ApiSystem.domain.usuario.Usuario;

public interface UsuarioListener {
    void onUsuarioRemovido(Usuario usuario);
}
