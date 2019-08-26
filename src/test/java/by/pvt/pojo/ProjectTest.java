package by.pvt.pojo;

import by.pvt.util.DBUnitTestBase;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static by.pvt.helper.ProjectHelper.addStudent;
import static by.pvt.helper.ProjectHelper.removeStudent;

public class ProjectTest extends DBUnitTestBase {

    public ProjectTest(String name) {
        super(name);
    }

    @Override
    protected String getResourceName() {
        return "project_TestDataSet.xml";
    }

    static Project createTestData(int index) {
        Project project = new Project();
        project.setProjectName("projectName" + index);
        project.setStartDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 12);
        project.setEndDate(calendar.getTime());
        return project;
    }

    @Test
    public void testSaveAndUpdate() {
        Transaction tx = null;
        try {
            tx = testSession.beginTransaction();

            Project project = createTestData(3);

            Student loadedStudent1 = testSession.get(Student.class, 1L);
            Student loadedStudent2 = testSession.get(Student.class, 2L);
            Student loadedStudent3 = testSession.get(Student.class, 3L);
            Project loadedProject = testSession.get(Project.class, 2L);

            addStudent(project, loadedStudent1);
            addStudent(project, loadedStudent2);
            addStudent(project, loadedStudent3);

            addStudent(loadedProject, loadedStudent1);
            addStudent(loadedProject, loadedStudent3);

            testSession.save(project);
            testSession.update(loadedProject);

            testSession.update(loadedStudent1);
            testSession.update(loadedStudent2);
            testSession.update(loadedStudent3);

            tx.commit();

            tx = testSession.beginTransaction();
            Project savedProject = testSession.load(Project.class, 1L);
            assertEquals(2, savedProject.getStudents().size());
            tx.commit();

            tx = testSession.beginTransaction();


        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        }
    }


    @Test
    public void testGet() {
        Student loadedStudent = testSession.get(Student.class, 1L);
        List<Project> projects = loadedStudent.getProjects();
        assertEquals(2, projects.size());

        Long idProjectToGet = 1L;
        Project loadedProject = testSession.get(Project.class, idProjectToGet);
        assertEquals(1, loadedProject.getId().intValue());
    }

    @Test
    public void testDelete() {
        Transaction tx = null;
        try {
            tx = testSession.beginTransaction();
            Long idProjectToDelete = 2L;
            Project loadedProject = testSession.get(Project.class, idProjectToDelete);

            Iterator<Student> studentIterator = loadedProject.getStudents().iterator();

            while (studentIterator.hasNext()) {
                Student student = studentIterator.next();
                if (student.getProjects().contains(loadedProject)) {
                    removeStudent(loadedProject, student);
                    break;
                }
            }

            testSession.delete(loadedProject);
            tx.commit();

            List<Project> projects = testSession.createQuery("from project ").list();
            assertTrue(projects.stream().noneMatch(project -> project.getId() == idProjectToDelete));

        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        }
    }
}
