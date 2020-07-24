package com.lambdaschool.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.*;
import com.lambdaschool.bookstore.services.AuthorService;
import com.lambdaschool.bookstore.services.BookService;
import com.lambdaschool.bookstore.services.SectionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)

/*****
 * Due to security being in place, we have to switch out WebMvcTest for SpringBootTest
 * @WebMvcTest(value = userController.class)
 */
@SpringBootTest(classes = BookstoreApplication.class)

/****
 * This is the book and roles we will use to test!
 */
@WithMockUser(username = "admin", roles = {"ADMIN", "DATA"})
public class BookControllerTest
{
    /******
     * WebApplicationContext is needed due to security being in place.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private SectionService sectionService;


    List<Book> bookList = new ArrayList<>();
    List<Section> sectionList = new ArrayList<>();


    @Before
    public void setUp() throws Exception
    {
        /*****
         * The following is needed due to security being in place!
         */
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        /*****
         * Note that since we are only testing bookstore data, you only need to mock up bookstore data.
         * You do NOT need to mock up book data. You can. It is not wrong, just extra work.
         */

        // Make Sectionlist
        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        Section s3 = new Section("Travel");
        Section s4 = new Section("Business");
        Section s5 = new Section("Religion");

        s1.setSectionid(11);
        s2.setSectionid(22);
        s3.setSectionid(33);
        s4.setSectionid(44);
        s5.setSectionid(55);

        sectionList.add(s1);
        sectionList.add(s2);
        sectionList.add(s3);
        sectionList.add(s4);
        sectionList.add(s5);

        // Make Booklist
        Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);
        Book b2 = new Book("Digital Fortress", "9788489367012", 2007, s1);
        Book b3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);
        Book b4 = new Book("Essentials of Finance", "1314241651234", 0, s4);
        Book b5 = new Book("Calling Texas Home", "1885171382134", 2000, s3);

        b1.setBookid(111);
        b2.setBookid(222);
        b3.setBookid(333);
        b4.setBookid(444);
        b5.setBookid(555);

        bookList.add(b1);
        bookList.add(b2);
        bookList.add(b3);
        bookList.add(b4);
        bookList.add(b5);

    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void listAllBooks() throws
            Exception
    {
        String apiUrl = "/books/books";
        Mockito.when(bookService.findAll()).thenReturn(bookList);
        // when you see "bookService.findAll()" dont call it use booklist

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        // create request (GET request to apiUrl and accept json data)

        // perform request
        MvcResult r = mockMvc.perform(rb).andReturn();
        // capture request as string
        String tr = r.getResponse().getContentAsString();

        // Object mapper does conversions
        ObjectMapper mapper = new ObjectMapper();
        // take string and write it as a sring value in JSON
        String er = mapper.writeValueAsString(bookList);

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void getBookById() throws
            Exception
    {
        String apiUrl = "/books/book/13";
        Mockito.when(bookService.findBookById(13)).thenReturn(bookList.get(2));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList.get(2));

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void getNoBookById() throws
            Exception
    {
        String apiUrl = "/books/book/5000";
        Mockito.when(bookService.findBookById(5000)).thenReturn(null); // EXPECTING NULL HERE

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void addNewBook() throws
            Exception
    {
        String apiUrl = "/books/book";

        Section s1 = new Section("section1");
        s1.setSectionid(1112233);

        Book book = new Book("nice", "yes", 2999, s1);
        book.setSection(new Section("section1"));
        book.setBookid(999);

        ObjectMapper mapper = new ObjectMapper();
        String bookString = mapper.writeValueAsString(book);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(book);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bookString);

        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteBookById() throws
            Exception
    {
        String apiUrl = "/books/book/{bookid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}