package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    @Autowired
    private SectionService sectionService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);

        System.out.println("\n*** BEFORE ***");
        List<Book> mylist = bookService.findAll();
        List<Section> mySects = sectionService.findAll();

        for (Book u : mylist) {
            System.out.println(u.getBookid() + " " + u.getTitle());
        }
        for (Section s: mySects) {
            System.out.println(s.getSectionid() + " " + s.getName());
        }

        System.out.println();
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void findAll()
    {
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void findBookById()
    {
        assertEquals("Flatterland", bookService.findBookById(26).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFindBookById()
    {
        assertEquals(1337, bookService.findBookById(1337).getBookid());
    }

    @Test
    public void delete()
    {
        // do this
        bookService.delete(29);
        // expect 4 from userService.findAll().size()
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void save()
    {

        Section section = sectionService.findSectionById(21);
        Book book = new Book("The Shadow Rising",
                "9780312854317", 1992, section);

        Book addBook = bookService.save(book);

        assertNotNull(addBook);
        assertEquals("The Shadow Rising", addBook.getTitle());

        System.out.println("Expect: The Shadow Rising");
        System.out.println("Actual: " + addBook.getTitle());


    }

    @Test
    public void deleteAll()
    {
        bookService.deleteAll();
        // userService.findAll().size() expect 0
        assertEquals(0, bookService.findAll().size());
    }
}