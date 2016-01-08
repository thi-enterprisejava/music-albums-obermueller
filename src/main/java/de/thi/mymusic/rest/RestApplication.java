package de.thi.mymusic.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Activate REST API under url extension /api
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
}
