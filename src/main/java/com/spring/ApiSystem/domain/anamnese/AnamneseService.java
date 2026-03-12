package com.spring.ApiSystem.domain.anamnese;

import org.springframework.stereotype.Service;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastroAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.mapper.AnamneseMapper;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;

@Service
public class AnamneseService {
    private final AnamneseRepository anamneseRepository;
    private final AnamneseMapper anamneseMapper;
    // private final JpaUserDetailsService jpaUserDetailsService;

    public AnamneseService(AnamneseRepository anamneseRepository, AnamneseMapper anamneseMapper) {
        this.anamneseRepository = anamneseRepository;
        this.anamneseMapper = anamneseMapper;
        // this.jpaUserDetailsService = jpaUserDetailsService;
    }

    public Anamnese cadastrarAnamnese(ReqCadastroAnamneseDto req) {
        Anamnese anamnese = new Anamnese();

        // Usuario usuario = jpaUserDetailsService.getCurrentUser();

        DtoToEntity(anamnese, req);
        // anamnese.setUsuario(usuario);

        anamneseRepository.save(anamnese);
        return anamneseMapper.toEntityFromResponse(anamnese);
    }

    public void DtoToEntity(Anamnese anamnese, ReqCadastroAnamneseDto dto) {
        anamnese.setAltura(dto.altura());
        anamnese.setPeso(dto.peso());
        anamnese.setObjectivoPrincipal(dto.objectivoPrincipal());
        anamnese.setRotina(dto.rotina());
        anamnese.setCondicoes(dto.condicoes());
        anamnese.setNivelDeAtividade(dto.nivelDeAtividade());
        anamnese.setObservacaoSaude(dto.observacaoSaude());
    }
}
