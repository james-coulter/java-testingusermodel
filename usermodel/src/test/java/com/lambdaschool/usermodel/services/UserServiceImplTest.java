package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        List<User> myList = userService.findAll();
        for (User u : myList) {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void aafindUserById() {
        assertEquals("barnbarn", userService.findUserById(11).getUsername());
    }

    @Test(expected = ComparisonFailure.class)
    public void aazfindUserByIdFail() {
        assertEquals("spookyGhost", userService.findUserById(11).getUsername());
    }

    @Test
    public void bfindByNameContaining() {
    }

    @Test
    public void cfindAll() {

        assertEquals(30, userService.findAll().size());
    }


    @Test
    public void efindByName() {

        assertEquals("cinnamon", userService.findByName("cinnamon").getUsername());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void zfsave() {


        List<UserRoles> thisrole = new ArrayList<>();
        User newUser = new User("TestingUser", "fakepassword", "myemail@email.com", thisrole);
        User addUser = userService.save(newUser);

        assertNotNull(addUser);
        User foundUser   = userService.findUserById(addUser.getUserid());
        assertEquals(addUser.getUsername(), foundUser.getUsername());
    }

    @Test
    public void aupdate() {

        List<UserRoles> thisrole = new ArrayList<>();
        thisrole.add(new UserRoles());
        thisrole.get(0).setRole(new Role());
        thisrole.get(0).getRole().setRoleid(2);
        thisrole.get(0).setUser(new User());
        User newUser = new User("TestingUser", "fakepassword1", "myemail@email.com", thisrole);

        User updateUser = userService.update(newUser, 51);
        assertEquals("fakepassword1", updateUser.getPassword());
    }

    @Test
    public void hgetCountUserEmails() {
    }

    @Test
    public void ideleteUserRole() {
    }

    @Test
    public void jaddUserRole() {
    }

    @Test
    public void zdelete() {
        userService.delete(11);
        assertEquals(29, userService.findAll().size());
    }
}