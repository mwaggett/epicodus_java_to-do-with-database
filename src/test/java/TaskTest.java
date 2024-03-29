import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class TaskTest {

     @Rule
     public DatabaseRule database = new DatabaseRule();

     @Test
     public void all_emptyAtFirst() {
       assertEquals(Task.all().size(), 0);
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
      public void update_changesTaskNameInDatabase_true() {
        Category myCategory = new Category("Morping");
        myCategory.save();
        Task myTask = new Task("Mow the lawn");
        myTask.save();
        String description = "Throw a party";
        myTask.update(description);
        assertTrue(Task.all().get(0).getDescription().equals(description));

      }

      @Test
      public void addCategory_addsCategoryToTask() {
        Category myCategory = new Category("Household chores");
        myCategory.save();

        Task myTask = new Task("Break all the plates");
        myTask.save();

        myTask.addCategory(myCategory);
        Category savedCategory = myTask.getCategories().get(0);
        assertTrue(myCategory.equals(savedCategory));
      }

      @Test
      public void getCategories_returnsAllCategories_ArrayList() {
        Category myCategory = new Category("Household chores");
        myCategory.save();

        Task myTask = new Task("Banking");
        myTask.save();

        myTask.addCategory(myCategory);
        List savedCategories = myTask.getCategories();
        assertEquals(savedCategories.size(), 1);
      }

      @Test
      public void delete_deletesAllTasksAndListAssociations() {
        Category myCategory = new Category("Banking");
        myCategory.save();

        Task myTask = new Task("Steal money");
        myTask.save();

        myTask.addCategory(myCategory);
        myTask.delete();
        assertEquals(myCategory.getTasks().size(), 0);
      }
 }
