package by.pvt.helper;

import by.pvt.pojo.Project;
import by.pvt.pojo.Student;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ProjectHelper {
    private static Logger log = Logger.getLogger(ProjectHelper.class.getName());

    public static void addStudent(Project project, Student student) {
        if (project == null || student == null) {
            throw new IllegalArgumentException("Project or student cannot be null");
        }

        if (project.getStudents() == null) {
            project.setStudents(new ArrayList<>());
        }
        project.getStudents().add(student);

        if (student.getProjects() == null) {
            student.setProjects(new ArrayList<>());
        }
        student.getProjects().add(project);
    }

    public static void removeStudent(Project project, Student student) {
        if (project == null || student == null) {
            throw new IllegalArgumentException("Project or student cannot be null");
        }

        student.getProjects().remove(project);
        log.info("Project size=" + student.getProjects().size());

        project.getStudents().remove(student);
        log.info("Student size=" + project.getStudents().size());
    }
}
