package com.spring.ApiSystem.domain.produtocontratado.events;

import com.spring.ApiSystem.shared.infrastructure.email.dto.Email;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.shared.infrastructure.email.service.EmailService;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoProdutoContratado {

    private final EmailService emailService;

    public NotificacaoProdutoContratado(EmailService emailService) {
        this.emailService = emailService;
    }

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
