package com.spring.ApiSystem.notificacoes.email.listener;

import com.spring.ApiSystem.eventos.produtocontratado.ProdutoContrataListener;
import com.spring.ApiSystem.notificacoes.email.dto.Email;
import com.spring.ApiSystem.notificacoes.email.service.EmailService;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UsuarioService;
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

        System.out.println("Notificação: Produto contratado com ID " + produtoContratado.getId() + " foi criado.");
    }
}
