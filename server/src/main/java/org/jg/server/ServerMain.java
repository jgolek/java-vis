package org.jg.server;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class ServerMain {

	public void run() throws Exception {

		//String fileString = "";//FileUtils.readFileToString(new File("test.json"), "UTF-8");
		//JsonArray ja = new JsonArray(fileString);

		Yaml yaml = new Yaml();
		Collection<File> threadsInFolder = FileUtils.listFiles(new File("./runtime/"), new String[] {"yaml"}, false);
		final List<Item> threadItems = new ArrayList<>();
		
		List<File> sortedList = new ArrayList<>(threadsInFolder);
		Collections.sort(sortedList);
		
		
		int index = 0;
		for (File file : sortedList) {
			List<String> list = (List<String>) yaml.load(new FileReader(file));
			String path = "/data/thread/"+index;
			//test
			final List<Item> items = toItems(list, path + "_");

			Item threadItem = new Item();
			String[] nameParts = file.getName().split("_thread_");
			threadItem.time = nameParts[0];
			threadItem.thread = nameParts[1].replace(".yaml", "");
			
			if(false && items.size() == 0) {
				Item item = items.get(0);
				threadItem.name = item.name;
				threadItem.path = item.path;
				threadItem.items = item.items;
			}else {
				threadItem.items = items;
				threadItem.name = "Thread.run()";
				threadItem.path = path;
			}
			
			index++;
			threadItems.add(threadItem);
		}

		Vertx vertx = Vertx.vertx();
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);

		router.route("/data").handler(routingContext -> {

			JsonArray jsonArray = toJsonArray(threadItems);

			sendJsonResponseToClient(routingContext, jsonArray);
		});

		router.route("/data/thread/:path").handler(routingContext -> {

			String param = routingContext.request().getParam("path");

			String[] pathIds = param.split("_");
			List<Item> searchItems = threadItems;
			for (int i = 0; i < pathIds.length; i++) {
				String s = pathIds[i];
				Item item = searchItems.get(Integer.parseInt(s));
				searchItems = item.items;
			}

			JsonArray jsonArray = toJsonArray(searchItems);

			sendJsonResponseToClient(routingContext, jsonArray);

		});

		router.route("/*").handler(
				StaticHandler.create()
					.setCachingEnabled(false)
					.setMaxAgeSeconds(1));
		

		server.requestHandler(router::accept).listen(8080);

		System.out.println("Server is running!");
	}

	private void sendJsonResponseToClient(RoutingContext routingContext, JsonArray jsonArray) {
		HttpServerResponse response = routingContext.response();
		response.putHeader("content-type", "application/json");		
		response.end(jsonArray.toString());
	}

	private JsonArray toJsonArray(List<Item> searchItems) {
		JsonArray jsonArray = new JsonArray();
		for (Item item : searchItems) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("title", item.name);
			jsonObject.put("lazy", !item.items.isEmpty());
			jsonObject.put("folder", !item.items.isEmpty());
			jsonObject.put("url", item.path);
			jsonObject.put("thread", item.thread);
			jsonObject.put("time", item.time);


			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	private List<Item> toItems(List ja, String path) {
		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < ja.size(); i++) {
			Item item = new Item();
			Map itemValue = (Map) ja.get(i);
			item.name = (String) itemValue.get("name");
			item.path = path + i;

			List subList = (List) itemValue.get("children");
			if (subList != null && subList != null && !subList.isEmpty()) {
				item.items = toItems(subList, item.path + "_");
			}
			items.add(item);
		}
		return items;
	}

	public static void main(String[] args) throws Exception {
		new ServerMain().run();
	}
}
