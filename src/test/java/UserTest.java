// import com.example.google_drive_clone.model.File;
// import com.example.google_drive_clone.model.Folder;
// import com.example.google_drive_clone.model.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// public class UserTest {

//     private User user;
//     private File file1;
//     private File file2;
//     private Folder folder;

//     @BeforeEach
//     void setUp() {
//         // Initialize test data before each test case
//         user = new User("testuser", "password123");

//         // Creating a dummy Folder object for association with files
//         folder = new Folder();
//         folder.setId(1L);
//         folder.setName("Test Folder");

//         // Creating dummy file data
//         byte[] data1 = "Sample data for file1".getBytes();
//         byte[] data2 = "Sample data for file2".getBytes();

//         // Initialize File objects with all required fields
//         file1 = new File();
//         file1.setFileName("file1.txt");
//         file1.setContentType("text/plain");
//         file1.setData(data1);
//         file1.setUploadTime(LocalDateTime.now());
//         file1.setUser(user);
//         file1.setFolder(folder);

//         file2 = new File();
//         file2.setFileName("file2.txt");
//         file2.setContentType("text/plain");
//         file2.setData(data2);
//         file2.setUploadTime(LocalDateTime.now());
//         file2.setUser(user);
//         file2.setFolder(folder);
//     }

//     @Test
//     void testAddFile() {
//         // Test adding a file to the user's file list
//         user.addFile(file1);
//         assertEquals(1, user.getFiles().size());
//         assertEquals(file1, user.getFiles().get(0));
//         assertEquals(user, file1.getUser()); // Ensure the file's user is correctly set
//     }

//     @Test
//     void testRemoveFile() {
//         // Test removing a file from the user's file list
//         user.addFile(file1);
//         user.addFile(file2);
//         user.removeFile(file1);

//         assertEquals(1, user.getFiles().size());
//         assertFalse(user.getFiles().contains(file1));
//         assertNull(file1.getUser()); // Ensure the file's user is set to null
//     }

//     @Test
//     void testGetFiles() {
//         // Test retrieving the user's file list
//         user.addFile(file1);
//         user.addFile(file2);

//         List<File> files = user.getFiles();
//         assertEquals(2, files.size());
//         assertTrue(files.contains(file1));
//         assertTrue(files.contains(file2));
//     }

//     @Test
//     void testGetId() {
//         // Test setting and getting the user's ID
//         user.setId(1L);
//         assertEquals(1L, user.getId());
//     }

//     @Test
//     void testGetUsername() {
//         // Test retrieving the username
//         assertEquals("testuser", user.getUsername());
//     }

//     @Test
//     void testSetUsername() {
//         // Test setting the username
//         user.setUsername("newuser");
//         assertEquals("newuser", user.getUsername());
//     }

//     @Test
//     void testGetPassword() {
//         // Test retrieving the password
//         assertEquals("password123", user.getPassword());
//     }

//     @Test
//     void testSetPassword() {
//         // Test setting the password
//         user.setPassword("newpassword");
//         assertEquals("newpassword", user.getPassword());
//     }

//     @Test
//     void testSetFiles() {
//         // Test setting the user's file list
//         List<File> newFiles = new ArrayList<>();
//         newFiles.add(file1);
//         user.setFiles(newFiles);

//         assertEquals(1, user.getFiles().size());
//         assertEquals(file1, user.getFiles().get(0));
//     }

//     @Test
//     void testSetId() {
//         // Test setting the user's ID
//         user.setId(10L);
//         assertEquals(10L, user.getId());
//     }
// }
