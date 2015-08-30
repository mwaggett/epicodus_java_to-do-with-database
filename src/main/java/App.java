import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;

public class App {
  public static void main(String[] args) {
  staticFileLocation("/public");
  String layout = "templates/layout.vtl";

  get("/", (request, response) -> {
    HashMap<String, Object> model = new HashMap<String, Object>();
    model.put("categories", Category.all());
    model.put("template", "templates/index.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  post("/categories", (request, response) -> {
    HashMap<String,Object> model = new HashMap<String, Object>();
    String name = request.queryParams("name");
    Category newCategory = new Category(name);
    newCategory.save();
    model.put("categories", Category.all());
    model.put("template", "templates/index.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  get("/categories/:id", (request, response) -> {
    HashMap<String, Object> model = new HashMap<String, Object>();
    model.put("category", Category.find(Integer.parseInt(request.params(":id"))));
    model.put("template", "templates/category.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());


   post("/categories/:id/delete", (request, response) -> {
    HashMap<String, Object> model = new HashMap<String, Object>();
    Category category = Category.find(Integer.parseInt(request.params(":id")));
    model.put("template", "templates/index.vtl");
    category.delete();
    model.put("categories", Category.all());
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  post("/tasks", (request, response) -> {
    HashMap<String,Object> model = new HashMap<String, Object>();
    String description = request.queryParams("description");
    Task newTask = new Task(description);
    newTask.save();
    //model.put("category", category);
    model.put("categories", Category.all());
    model.put("template", "templates/index.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  get("categories/:id/update", (request, response) -> {
    HashMap<String, Object> model = new HashMap<String, Object>();
    Category category = Category.find(Integer.parseInt(request.params(":id")));
    model.put("category", category);
    model.put("template", "templates/update-form.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  post("categories/:id/update", (request, response) -> {
    HashMap<String, Object> model = new HashMap<String, Object>();
    Category category = Category.find(Integer.parseInt(request.params(":id")));
    String name = request.queryParams("name");
    category.update(name);
    response.redirect("/");
    return null;
  });

  post("/tasks/:id/delete", (request, response) -> {
   HashMap<String, Object> model = new HashMap<String, Object>();
   Task task = Task.find(Integer.parseInt(request.params(":id")));
   model.put("template", "templates/index.vtl");
   task.delete();
   model.put("tasks", Task.all());
   model.put("categories", Category.all());
   return new ModelAndView(model, layout);
 }, new VelocityTemplateEngine());
 //

 get("/tasks/:id/update", (request, response) -> {
   HashMap<String, Object> model = new HashMap<String, Object>();
   Task task = Task.find(Integer.parseInt(request.params(":id")));
   model.put("category", category);
   model.put("task", task); //model.put("task", task) not needed for /delete, must add for /update
   model.put("template", "templates/edit_task.vtl");
   return new ModelAndView(model, layout);
 }, new VelocityTemplateEngine());

 post("/tasks/:id/update", (request, response) -> {
   HashMap<String, Object> model = new HashMap<String, Object>();
   Task task = Task.find(Integer.parseInt(request.params(":id")));
   String description = request.queryParams("description");
   task.update(description);
   response.redirect("/");
   return null;
 });

}//end of main

}//end of app
