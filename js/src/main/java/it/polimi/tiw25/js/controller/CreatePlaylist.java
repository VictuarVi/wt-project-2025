package it.polimi.tiw25.js.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw25.js.DAO.PlaylistDAO;
import it.polimi.tiw25.js.entities.Playlist;
import it.polimi.tiw25.js.entities.User;
import it.polimi.tiw25.js.utils.ConnectionHandler;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CreatePlaylist")
@MultipartConfig
public class CreatePlaylist extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    User user;

    public void init() throws ServletException {
        ServletContext context = getServletContext();
        connection = ConnectionHandler.openConnection(context);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PlaylistDAO playlistDAO = new PlaylistDAO(connection);
        user = (User) req.getSession().getAttribute("user");

        String playlistTitle = req.getParameter("playlistTitle");
        Playlist playlist = new Playlist(0, playlistTitle, null, user);

        String[] selectedTracksStringIds = req.getParameterValues("selectedTracks");
        List<Integer> selectedTracksIds = new ArrayList<>();
        if (selectedTracksStringIds != null) {
            for (String id : selectedTracksStringIds) {
                selectedTracksIds.add(Integer.parseInt(id));
            }
        }

        if (playlistTitle == null || playlistTitle.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Playlist title not valid");
            return;
        }
        try {
            Integer playlistId = null;
            playlistId = playlistDAO.createPlaylist(playlist);
            if (!selectedTracksIds.isEmpty())
                playlistDAO.addTracksToPlaylist(selectedTracksIds, playlistId);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(new Playlist(playlistId, playlist.title(), null, null));
            resp.setContentType("application/json");
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLIntegrityConstraintViolationException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.setContentType("text/plain");
            resp.getWriter().write("Error: duplicate playlist");
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while creating playlist");
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        ConnectionHandler.closeConnection(connection);
    }
}