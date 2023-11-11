package de.ohnes.util;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Setter;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@SuppressWarnings("unused")
public class TestResult {

    private UUID InstanceID = UUID.randomUUID();
    private int jobs;
    private int machines;
    private long milliseconds;
    private String algorithm;

}