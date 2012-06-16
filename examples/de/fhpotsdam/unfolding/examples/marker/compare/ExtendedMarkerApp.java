package de.fhpotsdam.unfolding.examples.marker.compare;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays various marker to compare features and behaviors of the different display methods.
 * 
 */
@SuppressWarnings("serial")
public class ExtendedMarkerApp extends PApplet {

	// maps
	List<Map> mapsWithInner = new ArrayList<Map>();
	List<Map> mapsWithOuter = new ArrayList<Map>();

	// locations for test marker
	Location usaCenter = new Location(39.828175f, -98.5795f);
	Location pointLocation = new Location(41.843376f, -87.583008f); // Chicago
	Location areaLocation1 = new Location(22.8450f, -109.95117f);
	Location areaLocation2 = new Location(25.8869f, -97.07519f);
	Location areaLocation3 = new Location(32.4801f, -117.15820f);

	// Marker
	SimpleMarker pointMarker;
	SimplePolygonMarker areaMarker;
	InnerLabelMarker labelMarker;
	OuterPointMarker outerPointMarker;
	OuterPolygonMarker outerAreaMarker;
	OuterLabelMarker outerLabelMarker;

	public void setup() {
		size(1280, 768, GLConstants.GLGRAPHICS);

		mapsWithInner = createMaps(50, 20, 170, 170, 10, 10);
		mapsWithOuter = createMaps(650, 20, 170, 170, 10, 10);
		MapUtils.createDefaultEventDispatcher(this, mapsWithInner);

		// inner marker
		pointMarker = new SimpleMarker(pointLocation);
		pointMarker.setRadius(5);
		areaMarker = new SimplePolygonMarker();
		areaMarker.addLocations(areaLocation1, areaLocation2, areaLocation3);
		labelMarker = new InnerLabelMarker(pointLocation);
		addMarkerToMaps(mapsWithInner, pointMarker, areaMarker, labelMarker);

		// outer marker
		outerPointMarker = new OuterPointMarker(pointLocation);
		outerPointMarker.setRadius(20);
		outerAreaMarker = new OuterPolygonMarker();
		outerAreaMarker.addLocations(areaLocation1, areaLocation2, areaLocation3);
		outerLabelMarker = new OuterLabelMarker(pointLocation);
		addMarkerToMaps(mapsWithOuter, outerPointMarker, outerAreaMarker, outerLabelMarker);
	}

	private void addMarkerToMaps(List<Map> maps, Marker pointMarker, Marker areaMarker, Marker labelMarker) {
		maps.get(0).addMarkers(pointMarker);
		maps.get(1).addMarkers(areaMarker);
		maps.get(2).addMarkers(labelMarker);
		
		maps.get(3).addMarkers(pointMarker);
		maps.get(4).addMarkers(areaMarker);
		maps.get(5).addMarkers(labelMarker);

		maps.get(6).addMarkers(pointMarker);
		maps.get(7).addMarkers(areaMarker);
		maps.get(8).addMarkers(labelMarker);

		maps.get(9).addMarkers(pointMarker);
		maps.get(10).addMarkers(areaMarker);
		maps.get(11).addMarkers(labelMarker);
	}

	public void draw() {
		background(70);

		for (Map map : mapsWithInner) {
			map.draw();
		}
		for (Map map : mapsWithOuter) {
			map.draw();
		}
	}

	public void keyPressed() {
		if (key == ' ') {
			changeMaps(mapsWithInner);
			changeMaps(mapsWithOuter);
		}
	}

	private void changeMaps(List<Map> maps) {
		maps.get(3).zoomAndPanTo(maps.get(3).getCenter(), 3);
		maps.get(4).zoomAndPanTo(maps.get(4).getCenter(), 3);
		maps.get(5).zoomAndPanTo(maps.get(5).getCenter(), 3);

		// outer rotate
		maps.get(6).outerRotate(radians(30));
		maps.get(7).outerRotate(radians(30));
		maps.get(8).outerRotate(radians(30));

		// inner rotate
		Map innerRotateMap1 = maps.get(9);
		Map innerRotateMap2 = maps.get(10);
		Map innerRotateMap3 = maps.get(11);
		innerRotateMap1.mapDisplay.setInnerTransformationCenter(innerRotateMap1.getScreenPosition(innerRotateMap1
				.getCenter()));
		innerRotateMap2.mapDisplay.setInnerTransformationCenter(innerRotateMap2.getScreenPosition(innerRotateMap2
				.getCenter()));
		innerRotateMap3.mapDisplay.setInnerTransformationCenter(innerRotateMap3.getScreenPosition(innerRotateMap3
				.getCenter()));
		innerRotateMap1.rotate(radians(30));
		innerRotateMap2.rotate(radians(30));
		innerRotateMap3.rotate(radians(30));
	}

	public List<Map> createMaps(int xStart, int yStart, int mapWidth, int mapHeight, int xGap, int yGap) {
		List<Map> maps = new ArrayList<Map>();
		for (int y = yStart; y < yStart + (yGap + mapHeight) * 4; y += yGap + mapHeight) {
			for (int x = xStart; x < xStart + (xGap + mapWidth) * 3; x += xGap + mapWidth) {
				Map map = createMap(x, y, mapWidth, mapHeight);
				maps.add(map);
			}
		}
		return maps;
	}

	public Map createMap(int x, int y, int mapWidth, int mapHeight) {
		Map map = new Map(this, x, y, mapWidth, mapHeight);
		map.zoomToLevel(2);
		map.panTo(usaCenter);
		return map;
	}
}
