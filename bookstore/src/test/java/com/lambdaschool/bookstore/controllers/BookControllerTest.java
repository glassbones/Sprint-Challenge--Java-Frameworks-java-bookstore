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
    List<Author> authorList = new ArrayList<>();
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

        Author a1 = new Author("John", "Mitchell");
        Author a2 = new Author("Dan", "Brown");
        Author a3 = new Author("Jerry", "Poe");
        Author a4 = new Author("Wells", "Teague");
        Author a5 = new Author("George", "Gallinger");
        Author a6 = new Author("Ian", "Stewart");

        a2.setAuthorid(2);
        a3.setAuthorid(3);
        a4.setAuthorid(4);
        a5.setAuthorid(5);
        a6.setAuthorid(6);

        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        Section s3 = new Section("Travel");
        Section s4 = new Section("Business");
        Section s5 = new Section("Religion");

        s1.setSectionid(1);
        s2.setSectionid(2);
        s3.setSectionid(3);
        s4.setSectionid(4);
        s5.setSectionid(5);

        Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);
        b1.getWrotes().add(new Wrote(a6, new Book()));
        b1.setBookid(1);
        bookList.add(b1);

        Book b2 = new Book("Digital Fortess", "9788489367012", 2007, s1);
        b2.getWrotes().add(new Wrote(a2, new Book()));
        b2.setBookid(2);
        bookList.add(b2);

        Book b3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);
        b3.getWrotes().add(new Wrote(a2, new Book()));
        b3.setBookid(3);
        bookList.add(b3);

        Book b4 = new Book("Essentials of Finance", "1314241651234", 0, s4);
        b4.getWrotes().add(new Wrote(a3, new Book()));
        b4.getWrotes().add(new Wrote(a5, new Book()));
        b4.setBookid(4);
        bookList.add(b4);

        Book b5 = new Book("Calling Texas Home", "1885171382134", 2000, s3);
        b5.getWrotes()
                .add(new Wrote(a4, new Book()));
        b5.setBookid(5);
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
        s1.setSectionid(1);

        Book book = new Book("nice", "yes", 2999, s1);
        book.setSection(new Section("section1"));
        book.setBookid(100);

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
    public void updateFullBook() throws Exception {
        String apiUrl = "/books/book/{bookid}";

        Section s2 = new Section("section2");
        s2.setSectionid(2);

        // create a book
        Book book = new Book("nicer", "moreyes", 9999, s2);
        book.setSection(new Section("section2"));
        book.setBookid(200);

        ObjectMapper mapper = new ObjectMapper();
        String bookString = mapper.writeValueAsString(book);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(book);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, 14)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bookString);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
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