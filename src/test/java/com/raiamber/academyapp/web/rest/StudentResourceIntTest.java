package com.raiamber.academyapp.web.rest;

import com.raiamber.academyapp.Application;
import com.raiamber.academyapp.domain.Student;
import com.raiamber.academyapp.repository.StudentRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StudentResource REST controller.
 *
 * @see StudentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StudentResourceIntTest {


    private static final Long DEFAULT_STUDENT_ID = 1L;
    private static final Long UPDATED_STUDENT_ID = 2L;

    private static final Long DEFAULT_AGE = 1L;
    private static final Long UPDATED_AGE = 2L;
    private static final String DEFAULT_EMAIL_ADD = "AAAAA";
    private static final String UPDATED_EMAIL_ADD = "BBBBB";

    private static final BigDecimal DEFAULT_PHONE_NUMBER = new BigDecimal(1);
    private static final BigDecimal UPDATED_PHONE_NUMBER = new BigDecimal(2);
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private StudentRepository studentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStudentMockMvc;

    private Student student;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentResource studentResource = new StudentResource();
        ReflectionTestUtils.setField(studentResource, "studentRepository", studentRepository);
        this.restStudentMockMvc = MockMvcBuilders.standaloneSetup(studentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        student = new Student();
        student.setStudentId(DEFAULT_STUDENT_ID);
        student.setAge(DEFAULT_AGE);
        student.setEmailAdd(DEFAULT_EMAIL_ADD);
        student.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        student.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student

        restStudentMockMvc.perform(post("/api/students")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(student)))
                .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = students.get(students.size() - 1);
        assertThat(testStudent.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testStudent.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testStudent.getEmailAdd()).isEqualTo(DEFAULT_EMAIL_ADD);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testStudent.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the students
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.intValue())))
                .andExpect(jsonPath("$.[*].emailAdd").value(hasItem(DEFAULT_EMAIL_ADD.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID.intValue()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.intValue()))
            .andExpect(jsonPath("$.emailAdd").value(DEFAULT_EMAIL_ADD.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

		int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        student.setStudentId(UPDATED_STUDENT_ID);
        student.setAge(UPDATED_AGE);
        student.setEmailAdd(UPDATED_EMAIL_ADD);
        student.setPhoneNumber(UPDATED_PHONE_NUMBER);
        student.setName(UPDATED_NAME);

        restStudentMockMvc.perform(put("/api/students")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(student)))
                .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = students.get(students.size() - 1);
        assertThat(testStudent.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testStudent.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testStudent.getEmailAdd()).isEqualTo(UPDATED_EMAIL_ADD);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

		int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Get the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(databaseSizeBeforeDelete - 1);
    }
}
