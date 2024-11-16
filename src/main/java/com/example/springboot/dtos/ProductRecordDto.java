package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal value, @CreatedDate LocalDateTime createdDate) {
}
