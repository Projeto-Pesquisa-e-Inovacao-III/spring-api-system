package com.spring.ApiSystem.shared.dto;

import java.util.List;

public record PaginaCursor<T>(
   List<T> conteudo,
   Long proximoCursor
) {}
