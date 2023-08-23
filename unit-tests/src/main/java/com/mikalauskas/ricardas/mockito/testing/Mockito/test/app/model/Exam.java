package com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Exam {
    private Long id;
    private String name;
    private List<String> questions;
}
