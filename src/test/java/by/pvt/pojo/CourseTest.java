package by.pvt.pojo;

import by.pvt.util.DBUnitTestBase;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;

public class CourseTest extends DBUnitTestBase {

    public CourseTest(String name) {
        super(name);
    }

    @Override
    protected String getResourceName() {
        return "course_TestDataSet.xml";
    }

    static Course createTestData(int index) {
        Course course = new Course();
        course.setFacultyName("facultyName" + index);
        course.setCourseNumber(index);
        return course;
    }

    @Test
    public void testSave() {
        Transaction tx = null;
        try {
            tx = testSession.beginTransaction();
            Course course = createTestData(6);

            Student student = StudentTest.createTestData(44);
            student.setCourse(course);
            testSession.save(course);
            testSession.save(student);
            tx.commit();

            tx = testSession.beginTransaction();
            List<Course> courseList = testSession.createQuery("from course ").list();
            tx.commit();

            assertTrue(courseList.stream().anyMatch(course1 -> course1.equals(course)));
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        }
    }

    @Test
    public void testGet() {
        Transaction tx = null;
        Long idCourseToGet = 3L;
        try {
            tx = testSession.beginTransaction();
            Course loadedCourse = testSession.get(Course.class, idCourseToGet);
            tx.commit();

            assertEquals(idCourseToGet, loadedCourse.getId());
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        }
    }

    @Test
    public void testDelete() {
        Transaction tx = null;
        Long idCourseToDelete = 1L;
        try {
            tx = testSession.beginTransaction();
            Course loadedCourse = testSession.load(Course.class, idCourseToDelete);
            assertEquals(2, loadedCourse.getStudents().size());

            for (Student student : loadedCourse.getStudents()) {
                student.setCourse(null);
                testSession.update(student);
            }

            testSession.delete(loadedCourse);
            tx.commit();

            tx = testSession.beginTransaction();
            List<Course> courseList = testSession.createQuery("from course ").list();
            tx.commit();

            assertTrue(courseList.stream().noneMatch(course -> course.getId() == idCourseToDelete));
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        }
    }
}
