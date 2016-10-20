package com.dreamforce.demo.composite;
import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;

import com.dreamforce.demo.composite.impl.CompositeExecutor;
import com.dreamforce.demo.composite.impl.SpeedComparisonScript;
import com.dreamforce.demo.composite.impl.TransactionalityScript;
import com.dreamforce.demo.composite.impl.TraditionalExecutor;
import com.dreamforce.demo.composite.utils.DemoUtils;

public class Main {

	public static void main(String[] args) {

		port(Integer.valueOf(System.getenv("PORT")));
		staticFileLocation("/public");
		Map<String, BlockingQueue<String>> traditionalQueueMap = new ConcurrentHashMap<>();
		Map<String, BlockingQueue<String>> compositeQueueMap = new ConcurrentHashMap<>();
		
		get("/home", (request, response) -> {
			return new ModelAndView(new HashMap<>(), "home/home.ftl");
		}, new FreeMarkerEngine());

		// Demo1******
		get("/traditionalExecutor",
				(request, response) -> {
					String instanceIdentifier = _getInstance(request);
					int size = _getQueryParam(request, "size", 1);
					
					(new Thread(
							new SpeedComparisonScript(
									new TraditionalExecutor(), 
									instanceIdentifier, 
									_getOrCreateQueue(traditionalQueueMap, instanceIdentifier), 
									size)
							)).start();
					return "Initiated";
				});
		get("/compositeExecutor",
				(request, response) -> {
					String instanceIdentifier = _getInstance(request);
					int size = _getQueryParam(request, "size", 1);

					(new Thread(
							new SpeedComparisonScript(
									new CompositeExecutor(), 
									instanceIdentifier, 
									_getOrCreateQueue(compositeQueueMap, instanceIdentifier), 
									size)
							)).start();
					return "Initiated";
				});
		
		get("/compositeResponse",
				(request, response) -> {
					String instanceIdentifier = _getInstance(request);
					BlockingQueue<String> compositeQueue = compositeQueueMap.get(instanceIdentifier);
					if (compositeQueue == null) {
						DemoUtils.debug("Error: Found empty response Queue"); 
						return DemoUtils.WAITING_STRING;
					}
					
					boolean blockingResponse = Boolean.valueOf(_getQueryParam(request, "blocking", "false"));
					return SpeedComparisonScript.returnResponse(compositeQueue, blockingResponse);
				});
		get("/traditionalResponse",
				(request, response) -> {
					String instanceIdentifier = _getInstance(request);
					BlockingQueue<String> traditionalQueue = traditionalQueueMap.get(instanceIdentifier);
					if (traditionalQueue == null) {
						DemoUtils.debug("Error: Found empty response Queue");
						return DemoUtils.WAITING_STRING;
					}
					
					boolean blockingResponse = Boolean.valueOf(_getQueryParam(request, "blocking", "true")); // default is true for this.
					return SpeedComparisonScript.returnResponse(traditionalQueue, blockingResponse);
				});
		
		get("/clear", 
				(request, response) -> {
					String instanceIdentifier = _getInstance(request);
					String url = Optional.ofNullable(request.queryParams("url")).orElse(null);
					
					if (url != null) {
						if (url.contains("traditionalResponse")) {
							_clearMap(traditionalQueueMap, instanceIdentifier, "traditional");
						} else if (url.contains("compositeResponse")) {
							_clearMap(compositeQueueMap, instanceIdentifier, "composite");
						} 
					}
					return DemoUtils.TERMINATION_STRING;
				});
		// END Demo1******
		get("/demo3", (request, response) -> {
			return new ModelAndView(new HashMap<>(), "home/demo3.ftl");
		}, new FreeMarkerEngine());
		
		get("/transactionalityTraditional",
				(request, response) -> {
					String instanceIdentifier = _getInstance(request);
					int size = _getQueryParam(request, "size", 1);
					String accountName = _getQueryParam(request, "accountName", null);
					String contact1Name = _getQueryParam(request, "contact1Name", null);
					String contact2Name = _getQueryParam(request, "contact2Name", null);
					String contact3Name = _getQueryParam(request, "contact3Name", null);
					
					(new Thread(
							new TransactionalityScript(
									new TraditionalExecutor(), 
									instanceIdentifier, 
									_getOrCreateQueue(traditionalQueueMap, instanceIdentifier), 
									size, accountName, contact1Name, contact2Name, contact3Name)
							)).start();
					return "Initiated";
				});
		get("/transactionalityComposite",
				(request, response) -> {
					String instanceIdentifier = _getInstance(request);
					int size = _getQueryParam(request, "size", 1);
					String accountName = _getQueryParam(request, "accountName", null);
					String contact1Name = _getQueryParam(request, "contact1Name", null);
					String contact2Name = _getQueryParam(request, "contact2Name", null);
					String contact3Name = _getQueryParam(request, "contact3Name", null);
					
					(new Thread(
							new TransactionalityScript(
									new CompositeExecutor(), 
									instanceIdentifier, 
									_getOrCreateQueue(compositeQueueMap, instanceIdentifier), 
									size, accountName, contact1Name, contact2Name, contact3Name)
							)).start();
					return "Initiated";
				});
	}
	
	/**
	 * @return the instance identifier of the request or "default" if there was none provided.
	 */
	private static String _getInstance(Request request) {
		return _getQueryParam(request, "instance", "default");
	}
	
	private static String _getQueryParam(Request request, String param, String defaultValue) {
		return Optional.ofNullable(request.queryParams(param)).orElse(defaultValue);
	}
	
	private static int _getQueryParam(Request request, String param, int defaultValue) {
		try {
			int value = Integer.parseInt(request.queryParams(param));
			return value;		
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}
	
	private static <T> BlockingQueue<T> _getOrCreateQueue(Map<String, BlockingQueue<T>> map, String instanceIdentifier) {
		BlockingQueue<T> queue = map.get(instanceIdentifier);
		if (queue == null) { // if there is no queue, create one,.
			queue = new LinkedBlockingQueue<>();
			map.put(instanceIdentifier, queue);
		} else { // if there is a queue, clear it.
			queue.clear();
		}
		return queue;
	}
	
	private static <T> void _clearMap(Map<String, BlockingQueue<T>> map, String instanceIdentifier, String queueName) {
		if (instanceIdentifier.equals("default")) {
			DemoUtils.debug(String.format("Clearing out %s values from queue", map.size()));
			map.clear();
		} else {
			BlockingQueue<T> queue = map.remove(instanceIdentifier);
			DemoUtils.debug(String.format("Removed %s from %s for %s", queue == null ? "nothing" : "1 item", queueName, instanceIdentifier));
		}
	}
}
