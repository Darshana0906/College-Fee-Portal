package com.coep.FeePortal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    /** BCrypt-hashed password — never stored as plain text */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String course;

    @Column(name = "admission_year", nullable = false)
    private Integer admissionYear;

    /** STUDENT or ADMIN */
    @Column(nullable = false)
    private String role = "STUDENT";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    public Student() {}

    public Student(String name, String email, String password, String course,
                   Integer admissionYear, Category category, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.course = course;
        this.admissionYear = admissionYear;
        this.category = category;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public Integer getAdmissionYear() { return admissionYear; }
    public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
