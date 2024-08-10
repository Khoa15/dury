package com.example.dury.Model;

public class SessionManager {

    private static SessionManager instance;

    private Session currentSession;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void createSession(User user) {
        // Create a new session object and store it in currentSession
        currentSession = new Session(user);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void destroySession() {
        // Destroy the current session and remove it from storage
        currentSession = null;
    }

    public boolean isLoggedIn() {
        return currentSession != null;
    }

    public void saveUser(User user) {
        if (currentSession != null) {
            currentSession.setUser(user);
        }
    }

    public void logoutUser() {
        destroySession();
    }
}