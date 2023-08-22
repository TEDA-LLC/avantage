package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fio, email, tashkilot, lavozim, faoliyat, tel;

    private LocalDateTime siteDate = LocalDateTime.now();

    @ManyToOne
    private Country country;

    @ManyToOne
    private Region region;

    private boolean resident;

    private Integer printerId;

    private LocalDateTime arrivalTime;
}
