package com.raiamber.academyapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;
    
    @Column(name = "age")
    private Long age;
    
    @Column(name = "email_add")
    private String emailAdd;
    
    @Column(name = "phone_number", precision=10, scale=2)
    private BigDecimal phoneNumber;
    
    @Column(name = "name")
    private String name;
    
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "student_course",
               joinColumns = @JoinColumn(name="students_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="courses_id", referencedColumnName="ID"))
    private Set<Course> courses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getAge() {
        return age;
    }
    
    public void setAge(Long age) {
        this.age = age;
    }

    public String getEmailAdd() {
        return emailAdd;
    }
    
    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public BigDecimal getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(BigDecimal phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        if(student.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", studentId='" + studentId + "'" +
            ", age='" + age + "'" +
            ", emailAdd='" + emailAdd + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
