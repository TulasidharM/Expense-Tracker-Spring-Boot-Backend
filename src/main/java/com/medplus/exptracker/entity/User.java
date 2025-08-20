package com.medplus.exptracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    
    @Column(name = "role_id")
    private Integer roleId;
    
    @Column(name = "manager_id")
    private Integer managerId;
}
