package it.polimi.tiw25.pure_html.entities;

/**
 * Class that represents a single track.
 */
public record Track(
        String title,
        String artist,
        java.sql.Date albumReleaseDate,
        String album_title,
        String genre,
        String album_image_path,
        String track_path
) {
}
