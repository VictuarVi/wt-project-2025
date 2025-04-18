package it.polimi.tiw25.pure_html.DAO;

import it.polimi.tiw25.pure_html.entities.Playlist;
import it.polimi.tiw25.pure_html.entities.Track;
import it.polimi.tiw25.pure_html.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {
    private final Connection connection;

    public PlaylistDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieve Playlists for a given User.
     *
     * @param user User of which to retrieve the playlists
     * @return List of Playlist
     * @throws SQLException
     */
    public List<Playlist> getUserPlaylists(User user) throws SQLException {
        List<Playlist> playlists = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT b.playlist_id, b.playlist_title,b.creation_date FROM user a NATURAL JOIN playlist b WHERE a.nickname = ?");

        preparedStatement.setString(1, user.nickname());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Playlist playlist = new Playlist(
                    resultSet.getString("playlist_title"),
                    resultSet.getDate("creation_date"),
                    user
            );
            playlists.add(playlist);
        }

        return playlists;
    }

    /**
     * Create a new Playlist for a given User.
     *
     * @param playlist Playlist to create
     * @param user User to add the Playlist
     * @throws SQLException
     */
    public void createPlaylist(Playlist playlist, User user) throws SQLException {
    }

    /**
     * Delete a Playlist. Note this will also delete all associated tracks with that playlist.
     *
     * @param playlist
     * @throws SQLException
     */
    public void deletePlaylist(Playlist playlist) throws SQLException {
    }


    /**
     * Adds a new Track to the given Playlist.
     *
     * @param track
     * @param playlist
     * @throws SQLException
     */
    public void addTrackToPlaylist(Track track, Playlist playlist) throws SQLException {

    }

    /**
     * Remove a Track from the given Playlist.
     *
     * @param track
     * @param playlist
     * @throws SQLException
     */
    public void removeTrackFromPlaylist(Track track, Playlist playlist) throws SQLException {
    }
}
