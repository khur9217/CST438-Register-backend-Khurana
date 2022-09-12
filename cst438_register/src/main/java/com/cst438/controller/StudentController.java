package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	
	@Autowired
	StudentRepository studentRepository;
	

	/*
	 * Create new student. Needs to be amended to only allow action for admins later - as of now any user can execute this
	 */
	@PostMapping("/student/new")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO  ) { 

		if (studentDTO.email == null)
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student email cannot be null");
		if (studentDTO.name == null)
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student name cannot be null");
		Student student = studentRepository.findByEmail(studentDTO.email);
		
		if (student == null) {
			student = new Student();
			student.setName(studentDTO.name);
			student.setEmail(studentDTO.email);
			student.setStatus(studentDTO.status);
			student.setStatusCode(studentDTO.statusCode);
			Student savedStudent = studentRepository.save(student);
			StudentDTO result = createStudentDTO(savedStudent);
			return result;
		}

		else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student already registered: "+studentDTO.email);
		}
		
	}
	
	/*
	 * Add a hold to a student given via at a minimum - the student ID. Needs to be improved to require admin authentication.
	 */
	@PostMapping("/student/add-hold")
	@Transactional
	public StudentDTO addHold(  @RequestBody StudentDTO studentDTO  ) {
		Student student = studentRepository.findById(studentDTO.student_id).orElse(null);
		if (student!=null) {
		    student.setStatusCode(1);
		    student.setStatus("Administrative Hold");
		    Student savedStudent = studentRepository.save(student);
			StudentDTO result = createStudentDTO(savedStudent);
			return result;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student doesn't exist in the system: "+studentDTO.email);
		}
	}
	
	/*
	 * Release a hold to a student given via at a minimum - the student ID. Needs to be improved to require admin authentication.
	 */
	@PostMapping("/student/release-hold")
	@Transactional
	public StudentDTO releaseHold(  @RequestBody StudentDTO studentDTO  ) {
		Student student = studentRepository.findById(studentDTO.student_id).orElse(null);
		if (student!=null) {
		    student.setStatusCode(0);
		    student.setStatus(null);
		    Student savedStudent = studentRepository.save(student);
			StudentDTO result = createStudentDTO(savedStudent);
			return result;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student doesn't exist in the system: "+studentDTO.email);
		}
	}
	
	
	
	private StudentDTO createStudentDTO(Student s) {
		StudentDTO studentDTO = new StudentDTO();

		studentDTO.email = s.getEmail();
		studentDTO.name = s.getName();
		studentDTO.status = s.getStatus();
		studentDTO.student_id = s.getStudent_id();
		studentDTO.statusCode = s.getStatusCode();
		return studentDTO;
	}
	
}
