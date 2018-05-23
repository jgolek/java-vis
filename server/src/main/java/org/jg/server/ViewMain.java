package org.jg.server;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
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

public class ViewMain {

	public void run() throws Exception {

		//String fileString = "";//FileUtils.readFileToString(new File("test.json"), "UTF-8");
		//JsonArray ja = new JsonArray(fileString);

		Yaml yaml = new Yaml();
		Collection<File> threadsInFolder = FileUtils.listFiles(new File("./runtime/"), new String[] {"txt"}, false);
		final List<Item> threadItems = new ArrayList<>();
		int index = 0;
		for (File file : threadsInFolder) {
			System.out.println(file.getName());
			List<String> list = (List<String>) yaml.load(new FileReader(file));
			String path = "/data/thread/"+index;
			final List<Item> items = toItems(list, path + "_");
			Item threadItem = new Item();
			threadItem.items = items;
			threadItem.name = file.getName();
			threadItem.path = path;
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

		router.route("/*").handler(StaticHandler.create());

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
			jsonObject.put("text", item.name);
			jsonObject.put("isLazy", !item.items.isEmpty());
			jsonObject.put("isFolder", !item.items.isEmpty());
			jsonObject.put("lazyUrl", item.path);

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
		new ViewMain().run();
	}
}
