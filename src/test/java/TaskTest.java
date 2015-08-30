import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;

public class TaskTest {

     @Rule
     public DatabaseRule database = new DatabaseRule();

     @Test
     public void all_emptyAtFirst() {
       assertEquals(Task.all().size());
     }

     @Test
     public void equals_returnsTrueIfDescriptionsAreTheSame() {
       Task firstTask = new Task("Mow the lawn");
       Task secondTask = new Task("Mow the lawn");
       assertTrue(firstTask.equals(secondTask));
     }

     @Test
     public void equals_returnsFalseIfDescriptionsAreDifferent() {
       Task firstTask = new Task("Mow the lawn");
       Task secondTask = new Task("Throw a party");
       assertTrue(!firstTask.equals(secondTask));
     }

      @Test
      public void save_assignsIdToObject() {
        Task myTask = new Task("Mow the lawn");
        myTask.save();
        Task savedTask = Task.all().get(0);
        assertEquals(myTask.getId(), savedTask.getId());
      }

      @Test
      public void find_findsTaskInDatabase_true() {
        Task myTask = new Task("Mow the lawn");
        myTask.save();
        Task savedTask = Task.find(myTask.getId());
        assertTrue(myTask.equals(savedTask));
      }

      @Test
      public void delete_deletesTaskFromDatabase_true() {
        Category myCategory = new Category("Banking");
        myCategory.save();
        Task myTask = new Task("Mow the lawn");
        myTask.save();
        myTask.delete();
        assertEquals(Task.all().size(), 0);
      }

      @Test
      public void update_changesTaskNameInDatabase_true() {
        Category myCategory = new Category("Morping");
        myCategory.save();
        Task myTask = new Task("Mow the lawn");
        myTask.save();
        String description = "Throw a party";
        myTask.update(description);
        assertTrue(Task.all().get(0).getDescription().equals(description));

      }
 }
