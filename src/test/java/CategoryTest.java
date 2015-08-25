import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.*;

public class CategoryTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Category.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Category firstCategory = new Category("Banking");
    Category secondCategory = new Category("Banking");
    assertTrue(firstCategory.equals(secondCategory));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Category newCategory = new Category("Banking");
    newCategory.save();
    assertTrue(Category.all().get(0).equals(newCategory));
  }

  @Test
  public void find_findsCategoryInDatabase_true() {
    Category myCategory = new Category("Banking");
    myCategory.save();
    Category savedCategory = Category.find(myCategory.getId());
    assertTrue(myCategory.equals(savedCategory));
  }

  @Test
  public void getTasks_retrievesAllTasksFromDatabase_tasksList() {
    Category myCategory = new Category("Banking");
    myCategory.save();
    Task firstTask = new Task("steal money", myCategory.getId());
    firstTask.save();
    Task secondTask = new Task("lots of money", myCategory.getId());
    secondTask.save();
    Task[] tasks = new Task[] { firstTask, secondTask };
    assertTrue(myCategory.getTasks().containsAll(Arrays.asList(tasks)));
  }
}
