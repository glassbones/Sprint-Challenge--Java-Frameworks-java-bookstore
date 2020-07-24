package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void findAll()
    {
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void findBookById()
    {
        assertEquals("Flatterland", bookService.findBookById(1).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFindBookById()
    {
        assertEquals(1337, bookService.findBookById(12412357).getBookid());
    }

    @Test
    public void delete()
    {
        // do this
        bookService.delete(2);
        // expect 4 from userService.findAll().size()
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void save()
    {
        /*
        String username = "batman";
        Role r1 = roleService.findByName("admin");
        Role r2 = roleService.findByName("user");

        User user = new User("batman",
                "password",
                "batman@batman.com");
        user.getRoles().add(new UserRoles(user, r1));
        user.getRoles().add(new UserRoles(user, r2));

        User addUser = userService.save(user);

        // expect that value is not null
        assertNotNull(addUser);
        // addUser.getUsername()  expect username
        assertEquals(username, addUser.getUsername());
        */
        
    }

    @Test
    public void update()
    {
    }

    @Test
    public void deleteAll()
    {
    }
}