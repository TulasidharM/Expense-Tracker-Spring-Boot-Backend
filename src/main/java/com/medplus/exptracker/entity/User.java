package com.medplus.exptracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message="Username cannot be empty")
    private String username;
    
    @NotBlank(message="password cannot be empty")
    private String password;
    
    @Column(name = "role_id")
    private Integer roleId;
    
    @Column(name = "manager_id")
    private Integer managerId;
}
