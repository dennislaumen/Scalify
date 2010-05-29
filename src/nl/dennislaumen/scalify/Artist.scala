package nl.dennislaumen.scalify

import xml.XML
import java.net.URL

class Artist private[scalify] (val uri: String, val name: String, val albums: Set[Album]) {

  private[scalify] def this(uri: String, name: String) = {
    this(uri, name, null)
  }

  override def toString(): String = name
}

object Artist {
  def lookup(uri: String): Artist = {
    val lookupURL = new URL("http://ws.spotify.com/lookup/1/?uri=" + uri)
    val artistXML = XML.load(lookupURL)

    new Artist(uri, (artistXML \ "name").text)
  }

  def lookupWithAlbums(uri: String): Artist = {
    val lookupURL = new URL("http://ws.spotify.com/lookup/1/?uri=" + uri + "&extras=album")
    val artistXML = XML.load(lookupURL)

    val name = (artistXML \ "name").text

    val albums = (Set[Album]() /: (artistXML) \\ "album") {
      (set, albumNode) =>
        val uri = (albumNode \ "@href").text
        val name = (albumNode \ "name").text

        val artistNode = albumNode \ "artist"
        val artist = new Artist((artistNode \ "@href").text, artistNode.text)

        set + new Album(uri, name, artist)
    }

    new Artist(uri, name, albums)
  }

  def lookupWithDetailedAlbums(uri: String): Artist = {
    val lookupURL = new URL("http://ws.spotify.com/lookup/1/?uri=" + uri + "&extras=albumdetail")
    val artistXML = XML.load(lookupURL)

    val name = (artistXML \ "name").text

    val albums = (Set[Album]() /: (artistXML) \\ "album") {
      (set, albumNode) =>
        val uri = (albumNode \ "@href").text
        val name = (albumNode \ "name").text

        val artistNode = albumNode \ "artist"
        val artist = new Artist((artistNode \ "@href").text, artistNode.text)

        val released = (albumNode \ "released").text.toInt

        val ids = (Set[ID]() /: (albumNode) \ "id") {
          (set, idNode) =>
            val idType = (idNode \ "@type").text
            val id = idNode.text

            set + new ID(idType, id)
        }

        set + new Album(uri, name, artist, released, ids)
    }

    new Artist(uri, name, albums)
  }
}