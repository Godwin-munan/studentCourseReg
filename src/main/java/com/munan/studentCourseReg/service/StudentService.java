package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.constants.RegExConstant;
import com.munan.studentCourseReg.dto.CourseListDto;
import com.munan.studentCourseReg.dto.StudentDto;
import com.munan.studentCourseReg.dto.Student_courseDto;
import com.munan.studentCourseReg.dto.UpdateStudentDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.*;
import com.munan.studentCourseReg.repository.*;
import com.munan.studentCourseReg.util.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class StudentService{

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final GenderRepository genderRepository;

    private final LevelRepository levelRepository;

    private final DepartmentRepository departmentRepository;

    private final EmailService emailService;

    private final AppUserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    //Email validation method
    public boolean emailValidator(String email)
    {
         Pattern pattern;
         Matcher matcher;

        pattern = Pattern.compile(RegExConstant.EMAIL_PATTERN);

        matcher = pattern.matcher(email);

        return matcher.matches();

    }

    private Optional<Student> findStudentById(Long id) throws NotFoundException {
        Optional<Student> findStudent = studentRepository.findById(id);

        if(findStudent.isEmpty())
        {
            throw new NotFoundException("Student with id "+id+" not found");
        }

        return findStudent;
    }

    //METHOD TO RETURN NEW STUDENT
    private Student newStudent(StudentDto studentDto) throws AlreadyExistException, NotFoundException {
        Optional<Student> existStudent = studentRepository.findByFirstName(studentDto.getFirstName());

        if(existStudent.isPresent())
        {
            throw new AlreadyExistException("Student with name "+existStudent.get().getFirstName()+" "+existStudent.get().getLastName()+" Already exists");
        }

        Optional<Department> findDept = departmentRepository.findByName(studentDto.getDepartment());

        if(findDept.isEmpty())
        {
            throw new NotFoundException(studentDto.getDepartment()+" department does not exist");
        }

        Optional<Level> findLevel = levelRepository.findByLevelNumber(studentDto.getLevel());


        if(findLevel.isEmpty())
        {
            throw new NotFoundException(studentDto.getLevel()+" level does not exist");
        }

        Optional<Gender> findGender = genderRepository.findByType(studentDto.getGender());

        if(findGender.isEmpty())
        {
            throw new NotFoundException(studentDto.getGender()+" gender does not exist");
        }

        boolean email = emailValidator(studentDto.getEmail());

        if(!email)
        {
            throw new NotFoundException(studentDto.getEmail()+" is invalid");
        }

        Student savedStudent = new Student();
        savedStudent.setAge(studentDto.getAge());
        savedStudent.setDepartment(findDept.get());
        savedStudent.setEmail(studentDto.getEmail());
        savedStudent.setGender(findGender.get());
        savedStudent.setLevel(findLevel.get());
        savedStudent.setMatric_number(studentDto.getMatric_number());
        savedStudent.setFirstName(studentDto.getFirstName());
        savedStudent.setLastName(studentDto.getLastName());

        return savedStudent;
    }

    //CREATE NEW SINGLE STUDENT WITH NO COURSE(S)
    public ResponseEntity<HttpResponse<?>> addStudent(StudentDto studentDto) throws AlreadyExistException, NotFoundException, MessagingException {

        Optional<AppUser> findUser = userRepository.findByUsername(studentDto.getEmail());

        if (findUser.isEmpty()) {
            Optional<Role> roles = roleRepository.findByName("ROLE_USER");

            if (roles.isEmpty()) {
                roles =  Optional.ofNullable(roleRepository.save(new Role(null, "ROLE_USER")));
            }
            roles.ifPresent(role -> {
                AppUser newUser = new AppUser();
                newUser.setUsername(studentDto.getEmail());
                newUser.setPassword(passwordEncoder.encode(studentDto.getPassword()));
                newUser.setRoles(Collections.singleton(role));

                userRepository.save(newUser);

            });
        }
            Student savedStudent = newStudent(studentDto);

            emailService.sendEmailToStudent(savedStudent.getFirstName(), savedStudent.getEmail());

            return ResponseEntity.ok(
                    new HttpResponse<>(HttpStatus.OK.value(), "Successful", studentRepository.save(savedStudent))
            );

    }


    //CREATE NEW STUDENT WITH COURSE LIST
    public ResponseEntity<HttpResponse<?>> add_student_course(Student_courseDto student_courseDto) throws NotFoundException, AlreadyExistException, MessagingException {

        StudentDto _student = new StudentDto();
        _student.setAge(student_courseDto.getAge());
        _student.setDepartment(student_courseDto.getDepartment());
        _student.setEmail(student_courseDto.getEmail());
        _student.setGender(student_courseDto.getGender());
        _student.setLevel(student_courseDto.getLevel());
        _student.setMatric_number(student_courseDto.getMatric_number());
        _student.setFirstName(student_courseDto.getFirstName());
        _student.setLastName(student_courseDto.getLastName());

        Student newStudent = newStudent(_student);

        Set<Course> newCourses = student_courseDto.getCourses().stream().map(course ->{

            Optional<Course> findCourse = Optional.ofNullable(courseRepository.findByCode(course.getCode()).orElse(null));

            Optional<Department> findDept = Optional.ofNullable(departmentRepository.findByName(course.getDepartment()).orElse(null));

            if(findCourse.get() == null)
            {
                newStudent.setCourses(null);
            }


            Course newCourse = new Course();
            newCourse.setName(course.getName());
            newCourse.setCode(course.getCode());
            newCourse.setCredit_unit(course.getCredit_unit());
            newCourse.setDepartment(findDept.get());

            return newCourse;
        }).collect(Collectors.toSet());

       Set<Course> updatedCourse= newCourses.stream().filter(x-> (!x.getName().equalsIgnoreCase(null))).collect(Collectors.toSet());

        newStudent.setCourses(updatedCourse);

        emailService.sendEmailToStudent(newStudent.getFirstName(),newStudent.getEmail());

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful",
                        studentRepository.save(newStudent))

//                        .save(newStudent)
        );
    }

    //ADD EXISTING OR NEW COURSE LIST TO EXISTING STUDENT USING STUDENT_ID
    public ResponseEntity<HttpResponse<?>> add_courseList(Long studentId, CourseListDto courseList) throws NotFoundException {

        Optional<Student> updateStudent = studentRepository.findById(studentId).map(student -> {

            Set<Course> courseSet =courseList.getCourses().stream().map(courseRequest ->{

                // Course exist
                Optional<Course> _course = courseRepository.findByCode(courseRequest.getCode());


                    if(_course.isEmpty())
                    {
                        // add and create new course
                        Optional<Department> dpt = Optional.ofNullable(departmentRepository.findByName(courseRequest.getDepartment()).orElse(null));

                        Course newCourse = new Course();
                        newCourse.setName(courseRequest.getName());
                        newCourse.setCode(courseRequest.getCode());
                        newCourse.setCredit_unit(courseRequest.getCredit_unit());
                        newCourse.setDepartment(dpt.get());

                       Course updateCourse = courseRepository.save(newCourse);
                        student.addCourse(updateCourse);
                        return updateCourse;
                    }

                    student.addCourse(_course.get());
                    studentRepository.save(student);
                    return _course.get();

            }).collect(Collectors.toSet());


            return student;
            });


        if(updateStudent.isEmpty())
        {
            throw new NotFoundException("Student with id " +studentId+" not found");
        }
        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", updateStudent)
        );
    }

    public ResponseEntity<HttpResponse<?>> getStudents() {

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful",
 studentRepository.findAll())
        );
    }

    //GET ALL COURSES BELONGING TO A SINGLE STUDENT
    public ResponseEntity<HttpResponse<?>> StudentsByCourse(Long course_id) throws NotFoundException {

        Optional<Course> findStudent = courseRepository.findById(course_id);

        if(findStudent.isEmpty())
        {
            throw new NotFoundException("Course with id "+course_id+" not found");
        }

        List<Student> students = studentRepository.findStudentsByCourses_id(course_id);

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", students)
        );
    }

    //DELETE SINGLE STUDENT
    public ResponseEntity<HttpResponse<?>> deleteStudent(Long student_id) throws NotFoundException {

        Optional<Student> findStudent = findStudentById(student_id);


        findStudent.get().setDeleted(true);
        studentRepository.deleteById(findStudent.get().getId());

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", findStudent.get().getEmail()+" has been deleted")
        );
    }

    public ResponseEntity<HttpResponse<?>> deleteCourseByStudent(Long student_id, Long course_id) throws NotFoundException {

        Optional<Student> student = findStudentById(student_id);

        Optional<Course> course = courseRepository.findById(course_id);

        if(course.isEmpty())
        {
            throw new NotFoundException("Course with id "+course.get().getId()+" not found");
        }

        String courseName = course.get().getName();

        student.get().removeCourse(course.get().getId());
        studentRepository.save(student.get());

        return ResponseEntity.ok(
                new HttpResponse<>
                        (
                                HttpStatus.OK.value(),
                                "Successful",
                                courseName+" has been deleted from "+student.get().getEmail()+" list of courses")
        );
    }

    public ResponseEntity<HttpResponse<?>> updateStudent(UpdateStudentDto studentDto) throws NotFoundException, AlreadyExistException {


        if(studentDto.getId() == null){
            studentDto.setId(0L);
        }

        Optional<Student> _student = studentRepository.findById(studentDto.getId());

        if(_student.isPresent()) {
            studentDto.setId(_student.get().getId());

            //UPDATE FOR DEPT
            if(studentDto.getDepartment() != null)
            {
                Optional<Department> updateDept = departmentRepository.findByName(studentDto.getDepartment());

                if (updateDept.isEmpty()) {
                    throw new NotFoundException(studentDto.getDepartment() + " department does not exist");
                }
                _student.get().setDepartment(updateDept.get());
            }


            //UPDATE FOR LEVEL
            if(studentDto.getLevel() != null)
            {
                Optional<Level> updatedLevel = levelRepository.findByLevelNumber(studentDto.getLevel());


                if (updatedLevel.isEmpty()) {
                    throw new NotFoundException(studentDto.getLevel() + " level does not exist");
                }
                _student.get().setLevel(updatedLevel.get());
            }

            System.out.println("LEVEL IS: "+studentDto.getLevel());

            //UPDATE FOR GENDER
            if(studentDto.getGender() != null)
            {
                Optional<Gender> updatedGender = genderRepository.findByType(studentDto.getGender());

                if (updatedGender.isEmpty()) {
                    throw new NotFoundException(studentDto.getGender() + " gender does not exist");
                }
                _student.get().setGender(updatedGender.get());
            }

            //UPDATE FOR EMAIL
            if(studentDto.getEmail() != null)
            {
                boolean email = emailValidator(studentDto.getEmail());

                if (!email) {
                    throw new NotFoundException(studentDto.getEmail() + " is invalid");
                }

                _student.get().setEmail(studentDto.getEmail());
            }

            //UPDATE FOR AGE
            if(studentDto.getAge() != null)
            {
                _student.get().setAge(studentDto.getAge());
            }

            //UPDATE FOR FIRST NAME
            if(studentDto.getFirstName() != null)
            {
                _student.get().setFirstName(studentDto.getFirstName());
            }

            //UPDATE FOR LAST NAME
            if(studentDto.getLastName() != null)
            {
                _student.get().setLastName(studentDto.getLastName());
            }

            //UPDATE FOR MATRICULATION NUMBER
            if(studentDto.getMatric_number() != null)
            {
                _student.get().setMatric_number(studentDto.getMatric_number());
            }

            studentRepository.save(_student.get());

        }else {
            StudentDto newStudent = new StudentDto();

            newStudent.setFirstName(studentDto.getFirstName());
            newStudent.setLastName(studentDto.getLastName());
            newStudent.setEmail(studentDto.getEmail());
            newStudent.setLevel(studentDto.getLevel());
            newStudent.setAge(studentDto.getAge());
            newStudent.setDepartment(studentDto.getDepartment());
            newStudent.setGender(studentDto.getGender());
            newStudent.setMatric_number(studentDto.getMatric_number());

            Student saveStudent = newStudent(newStudent);

            studentDto.setId(saveStudent.getId());

            studentRepository.save(saveStudent);
        }



        return  ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", studentDto)
        );
    }
}