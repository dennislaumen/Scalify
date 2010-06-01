package nl.dennislaumen.scalify

import java.net.URL
import xml.XML

class Track private[scalify] (val uri: String, val name: String, val artist: Artist, val album: Album, val ids: Map[String, String],
    val discNumber: Option[Int], val trackNumber: Int, val length: Float, val popularity: Float) {

  private[scalify] def this(uri: String, name: String, artist: Artist) = {
    this (uri, name, artist, null, null, None, 0, 0F, 0F)
  }

  private[scalify] def this(uri: String, name: String, artist: Artist, ids: Map[String, String], discNumber: Option[Int], trackNumber: Int,
    length: Float, popularity: Float) = {
    this (uri, name, artist, null, ids, discNumber, trackNumber, length, popularity)
  }

  override def toString(): String = name
}

object Track {  
  def lookup(uri: String): Track = {
    val trackXML = XML.load(new URL("http://ws.spotify.com/lookup/1/?uri=" + uri))

    val name = (trackXML \ "name").text

    val artistXML = trackXML \ "artist"
    val artist = new Artist((artistXML \ "@href").text, (artistXML \ "name").text)

    val albumXML = trackXML \ "album"
    val album = new Album((albumXML \ "@href").text, (albumXML \ "name").text)

    val ids = (Map[String, String]() /: (trackXML) \ "id") { (map, idNode) =>
      map((idNode \ "@type").text) = idNode.text
    }

    val discNumber =
      if ((trackXML \ "disc-number").text.isEmpty)
        None
      else
        Some((trackXML \ "disc-number").text.toInt)

    val trackNumber = (trackXML \ "track-number").text.toInt
    val length = (trackXML \ "length").text.toFloat
    val popularity = (trackXML \ "popularity").text.toFloat

    new Track(uri, name, artist, album, ids, discNumber, trackNumber, length, popularity)
  }
}

