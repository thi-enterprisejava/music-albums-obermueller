package de.thi.mymusic.repository;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael on 07.11.2015.
 */
public class AlbumRepositoryImplTest {

    /**
     * class under test
     */
    static Repository<Album> repository;

    @BeforeClass
    public static void setUp() throws Exception {
        repository = new AlbumRepositoryImpl();
        repository.add(new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                new Song(2, "Meantime", "02:14")), 2007));
        repository.add(new Album("Boom Box", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Fix It", "02:56"),
                new Song(2, "Milk & Honey", "03:04")), 2011));
        repository.add(new Album("Smack Smash", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Big Attack", "02:24"),
                new Song(2, "Vision", "02:46")), 2004));
    }

    @Test
    public void thatAddCreateNewElementInList() throws Exception {

        repository.add(new Album("Living Targets", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Not Ready to Rock", "01:27"),
                new Song(2, "God Knows", "02:32")), 2002));

        assertEquals(4, repository.findAll().size());
        assertEquals(new Album("Living Targets", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Not Ready to Rock", "01:27"),
                new Song(2, "God Knows", "02:32")), 2002), repository.findAll().get(3));

        repository.remove(new Album("Living Targets", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Not Ready to Rock", "01:27"),
                new Song(2, "God Knows", "02:32")), 2002));
    }

    @Test
    public void thatFindAllContainsAllElements() throws Exception {

        List<Album> result = repository.findAll();

        assertEquals(3, result.size());
    }

    @Test
    public void thatFindByNameContainsCorrectResults() throws Exception {

        List<Album> result = repository.findByName("Boom Box");

        assertEquals(1, result.size());
        assertEquals(new Album("Boom Box", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Fix It", "02:56"),
                new Song(2, "Milk & Honey", "03:04")), 2011), result.get(0));
    }

    @Test
    public void thatRemoveDeleteCorrectElement() throws Exception {

        repository.remove(new Album("Boom Box", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Fix It", "02:56"),
                new Song(2, "Milk & Honey", "03:04")), 2011));

        assertEquals(2, repository.findAll().size());
        assertEquals(repository.findAll(), Arrays.asList(new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                new Song(2, "Meantime", "02:14")), 2007), new Album("Smack Smash", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Big Attack", "02:24"),
                new Song(2, "Vision", "02:46")), 2004)));

        repository.add(new Album("Boom Box", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "Fix It", "02:56"),
                new Song(2, "Milk & Honey", "03:04")), 2011));
    }

    //TODO Implement test method if search found nothing!
    //TODO Implement test method to test lower and upper case
}