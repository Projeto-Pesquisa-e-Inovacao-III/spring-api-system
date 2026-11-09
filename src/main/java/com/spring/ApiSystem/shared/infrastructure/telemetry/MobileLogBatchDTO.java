package com.spring.ApiSystem.shared.infrastructure.telemetry;

import java.util.List;

public record MobileLogBatchDTO(
        String clientTimestamp,
        String os,
        String osVersion,
        String appVersion,
        String appBuild,
        List<MobileLogEntryDTO> entries
) {
}
