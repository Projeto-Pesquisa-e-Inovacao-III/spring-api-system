package com.spring.ApiSystem.domain.usuario.events;


import com.spring.ApiSystem.domain.usuario.Usuario;

import java.util.List;

public class UsuarioEventPublisher {

    private final List<UsuarioListener> listeners;

    public UsuarioEventPublisher(List<UsuarioListener> listeners) {
        this.listeners = listeners;
    }

    public void publishUsuarioRemovido(Usuario usuario) {
        listeners.forEach(listener -> listener.onUsuarioRemovido(usuario));
    }
}
