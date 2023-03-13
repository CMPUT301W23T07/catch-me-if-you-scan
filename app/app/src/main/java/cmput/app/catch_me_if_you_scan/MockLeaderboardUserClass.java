package cmput.app.catch_me_if_you_scan;

public class MockLeaderboardUserClass {

    public String user;
    public String subtitle;
    public String position;


    /**
     * Constructs a new MockLeaderboardUserClass object with the given user name, subtitle, and position.
     *
     * @param user      the user's name.
     * @param subtitle  the subtitle for the user's entry in the leaderboard.
     * @param position  the user's position in the leaderboard.
     */
    public MockLeaderboardUserClass(String user, String subtitle, String position) {
        this.user = user;
        this.subtitle = subtitle;
        this.position = position;
    }
}
