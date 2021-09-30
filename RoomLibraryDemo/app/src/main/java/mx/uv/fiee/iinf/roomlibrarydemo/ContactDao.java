package mx.uv.fiee.iinf.roomlibrarydemo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Query ("SELECT full_name, phone_number FROM contact ORDER BY id DESC")
    List<Contact> getAll ();

    @Query ("SELECT * FROM contact WHERE full_name LIKE :name LIMIT 1")
    Contact findByName (String name);

    @Insert
    void insertAll (Contact...contacts);

    @Delete
    void delete (Contact contact);
}
