import java.util.List;
import org.sql2o.*;


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
    }
    else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName());
    }
  }

  public static List<Category> all() {
    String sql ="SELECT id, name FROM categories";
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
      String sql ="select * from categories where id=:id";
      Category category = con.createQuery(sql)
      .addParameter("id",id)
      .executeAndFetchFirst(Category.class);
      return category;
    }
  }

  public List<Task> getTasks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where categoryId=:id";
      return con.createQuery(sql)
       .addParameter("id", this.id)
       .executeAndFetch(Task.class);
    }
  }
  //added stuff

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM categories WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void update(String name) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE categories SET name = :name WHERE id = :id";
        con.createQuery(sql)
          .addParameter("name", name)
          .addParameter("id", id)
          .executeUpdate();
      }
    }
}
