package com.spring.ApiSystem.shared.service;

import org.springframework.stereotype.Service;

import java.lang.reflect.RecordComponent;
import java.util.Collection;
import java.util.Map;

@Service
public class UtilsService {
    public boolean hasAnyFilter(Object maybeRecord) {
        if (maybeRecord == null) return false;

        Class<?> type = maybeRecord.getClass();
        if (!type.isRecord()) {
            throw new IllegalArgumentException("Objeto informado nao e um record: " + type.getName());
        }

        try {
            for (RecordComponent component : type.getRecordComponents()) {
                Object value = component.getAccessor().invoke(maybeRecord);

                switch (value) {
                    case null -> {
                        continue;
                    }
                    case String s -> {
                        if (!s.isBlank()) return true;
                        continue;
                    }
                    case Collection<?> c -> {
                        if (!c.isEmpty()) return true;
                        continue;
                    }
                    case Map<?, ?> m -> {
                        if (!m.isEmpty()) return true;
                        continue;
                    }
                    default -> {
                    }
                }

                // Qualquer outro tipo nao-nulo conta como filtro
                return true;
            }
            return false;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Erro ao inspecionar record para filtros", e);
        }
    }
}
