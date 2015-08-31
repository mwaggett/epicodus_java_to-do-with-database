import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;


public class Category {
  private int id;
  private String name;

  public int getId() {
    return id;
  }
  public String getName() {
    return name;
  }

  public Category(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object otherCategory) {
    if(!(otherCategory instanceof Category )) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName());
    }
  }

  public static List<Category> all() {
    String sql ="SELECT id, name FROM categories ORDER BY name ASC";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql ="INSERT INTO categories (name) values (:name)";
      this.id = (int) con.createQuery(sql,true)
      .addParameter("name", this.name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Category find(int id ) {
    try(Connection con = DB.sql2o.open()) {
      String sql ="SELECT * FROM categories WHERE id=:id";
      Category category = con.createQuery(sql)
      .addParameter("id",id)
      .executeAndFetchFirst(Category.class);
      return category;
    }
  }

  public void update(String name) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE categories SET name= :name WHERE id= :id";
        con.createQuery(sql)
          .addParameter("name", name)
          .addParameter("id", id)
          .executeUpdate();
    }
  }

  public void addTask(Task task) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id)";
      con.createQuery(sql)
        .addParameter("category_id", this.getId())
        .addParameter("task_id", task.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Task> getTasks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT task_id FROM categories_tasks WHERE category_id = :category_id";
      List<Integer> taskIds = con.createQuery(sql)
      .addParameter("category_id", this.getId())
      .executeAndFetch(Integer.class);

      ArrayList<Task> tasks = new ArrayList<Task>();

      for (Integer taskId : taskIds) {
        String taskQuery = "SELECT * FROM tasks WHERE id = :taskId";
        Task task = con.createQuery(taskQuery)
          .addParameter("taskId", taskId)
          .executeAndFetchFirst(Task.class);
        if (!task.isComplete()) {
          tasks.add(task);
        }
      }
      return tasks;
    }
  }

  public ArrayList<Task> getCompletedTasks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT task_id FROM categories_tasks WHERE category_id = :category_id";
      List<Integer> taskIds = con.createQuery(sql)
      .addParameter("category_id", this.getId())
      .executeAndFetch(Integer.class);

      ArrayList<Task> tasks = new ArrayList<Task>();

      for (Integer taskId : taskIds) {
        String taskQuery = "SELECT * FROM tasks WHERE id = :taskId";
        Task task = con.createQuery(taskQuery)
          .addParameter("taskId", taskId)
          .executeAndFetchFirst(Task.class);
        if (task.isComplete()) {
          tasks.add(task);
        }
      }
      return tasks;
    }
  }

  // public List<Task> getCompletedTasks() {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT tasks.*, categories_tasks.category_id FROM categories JOIN categories_tasks ON categories_tasks.category_id = categories.id JOIN tasks ON categories_tasks.task_id = tasks.id WHERE complete = true AND category_id = :category_id;";
  //     List<Task> completedTasks = con.createQuery(sql)
  //         .addParameter("category_id", id)
  //         .executeAndFetch(Task.class);
  //     return completedTasks;
  //   }
  // }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM categories WHERE id = :id";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM categories_tasks WHERE category_id = :categoryId";
        con.createQuery(joinDeleteQuery)
          .addParameter("categoryId", this.getId())
          .executeUpdate();
    }
  }
}
