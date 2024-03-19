package de.ohnes.util;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@SuppressWarnings("unused")
public class TestResult {

    private UUID InstanceID = UUID.randomUUID();
    private int jobs;
    private int machines;
    @Getter
    private long milliseconds;
    private String algorithm;
    private double epsilon;
    private double q;
    @Getter
    private double objectiveValue;

    @JsonIgnore
    public String toString() {
        return String.format("%s, %d, %d, %d, %s, %f, %f, %f", InstanceID, jobs, machines, milliseconds, algorithm, epsilon, q, objectiveValue);
    }

}