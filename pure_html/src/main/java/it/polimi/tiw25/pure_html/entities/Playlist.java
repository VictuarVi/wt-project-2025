package it.polimi.tiw25.pure_html.entities;

/**
 * Class that represents a Playlist.
 */
public record Playlist(
        String title,
        int creation_date,
        User user
) {
}
