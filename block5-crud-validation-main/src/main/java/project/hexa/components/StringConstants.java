package project.hexa.components;

import org.springframework.stereotype.Component;

@Component
public class StringConstants {
    //PERSON
    //public static final String PERSONS_NOT_FOUND = "No persons found for the user: ";
    public static final String PERSON_ID_NOT_FOUND = "No person found with the ID: ";
    public static final String PERSON_EMPTY = "No persons available. ";
    public static final String PERSON_IS_STUDENT = "This person is already a student.";
    public static final String PERSON_IS_PROFESOR = "This person is already a professor.";


    public static final String SIZE_DEFAULT_VALUE = "10";
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    //person/criteria
    public static final String CORRECT_DATE = "The lower date cannot be later than the upper date.";
    public static final String CORRECT_SIZE = "Page size must be greater than 0.";
    public static final String PATTERN_PAGE = "The page number must be greater than or equal to 0.";
    public static final String ERROR_MSG_ORDERBY = "The 'orderBy' parameter can only be 'username' or 'name'";
    //STUDENT
    public static final String STUDENT_ID_NOT_FOUND = "No student found with the ID: ";
    public static final String STUDENT_EMPTY = "No students available. ";
    public static final String STUDENT_EXIST = "The person is associated with a student. Delete the student first.";
    //PROFESSOR
    public static final String PROFESSOR_ID_NOT_FOUND = "No professor found with the ID: ";
    public static final String PROFESSOR_EXIST = "The person is associated with a professor. Delete the professor first.";
    public static final String PROFESSOR_EMPTY = "No professor available. ";
    //SUBJECT
    public static final String SUBJECT_ID_NOT_FOUND = "No subject found with the ID: ";
    public static final String SUBJECT_EMPTY = "There are no subjects. ";
    public static final String SUBJECT_ASSIGNED_OR_NOTEXIST = "All subjects are already assigned to the student or do not exist.";
    public static final String SUBJECT_UNASSIGNED="None of the subjects are assigned to the student.";
    public static final String SUBJECT_NOT_VALID = "Enter at least one valid subject. ";

    //Delete message
    public static final String DELETE_SUCCESSFULLY = "The person has been successfully deleted. Id: ";

    //OTHER
    public static final String VALIDATION_ERROR = "Validation error: ";

    public static final String WRONG_OUTPUT_TYPE = "The outputType must be simple or full.";

    public static final String OUTPUT_TYPE_SIMPLE = "simple";
    public static final String OUTPUT_TYPE_FULL = "full";
    public static final String OUTPUT_TYPE_INVALID = "invalid";
    public static final String OUTPUT_TYPE_EMPTY = "";
    public static final String OUTPUT_TYPE_NULL = null;

    public static final String URL_GET_PROFESSOR ="http://localhost:8081/professor/";


    //Person Exercise 9 CriteriaBuilder
    public static final String USERNAME_VARIABLE = "username";
    public static final String NAME_VARIABLE = "name";
    public static final String SURNAME_VARIABLE = "surname";
    public static final String CREATEDDATE_VARIABLE = "created_date";
    //Person Exercise 9 CriteriaBuilder TEST
    public static final String USERNAME_VARIABLE_TEST ="john";
    public static final String NAME_VARIABLE_TEST ="John";
    public static final String SURNAME_VARIABLE_TEST ="Doe";
    public static final String ORDERBY_VARIABLE_TEST ="username";
    public static final String INVALID_ORDERBY_VARIABLE_TEST ="invalidField";

    //App TEST
    public static final String APP_MESSAGE_CONTEXT ="The application context should load correctly.";
    public static final String APP_MESSAGE_NO_EXCEPTION ="The main method should execute without throwing exceptions.";





}



