package com.github.CulinaryApp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.CulinaryApp.views.RegistrationActivity;

import org.junit.Test;

public class RegistrationTests {

    //True test
    @Test
    public void passwordValidator_PasswordsSimple_ReturnsTrue() {
        String pass1 = "password";
        String pass2 = "password";
        assertTrue(RegistrationActivity.passwordsMatch(pass1, pass2));
    }

    //False Test
    @Test
    public void passwordValidator_PasswordsSimple_ReturnsFalse() {
        String pass1 = "password";
        String pass2 = "default";
        assertFalse(RegistrationActivity.passwordsMatch(pass1, pass2));
    }

    //True test
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(RegistrationActivity.emailValidator("name@email.com"));
    }

    //False Tests
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsFalse_One() {
        assertFalse(RegistrationActivity.emailValidator("name.email.com"));
    }
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsFalse_Two() {
        assertFalse(RegistrationActivity.emailValidator("name@emaildotcom"));
    }
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsFalse_Three() {
        assertFalse(RegistrationActivity.emailValidator("thisisanemailiswear"));
    }



}
