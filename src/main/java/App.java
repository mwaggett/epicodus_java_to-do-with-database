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

      for (Object param : params) {
        if (param.equals("name")) {
        } else {
          String selectedTask = (String) request.queryParams((String) param);
          Task task = Task.find(Integer.parseInt(selectedTask));
          newCategory.addTask(task);
        }
      }

      response.redirect("/admin");
      return null;
    });

    post("/add-task", (request, response) -> {

      Task newTask = new Task(request.queryParams("description"));
      newTask.save();

      Object[] params = request.queryParams().toArray();

      for (Object param : params) {
        if (param.equals("description")) {
        } else {
          String selectedCategory = (String) request.queryParams((String) param);
          Category category = Category.find(Integer.parseInt(selectedCategory));
          newTask.addCategory(category);
        }
      }
      response.redirect("/admin");
      return null;
    });

    get("/categories/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Category category = Category.find(Integer.parseInt(request.params(":id")));

      model.put("category", category);
      model.put("template", "templates/category-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Category toBeUpdated = Category.find(Integer.parseInt(request.params(":id")));
      toBeUpdated.update(request.queryParams("name"));

      response.redirect("/");
      return null;
    });

    post("/categories/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Category toBeDeleted = Category.find(Integer.parseInt(request.params(":id")));
      toBeDeleted.delete();

      response.redirect("/");
      return null;
    });

    get("/tasks/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Task task = Task.find(Integer.parseInt(request.params(":id")));

      model.put("task", task);
      model.put("template", "templates/task-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tasks/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Task toBeUpdated = Task.find(Integer.parseInt(request.params(":id")));
      toBeUpdated.update(request.queryParams("description"));

      response.redirect("/");
      return null;
    });

    post("/tasks/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Task toBeDeleted = Task.find(Integer.parseInt(request.params(":id")));
      toBeDeleted.delete();

      response.redirect("/");
      return null;
    });

    post("/tasks/:id/complete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Task toBeDeleted = Task.find(Integer.parseInt(request.params(":id")));
      toBeDeleted.completeTask();

      response.redirect("/");
      return null;
    });

  }//end of main

}//end of app
