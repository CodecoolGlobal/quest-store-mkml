package com.queststore.Services;

import com.queststore.DAO.ClassDAO;
import com.queststore.DAO.DaoException;
import com.queststore.Model.Class;
import com.queststore.Model.User;
import com.queststore.Model.UserType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UserCardAddTest {

    @Test
    public void createStudentTest2() throws DaoException {
        ClassDAO classDAOMock = mock(ClassDAO.class);
        when(classDAOMock.getClassId(any(String.class))).thenReturn(java.util.Optional.of(1));
        UserCardAdd userCardAdd = new UserCardAdd(classDAOMock);
        //verify(classDAOMock.getClassId(any(String.class)), times(1));
        List<String> items = new ArrayList<>();
        items.add("someEmailName");
        items.add("someFirstName");
        items.add("someLastName");

        User user = userCardAdd.createStudent(items);
        User expectedUser = new User(
                1,
                "someFirstName",
                "someLastName",
                "someEmailName",
                null,
                null,
                new UserType(1, "student")
        );

        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }
}