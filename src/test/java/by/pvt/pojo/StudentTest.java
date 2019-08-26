package by.pvt.pojo;

import by.pvt.util.DBUnitTestBase;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class StudentTest extends DBUnitTestBase {

    public StudentTest(String name) {
        super(name);
    }

    static Student createTestData(int index) {
        Student student = new Student();
        student.setDateOfBirth(new Date());
        student.setFirstName("firstName" + index);
        student.setLastName("lastName" + index);
        student.setGender('f');
        return student;
    }

    @Test
    public void testSaveOrUpdateStudent() {
        int index = 13;
        Transaction tx = null;
        try {
            tx = testSession.beginTransaction();
            Student student0 = createTestData(index);
            Course course = CourseTest.createTestData(5);
            student0.setCourse(course);
            testSession.save(course);
            testSession.saveOrUpdate(student0);
            tx.commit();

            tx = testSession.beginTransaction();
            List<Student> studentList = testSession.createQuery("from student").list();
            tx.commit();

            Assert.assertEquals(6, studentList.size());
            assertTrue(studentList.stream().anyMatch(student -> student.equals(student0)));
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        }
    }

    @Test
    public void testGetStudent() {
        Long idStudentToGet = 2L;
        Transaction tx = null;
        try {
            tx = testSession.beginTransaction();
            Student loadedStudent = testSession.get(Student.class, idStudentToGet);
            tx.commit();

            Assert.assertEquals(Long.valueOf(2), loadedStudent.getId());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        }
    }

    @Test
    public void testDeleteStudent() {
        Long idStudentToDelete = 3L;
        Transaction tx = null;
        try {
            tx = testSession.beginTransaction();
            Student loadedStudent = testSession.get(Student.class, idStudentToDelete);
            StudentInfo loadedStudentInfo = testSession.load(StudentInfo.class, loadedStudent.getId());
            testSession.delete(loadedStudentInfo);
            testSession.delete(loadedStudent);
            tx.commit();

            tx = testSession.beginTransaction();
            List<Student> studentList = testSession.createQuery("from student").list();
            tx.commit();
            assertEquals(4, studentList.size());
            assertTrue(studentList.stream().noneMatch(student -> student.getId() == idStudentToDelete));
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        }
    }

    protected String getResourceName() {
        return "student_TestDataSet.xml";
    }
}
