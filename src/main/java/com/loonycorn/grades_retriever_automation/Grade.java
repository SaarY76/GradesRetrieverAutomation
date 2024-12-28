package com.loonycorn.grades_retriever_automation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Grade {// this class represent a Grade of course from the school's website
    // class variables
    private String courseName;
    private String semester;
    private String grade;
    private Date dateOfUpdate;

    // constructor
    public Grade(String courseName, String semester, String grade, Date dateOfUpdate) {
        this.courseName = courseName.substring(0, courseName.length()-1);
        this.semester = semester;
        this.grade = grade;
        this.dateOfUpdate = dateOfUpdate;
    }

    // getters and setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getDateOfUpdate() {
        return dateOfUpdate;
    }

    public void setDateOfUpdate(Date dateOfUpdate) {
        this.dateOfUpdate = dateOfUpdate;
    }

    public String toString () {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(this.dateOfUpdate);
        return "Grade [Course Name : " + this.courseName + ", Semester : " + this.semester + ", Grade : "
                + this.grade + ", Date of update : " + formattedDate + "]\n";
    }
}
