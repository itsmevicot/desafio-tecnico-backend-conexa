package com.conexa.backend.scheduling.domain.exceptions;

public interface ExceptionInterface {
    String getTitle();
    String getMessage();
    int getStatusCode();
}
