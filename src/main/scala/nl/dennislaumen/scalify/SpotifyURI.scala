package nl.dennislaumen.scalify

/**
 * Created by IntelliJ IDEA.
 * User: dennislaumen
 * Date: May 30, 2010
 * Time: 1:37:51 PM
 * To change this template use File | Settings | File Templates.
 */

object SpotifyURLBuilder {
  private var baseURL = "http://ws.spotify.com/"
  private val version = "1/"
  private val lookupService = "lookup/"
  private val uriParameter = "?uri="
  private val extrasParameter = "extras="

  /**def lookupServiceURL(spotifyURI: String, extras: Array[String]) = URL {
    val urlBuilder = new StringBuilder(baseURL).append(lookupService).append(version).append(uriParameter)

    if (spotifyURI.startsWith("spotify:artist:") || spotifyURI.startsWith("spotify:album:") ||
        spotifyURI.startsWith("spotify:track"))


    baseURL + lookupService + version +
  }**/

}
