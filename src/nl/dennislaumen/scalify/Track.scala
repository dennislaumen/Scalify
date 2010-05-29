package nl.dennislaumen.scalify

import java.net.URL
import xml.XML

class Track private[scalify] (val uri: String, val name: String, val artist: Artist, val album: Album, val ids: Set[ID],
    val discNumber: Int, val trackNumber: Int, val length: Float, val popularity: Float) {

  private[scalify] def this(uri: String, name: String, artist: Artist) = {
    this (uri, name, artist, null, null, 0, 0, 0F, 0F)
  }

  private[scalify] def this(uri: String, name: String, artist: Artist, ids: Set[ID], discNumber: Int, trackNumber: Int,
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

    val ids = (Set[ID]() /: (trackXML) \ "id") { (set, idNode) =>
      val idType = (idNode \ "@type").text
      val id = idNode.text

      set + new ID(idType, id)
    }

    var discNumber = 0
    
    try {
      discNumber = (trackXML \ "disc-number").text.toInt
    } catch {
      case ex: NumberFormatException => //println("No disc number found.")
    }

    val trackNumber = (trackXML \ "track-number").text.toInt
    val length = (trackXML \ "length").text.toFloat
    val popularity = (trackXML \ "popularity").text.toFloat

    new Track(uri, name, artist, album, ids, discNumber, trackNumber, length, popularity)
  }
}

