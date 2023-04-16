package mkaciuba.inggreencode.atmservice.dto;

import lombok.RequiredArgsConstructor;

public record ServiceTask(int region, RequestType requestType, int atmId) {

    @RequiredArgsConstructor
    public enum RequestType implements Comparable<RequestType> {
        FAILURE_RESTART,
        PRIORITY,
        SIGNAL_LOW,
        STANDARD
    }
}
