package com.example.androidprojecttemplate;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.views.LoginPageActivity;
import com.example.androidprojecttemplate.views.PersonalInfo;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //Tracy
//    @Test
//    public void nullUsername() {
//        LoginPageActivity login = new LoginPageActivity();
//        login.setUsername(null);
//        assertEquals(null, login.getUsername());
//    }
//
//    //Tracy
//    @Test
//    public void nullPassword() {
//        LoginPageActivity login = new LoginPageActivity();
//        login.setPassword(null);
//        assertEquals(null, login.getPassword());
//    }
//    //Michael Leonick
//    @Test
//    public void emptyUsername() {
//        LoginPageActivity login = new LoginPageActivity();
//        login.setUsername("");
//        assertEquals("", login.getUsername());
//    }
//
//    //Michael Leonick
//    @Test
//    public void emptyPassword() {
//        LoginPageActivity login = new LoginPageActivity();
//        login.setPassword("");
//        assertEquals("", login.getPassword());
//    }

    //Adam Vaughn
    public void nullName() {
        UserLoginData login = new UserLoginData("Username", "null", null);
        assertEquals(null, login.getName());
    }
    //Adam Vaughn
    @Test
    public void emptyName() {
        UserLoginData login = new UserLoginData("User", "Password", "");
        assertEquals("", login.getName());
    }

}
