package com.spring.ApiSystem.domain.usuario.events;

import com.spring.ApiSystem.domain.usuario.Usuario;

public interface UsuarioListener {
    void onUsuarioRemovido(Usuario usuario);
}
