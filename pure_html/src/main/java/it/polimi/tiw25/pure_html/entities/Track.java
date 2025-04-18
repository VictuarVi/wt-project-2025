package it.polimi.tiw25.pure_html.entities;

/**
 * Class that represents a single track.
 */
public record Track(
        String title,
        String artist,
        String album_title,
        int album_release_date,
        int album_image_path,
        int track_path
) {
}
