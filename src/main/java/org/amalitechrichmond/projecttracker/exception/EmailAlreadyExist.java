package org.amalitechrichmond.projecttracker.exception;

public class EmailAlreadyExist extends ResourceNotFoundException {
    public EmailAlreadyExist(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
