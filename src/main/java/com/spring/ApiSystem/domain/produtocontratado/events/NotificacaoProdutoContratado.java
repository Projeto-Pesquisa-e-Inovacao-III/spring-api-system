package com.spring.ApiSystem.shared.infrastructure.email.listener;

import com.spring.ApiSystem.domain.produtocontratado.events.ProdutoContrataListener;
import com.spring.ApiSystem.shared.infrastructure.email.dto.Email;
import com.spring.ApiSystem.shared.infrastructure.email.service.EmailService;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoProdutoContratado implements ProdutoContrataListener {

    private final EmailService emailService;

    public NotificacaoProdutoContratado(EmailService emailService, UsuarioService usuarioService) {
        this.emailService = emailService;
    }

    @Override
    public void onProdutoContratadoCreated(ProdutoContratado produtoContratado) {

        Usuario usuario =produtoContratado.getAluno();
        String produtoNome= produtoContratado.getProdutoExibicao().getTitulo();

        Email email = new Email(
                usuario.getEmail(),
                "Confirmação de Contratação de Produto",
                String.format("Olá %s,<br><br>Seu produto %s foi contratado com sucesso!<br><br>Obrigado por escolher nossos serviços.",
                        usuario.getNome(),
                        produtoNome)
        );
        emailService.enviarEmail(email);
    }
}
