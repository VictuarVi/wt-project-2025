package it.polimi.tiw25.pure_html.entities;

/**
 * Class that represents a Playlist.
 */
public record Playlist(
        int id,
        String title,
        java.sql.Date creation_date,
        User user
) {
}
