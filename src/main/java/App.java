import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;
import java.util.Arrays;
import java.util.Set;

 public class App {
    public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      List<Task> tasks = Task.all();
      List<Category> categories = Category.all();
      model.put("tasks", tasks);
      model.put("categories", categories);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/admin", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      List<Task> tasks = Task.all();
      List<Category> categories = Category.all();

      model.put("tasks", tasks);
      model.put("categories", categories);
      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add-category", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Category newCategory = new Category(request.queryParams("name"));
      newCategory.save();

      Object[] params = request.queryParams().toArray();

      for (int i = 1; i < params.length; i++) {
        String selectedTask = (String) request.queryParams((String) params[i]);
        Task task = Task.find(Integer.parseInt(selectedTask));
        newCategory.addTask(task);
      }

      response.redirect("/admin");
      return null;
    });

    post("/add-task", (request, response) -> {

      Task newTask = new Task(request.queryParams("description"));
      newTask.save();

      Object[] params = request.queryParams().toArray();

      for (int i = 1; i < params.length; i++) {
        String selectedCategory = (String) request.queryParams((String) params[i]);
        Category category = Category.find(Integer.parseInt(selectedCategory));
        newTask.addCategory(category);
      }

      response.redirect("/admin");
      return null;
    });




  }//end of main

}//end of app
