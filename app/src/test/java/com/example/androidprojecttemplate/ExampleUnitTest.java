package com.example.androidprojecttemplate;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.androidprojecttemplate.models.MealData;
import com.example.androidprojecttemplate.models.UserData;
import com.example.androidprojecttemplate.models.UserLoginData;
import com.example.androidprojecttemplate.views.InputMealPage;

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

    //Rohan Bhole
    @Test
    public void testMealData() {
        MealData meal = new MealData();
        meal.setCalories(500);
        meal.setDate("20240312");
        meal.setUsername("Rohan");
        assertEquals(500, meal.getCalories());
        assertEquals("20240312", meal.getDate());
        assertEquals("Rohan", meal.getUsername());

        MealData meal2 = new MealData("Rohan", "20240312", 500);
        assertEquals(500, meal2.getCalories());
        assertEquals("20240312", meal2.getDate());
        assertEquals("Rohan", meal2.getUsername());
    }

    //Rohan Bhole
    @Test
    public void testNullMealData() {
        MealData meal2 = new MealData(null, "20240312", 500);
        assertEquals(null, meal2.getUsername());
    }

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

    //Daniel Deller
    @Test
    public void negHeight() {
        boolean heightExc = false;
        UserData dataUse = new UserData();
        try {
            dataUse.setHeight(-10);
        } catch (IllegalArgumentException e) {
            heightExc = true;
        }
        assertEquals(heightExc,true);
    }
    //Daniel Deller
    public void negAge() {
        boolean ageExc = false;
        UserData dataUse = new UserData();
        try {
            dataUse.setAge(-3);
        } catch (IllegalArgumentException e) {
            ageExc = true;
        }
        assertEquals(ageExc,true);
}}
