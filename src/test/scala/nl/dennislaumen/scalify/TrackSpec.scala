package nl.dennislaumen.scalify

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TrackSpec extends FlatSpec with ShouldMatchers {
  "Track" should """ work even when no disc number is available.""" in {
    val rendezVu = Track.lookup("spotify:track:3zBhJBEbDD4a4SO1EaEiBP")
    rendezVu.uri should equal("spotify:track:3zBhJBEbDD4a4SO1EaEiBP")
    rendezVu.name should equal("Rendez-vu")
    rendezVu.artist.uri should equal("spotify:artist:4YrKBkKSVeqDamzBPWVnSJ")
    rendezVu.artist.name should equal("Basement Jaxx")
    rendezVu.album.name should equal("Remedy")
    rendezVu.ids.size should equal(1)
    rendezVu.ids.get("isrc") should equal(Some("GBBKS9900090"))
    rendezVu.discNumber should equal(None)
    rendezVu.trackNumber should equal(1)
    rendezVu.length should equal(345.000000F)
    rendezVu.popularity should equal(0.49336F)
  }

  it should " work with tracks filled with weird characters (UTF-8)." in {
    val godanDaginn = Track.lookup("spotify:track:7K8OV51JY2GyDN2EICwrFX")
    godanDaginn.uri should equal("spotify:track:7K8OV51JY2GyDN2EICwrFX")
    godanDaginn.name should equal("Góðan daginn")
    godanDaginn.artist.uri should equal("spotify:artist:6UUrUCIZtQeOf8tC0WuzRy")
    godanDaginn.artist.name should equal("Sigur Ros ")
    godanDaginn.album.uri should equal("spotify:album:6gAPGWoCZTnIaqB5EMAllD")
    godanDaginn.album.name should equal("Með suð í eyrum við spilum endalaust")
    godanDaginn.ids.size should equal(1)
    godanDaginn.ids.get("isrc") should equal(Some("GBKEE0800078"))
    godanDaginn.discNumber should equal(None)
    godanDaginn.trackNumber should equal(3)
    godanDaginn.length should equal(315.320000)
    godanDaginn.popularity should equal(0.64138)
  }

}