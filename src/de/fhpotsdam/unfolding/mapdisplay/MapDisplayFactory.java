package de.fhpotsdam.unfolding.mapdisplay;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;

@SuppressWarnings("rawtypes")
public class MapDisplayFactory {

	public static final String OPEN_GL_CLASSNAME = "processing.opengl.PGraphicsOpenGL";
	public static final String GLGRAPHICS_CLASSNAME = "codeanticode.glgraphics.GLGraphics";

	public static final boolean DEFAULT_USE_MASK = true;
	public static final boolean DEFAULT_USE_DISTORTION = false;

	public static final String OSM_API_KEY = "607e6483654b5c47b9056791d607ab74";
	public static final int OSM_STYLE_ID = 65678; // test: 69960; // original: 998

	public static Logger log = Logger.getLogger(MapDisplayFactory.class);

	public static AbstractMapDisplay getMapDisplay(PApplet p, String id, float x, float y, float width, float height,
			AbstractMapProvider provider, UnfoldingMap map) {
		return getMapDisplay(p, id, x, y, width, height, DEFAULT_USE_MASK, DEFAULT_USE_DISTORTION, provider, map);
	}

	public static AbstractMapDisplay getMapDisplay(PApplet p, String id, float x, float y, float width, float height,
			boolean useMask, boolean useDistortion, AbstractMapProvider provider, UnfoldingMap map) {

		AbstractMapDisplay mapDisplay = null;

		if (provider == null) {
			provider = getDefaultProvider();
		}

		if (useMask) {
			try {
				Class glGraphicsClass = Class.forName(GLGRAPHICS_CLASSNAME);
				if (glGraphicsClass.isInstance(p.g)) {
					if (useDistortion) {
						log.debug("Using DistortedGLGraphicsMapDisplay for '" + id + "'");
						mapDisplay = new DistortedGLGraphicsMapDisplay(p, provider, x, y, width, height);
					} else {
						log.debug("Using GLGraphicsMapDisplay for '" + id + "'");
						// TODO @chris: Why always use MaskedGLGraphicsMD?
						// mapDisplay = new MaskedGLGraphicsMapDisplay(p, provider, x, y, width, height);
						mapDisplay = new GLGraphicsMapDisplay(p, provider, x, y, width, height);
					}
				}
			} catch (ClassNotFoundException e) {
				// GLGraphics not found, go for Processing default
			}

			if (mapDisplay == null) {
				try {
					Class openGLClass = Class.forName(OPEN_GL_CLASSNAME);
					if (openGLClass.isInstance(p.g)) {
						log.warn("No OpenGL mapDisplay available. Use GLGraphics or P3D. '" + id + "'");
					}
				} catch (ClassNotFoundException e) {
					// OpenGL not found, was for informational purposes anyway. 
				}

				log.debug("Using MaskedPGraphicsMapDisplay for '" + id + "'");
				log.warn("no rotation possible (without OpenGL)");
				mapDisplay = new MaskedPGraphicsMapDisplay(p, provider, x, y, width, height);
			}

		} else {
			PApplet.println("Using ProcessingMapDisplay");
			mapDisplay = new ProcessingMapDisplay(p, provider, x, y, width, height);
		}

		mapDisplay.createDefaultMarkerManager(map);
		return mapDisplay;
	}

	public static AbstractMapProvider getDefaultProvider() {
		return new OpenStreetMap.CloudmadeProvider(OSM_API_KEY, OSM_STYLE_ID);
	}
}
