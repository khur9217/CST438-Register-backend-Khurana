# Registration service - students can view their course schedule and add/drop courses 

### REST apis  used by front end 

#### GET /schedule?year={year}&semester={semester}
- query parameters - year such as 2021,   semester such as Spring, Fall 
- result - returned JSON.  see ScheduleDTO class    

#### POST /schedule 
- body contains JSON see EnrollmentDTO

#### DELETE /schedule/{enrollment_id}  
- enrollment_id from a course enrollment  See ScheduleDTO.CourseDTO.id 

### Database Tables
- Course - course number, title, year, semester, hours, location, instructor, start and end dates
- Student - id, name and email of student
- Enrollment - student id, course number, year, semester

### Rest apis used by other services
- tbd 
