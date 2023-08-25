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
    @JoinColumn(name = "davlat_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "obl_id")
    private Region region;

    private boolean resident;

    private Integer printerId;

    private LocalDateTime reg_date;

    private LocalDateTime arrivalTime;

    private boolean photo = true;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = true)
    private Attachment attachment;
}
