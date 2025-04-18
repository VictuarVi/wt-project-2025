package it.polimi.tiw25.pure_html.DAO;

/**
 * Utility class to manage all queries from the same place.
 */
public class Queries {
    // Checks
    public static final String CHECK_USER = "SELECT * FROM user WHERE nickname = ? AND password = ?";

    // Insertions
    public static final String ADD_USER = "INSERT INTO user (nickname, password, name, surname) VALUES (?, ?, ?, ?)";

    // Data retrieval
    public static final String GET_USER_TRACKS = "SELECT c.title, c.album_title FROM user a natural join user_tracks b natural join track c WHERE a.nickname = ?";

    public static final String GET_USER_PLAYLISTS = "SELECT b.playlist_id, b.playlist_title FROM user a NATURAL JOIN playlist b WHERE a.nickname = ?";

    public static final String GET_PLAYLIST_TRACKS = "SELECT c.track_id, c.title, c.album_title FROM playlist a NATURAL JOIN playlist_tracks b NATURAL JOIN track c WHERE a.playlist_id = ?";
}
