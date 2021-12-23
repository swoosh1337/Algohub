package edu.wpi.cs.dss.serverless.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum HttpStatus {
    SUCCESS(200),
    BAD_REQUEST(400);

    private final Integer value;
}
