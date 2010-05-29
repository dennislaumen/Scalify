package nl.dennislaumen.scalify

import java.net.URL
import xml.XML

class Album private(val uri: String, val name: String, val artist: Artist, val released: Int, val ids: Set[ID],
                    val tracks: Set[Track]) {
  private[scalify] def this(uri: String, name: String, artist: Artist, released: Int, ids: Set[ID]) = {
    this (uri, name, artist, released, ids, null)
  }

  private[scalify] def this(uri: String, name: String, artist: Artist) = {
    this (uri, name, artist, 0, null)
  }

  private[scalify] def this(uri: String, name: String) = {
    this (uri, name, null)
  }

  override def toString(): String = name
}

object Album {
  def lookup(uri: String): Album = {
    val albumXML = XML.load(new URL("http://ws.spotify.com/lookup/1/?uri=" + uri))

    val name = (albumXML \ "name").text

    val artistXML = albumXML \ "artist"
    val artist = new Artist((artistXML \ "@href").text, (artistXML \ "name").text)

    val released = (albumXML \ "released").text.toInt

    val ids = (Set[ID]() /: (albumXML) \ "id") {
      (set, idNode) =>
        val idType = (idNode \ "@type").text
        val id = idNode.text

        set + new ID(idType, id)
    }

    new Album(uri, name, artist, released, ids)
  }

  def lookupWithTracks(uri: String): Album = {
    val albumXML = XML.load(new URL("http://ws.spotify.com/lookup/1/?uri=" + uri + "&extras=track"))

    val name = (albumXML \ "name").text

    val artistXML = albumXML \ "artist"
    val artist = new Artist((artistXML \ "@href").text, (artistXML \ "name").text)

    val released = (albumXML \ "released").text.toInt

    val ids = (Set[ID]() /: (albumXML) \ "id") {
      (set, idNode) =>
        val idType = (idNode \ "@type").text
        val id = idNode.text

        set + new ID(idType, id)
    }

    val tracks = (Set[Track]() /: (albumXML) \\ "track") {
      (set, trackNode) =>
        val uri = (trackNode \ "@href").text
        val name = (trackNode \ "name").text

        val artistNode = trackNode \ "artist"
        val artist = new Artist((artistNode \ "@href").text, artistNode.text)

        set + new Track(uri, name, artist)
    }

    new Album(uri, name, artist, released, ids, tracks)
  }

  def lookupWithDetailedTracks(uri: String): Album = {
    val albumXML = XML.load(new URL("http://ws.spotify.com/lookup/1/?uri=" + uri + "&extras=trackdetail"))

    val name = (albumXML \ "name").text

    val artistXML = albumXML \ "artist"
    val artist = new Artist((artistXML \ "@href").text, (artistXML \ "name").text)

    val released = (albumXML \ "released").text.toInt

    val ids = (Set[ID]() /: (albumXML) \ "id") {
      (set, idNode) =>
        val idType = (idNode \ "@type").text
        val id = idNode.text

        set + new ID(idType, id)
    }

    val tracks = (Set[Track]() /: (albumXML) \\ "track") {
      (set, trackNode) =>
        val uri = (trackNode \ "@href").text
        val name = (trackNode \ "name").text

        val artistNode = trackNode \ "artist"
        val artist = new Artist((artistNode \ "@href").text, artistNode.text)

        val ids = (Set[ID]() /: (albumXML) \ "id") {
          (set, idNode) =>
            val idType = (idNode \ "@type").text
            val id = idNode.text

            set + new ID(idType, id)
        }

        var discNumber = 0

        try {
          discNumber = (albumXML \ "disc-number").text.toInt
        } catch {
          case ex: NumberFormatException => //println("No disc number found.")
        }
        
        val trackNumber = (trackNode \ "track-number").text.toInt
        val length = (trackNode \ "length").text.toFloat
        val popularity = (trackNode \ "popularity").text.toFloat

        set + new Track(uri, name, artist, ids, discNumber, trackNumber, length, popularity)
    }

    new Album(uri, name, artist, released, ids, tracks)
  }
}