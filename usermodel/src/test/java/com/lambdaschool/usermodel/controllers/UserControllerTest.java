package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private List<User> userList;
    @Before
    public void setUp() throws Exception {
        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        Role r3 = new Role("data");
        r3.setRoleid(3);

        // admin, data, user
        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(),
                r1));
        admins.add(new UserRoles(new User(),
                r2));
        admins.add(new UserRoles(new User(),
                r3));
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local",
                admins);
        u1.setUserid(1);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(1);

        User u2 = new User("admin2",
                "password2",
                "admin2@lambdaschool.local",
                admins);
        u2.setUserid(2);
        u2.getUseremails()
                .add(new Useremail(u1,
                        "admin2@email.local"));
        u2.getUseremails().get(0).setUseremailid(2);

        userList.add(u1);
        userList.add(u2);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllUsers() throws Exception {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String tr = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String expectedResults = mapper.writeValueAsString(userList);
        System.out.println("Expect " + expectedResults);
        System.out.println("Actual " + tr);
        assertEquals("returns", expectedResults, tr);
    }

    @Test
    public void getUserById() throws Exception {
        String apiUrl = "/users/user/1";
        Mockito.when(userService.findUserById(1))
                .thenReturn(userList.get(1));
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(1));
        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);
        assertEquals("Rest API Returns List", er, tr);


    }

    @Test
    public void getUserByIdNotFound() throws
            Exception
    {
        String apiUrl = "/users/user/77";
        Mockito.when(userService.findUserById(77))
                .thenReturn(null);
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();
        String er = "";
        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);
        assertEquals("Rest API Returns List", er, tr);
    }


    @Test
    public void getUserByName() throws Exception {
        String apiUrl = "/users/user/name/admin2";
        Mockito.when(userService.findByName("admin2"))
                .thenReturn(userList.get(1));
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(1));
        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);
        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void getUserLikeName() throws Exception {
        String apiUrl = "/users/user/name/like/admin";
        Mockito.when(userService.findByNameContaining("admin"))
                .thenReturn(userList);
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);
        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);
        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void addNewUser() throws Exception {
        String apiUrl = "/users/user";

        ArrayList<UserRoles> thisRole = new ArrayList<>();

        User u3 = new User("admin3", "admin3Pass", "admin3@gmail.com", thisRole);
        u3.setUserid(3);
        ObjectMapper mapper = new ObjectMapper();
        String restaurantString = mapper.writeValueAsString(u3);
        Mockito.when(userService.save(any(User.class)))
                .thenReturn(u3);
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(restaurantString);
        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateFullUser() throws Exception {
        String apiUrl = "/users/user";

        ArrayList<UserRoles> thisRole = new ArrayList<>();

        User u3 = new User("admin3", "admin3Pass", "admin3@gmail.com", thisRole);
        u3.setUserid(3);
        ObjectMapper mapper = new ObjectMapper();
        String restaurantString = mapper.writeValueAsString(u3);
        Mockito.when(userService.save(any(User.class)))
                .thenReturn(u3);
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(restaurantString);
        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception {
        String apiUrl = "/users/user";

        ArrayList<UserRoles> thisRole = new ArrayList<>();

        User u3 = new User("admin3", "admin3Pass", "admin3@gmail.com", thisRole);
        u3.setUserid(3);
        ObjectMapper mapper = new ObjectMapper();
        String restaurantString = mapper.writeValueAsString(u3);
        Mockito.when(userService.save(any(User.class)))
                .thenReturn(u3);
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(restaurantString);
        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/users/user/{id}";
        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void getNumUserEmails()throws Exception{
    }

    @Test
    public void deleteUserRoleByIds() throws Exception{

        String apiUrl = "/users/user/{userid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void postUserRoleByIds() throws Exception {

        String apiUrl = "/users/user/{userid}/role/{roleid}";

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl, 3, 2);

        mockMvc.perform(rb)
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());

    }
}