package com.github.CulinaryApp;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.CulinaryApp.views.RegistrationActivity;

public class EmailValidatorTest {
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(RegistrationActivity.emailValidator("name@email.com"));
    }
}
