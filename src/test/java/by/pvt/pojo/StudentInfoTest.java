package by.pvt.pojo;

import by.pvt.util.DBUnitTestBase;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class StudentInfoTest extends DBUnitTestBase {

    private static StudentInfo createTestData(int index) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setCity("City" + index);
        studentInfo.setAddressLine("addressLine" + index);
        studentInfo.setStreet("Street" + index);
        studentInfo.setCourseInfo(index);
        return studentInfo;
    }

    public StudentInfoTest(String name) {
        super(name);
    }

    @Override
    protected String getResourceName() {
        return "studentInfo_TestDataSet.xml";
    }

    @Test
    public void testSaveOrUpdate() {
        Transaction tx = null;
        try {
            tx = testSession.beginTransaction();
            Student student = StudentTest.createTestData(17);
            Course course = CourseTest.createTestData(4);
            student.setCourse(course);
            testSession.save(course);
            testSession.save(student);
            tx.commit();

            tx = testSession.beginTransaction();
            StudentInfo studentInfo = createTestData(17);
            studentInfo.setStudent(student);
            testSession.saveOrUpdate(studentInfo);
            tx.commit();

            tx = testSession.beginTransaction();
            List<StudentInfo> studentInfoList = testSession.createQuery("from studentInfo ").list();
            tx.commit();

            Assert.assertEquals(6, studentInfoList.size());
            assertTrue(studentInfoList.stream().anyMatch(studentInfo1 -> studentInfo1.equals(studentInfo)));
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        }
    }

    @Test
    public void testGet() {
        Transaction tx = null;
        Long idStudentInfoToGet = 5L;
        try {
            tx = testSession.beginTransaction();
            StudentInfo loadedStudentInfo = testSession.get(StudentInfo.class, idStudentInfoToGet);
            tx.commit();

            assertEquals(idStudentInfoToGet, loadedStudentInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        }
    }

    @Test
    public void testDelete() {
        Transaction tx = null;
        Long idStudentInfoToDelete = 4L;
        try {
            tx = testSession.beginTransaction();
            StudentInfo loadedStudentInfo = testSession.load(StudentInfo.class, idStudentInfoToDelete);
            testSession.delete(loadedStudentInfo);
            tx.commit();

            tx = testSession.beginTransaction();
            List<StudentInfo> studentInfoList = testSession.createQuery("from studentInfo ").list();
            tx.commit();

            Assert.assertEquals(4, studentInfoList.size());
            assertTrue(studentInfoList.stream().noneMatch(studentInfo -> studentInfo.getId() == idStudentInfoToDelete));
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.commit();
        }
    }
}
