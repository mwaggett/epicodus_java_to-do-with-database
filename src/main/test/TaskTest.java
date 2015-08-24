import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;

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

 }
