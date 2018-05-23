package org.jg.agent.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

		final List<Item> items = toItems(ja, "/data/");
		
		Vertx vertx = Vertx.vertx();
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);

		router.route("/data").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "application/json");
			
			JsonArray jsonArray = new JsonArray();
			for (Item item : items) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.put("text", item.name);
				jsonObject.put("isLazy", !item.items.isEmpty());
				jsonObject.put("isFolder", !item.items.isEmpty());		
				jsonObject.put("lazyUrl", item.path);		
				
				jsonArray.add(jsonObject);
			}
			
			response.putHeader("content-type", "application/json");
			response.end(jsonArray.toString());
			
		});
		
		router.route("/data/:path").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "application/json");
			
			String param = routingContext.request().getParam("path");
			System.out.println(param);
			
			String[] pathIds = param.split("_");
			List<Item> searchItems = items;
			for (int i = 0; i < pathIds.length; i++) {
				String s = pathIds[i];
				Item item = searchItems.get(Integer.parseInt(s));
				searchItems = item.items;
			}
			
			JsonArray jsonArray = new JsonArray();
			for (Item item : searchItems) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.put("text", item.name);
				jsonObject.put("isLazy", !item.items.isEmpty());
				jsonObject.put("isFolder", !item.items.isEmpty());		
				jsonObject.put("lazyUrl", item.path);		

				jsonArray.add(jsonObject);
			}
			
			 
			response.putHeader("content-type", "application/json");
			response.end(jsonArray.toString());
			
		});
		

		router.route("/*").handler(StaticHandler.create());

		server.requestHandler(router::accept).listen(8080);

		System.out.println("Server is running!");
	}

	private List<Item> toItems(JsonArray ja, String path) {
		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < ja.size(); i++) {
			Item item = new Item();
			JsonObject obj = new JsonObject((Map<String, Object>)ja.getList().get(i));
			item.name = obj.getString("name");
			item.path = path + i;
			
			JsonArray jsonArray = obj.getJsonArray("children", new JsonArray(new ArrayList<>()));
			if(jsonArray != null && jsonArray.getList() != null && !jsonArray.getList().isEmpty()) {
				item.items = toItems(jsonArray, item.path + "_");
			}
			items.add(item);
		}
		return items;
	}

	public static void main(String[] args) throws Exception {

		new ViewMain().run();

	}
}
