package org.jg.agent.view;

import java.io.File;

import org.apache.commons.io.FileUtils;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class ViewMain {

	public void run() throws Exception {
		
		String fileString = FileUtils.readFileToString(new File("test.json"), "UTF-8");
		JsonArray ja = new JsonArray(fileString);

		
		
		for (int i = 0; i < ja.size(); i++) {
			Item item = new Item();
			JsonObject obj = (JsonObject) ja.getList().get(i);
			item.name = obj.getString("name");
			item.path = "/data/"+i;
			
			JsonArray jsonArray = obj.getJsonArray("children", new JsonArray());
			if(!jsonArray.getList().isEmpty()) {
				
			}
			
		}
		
		
		Vertx vertx = Vertx.vertx();
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);

		router.route("/data/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "application/json");
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("text", "hello");
			jsonObject.put("isLazy", true);
			jsonObject.put("isFolder", true);
			
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(jsonObject);
			jsonArray.add(jsonObject);

			
			System.out.println(jsonArray.toString());
			 
			response.putHeader("content-type", "application/json");
			response.end(jsonArray.toString());
			
		});
		

		router.route("/*").handler(StaticHandler.create());

		server.requestHandler(router::accept).listen(8080);

		System.out.println("Server is running!");
		// http://www.easyjstree.com/Demos/LazyLoading
	}

	public static void main(String[] args) throws Exception {

		new ViewMain().run();

	}
}
