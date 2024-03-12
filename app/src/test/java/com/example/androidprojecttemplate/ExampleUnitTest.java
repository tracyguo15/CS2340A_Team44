package com.example.androidprojecttemplate;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.androidprojecttemplate.models.UserLoginData;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //Tracy Guo
    @Test
    public void nullUsername() {
        UserLoginData login = new UserLoginData(null, "password", "name");
        assertEquals(null, login.getUsername());
    }

    //Tracy Guo
    @Test
    public void nullPassword() {
        UserLoginData login = new UserLoginData("Username", null, "null");
        assertEquals(null, login.getPassword());
    }
    //Michael Leonick
    @Test
    public void emptyUsername() {
        UserLoginData login = new UserLoginData("", "Password", "Name");
        assertEquals("", login.getUsername());
    }

    //Michael Leonick
    @Test
    public void emptyPassword() {
        UserLoginData login = new UserLoginData("Username", "", "Name");
        assertEquals("", login.getPassword());
    }

    //Michael Vaden
    @Test
    public void whitespaceUsername() {
        UserLoginData login = new UserLoginData(" ", "Password", "Name");
        assertEquals(" ", login.getUsername());
    }

    //Michael Vaden
    @Test
    public void whitespacePassword() {
        UserLoginData login = new UserLoginData("Username", " ", "Name");
        assertEquals(" ", login.getPassword());
    }
}