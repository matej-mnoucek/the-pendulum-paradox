package com.thependulumparadox.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.ScoreSubmissionData;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.thependulumparadox.control.IMoveCommands;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * Android application wrapper which interfaces Google Play Game services
 */
public class MultiPlayerApplication extends AndroidApplication implements ISynchronization
{
    final static String TAG = "Pendulum Paradox";

    // Request code for waiting room
    final static int RC_WAITING_ROOM = 10002;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;

    // Client used to interact with the real time multiplayer system.
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;

    // Client used for high scores
    private LeaderboardsClient mLeaderboardClient = null;

    private IMoveCommands inputHandler;

    public Event<EventArgs> startMultiplayerEvent = new Event<>();
    public Event<EventArgs> stopMultiplayerEvent = new Event<>();
    public Event<EventArgs> playerDeathEvent = new Event<>();
    public Event<EventArgs> userLoggedIn = new Event<>();


    // Room ID where the currently active game is taking place, null if we're not playing.
    String mRoomId = null;
    boolean roomFull = false;

    // Holds the configuration of the current room
    RoomConfig mRoomConfig;


    // Holds the top 10 players on the leaderboard
    String leaderboard = "";

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = new ArrayList<>();

    // My participant ID in the currently active game
    String mMyId = null;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[100];

    // Queue for actions
    Queue actionQueue = new Queue();

    // Simply player ID
    private String mPlayerId;

    // The currently signed in account
    GoogleSignInAccount mSignedInAccount = null;


    /*
     * API INTEGRATION SECTION. This section contains the code that integrates
     * the game with the Google Play game services API.
     */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;


        System.out.println("Android launcher starts initialize...");
        initialize(new PendulumParadoxGame(this), config);

        mGoogleSignInClient = GoogleSignIn
                .getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    }


    public boolean isUserSignedIn(){
        return (mSignedInAccount != null);
    }

    public void setInputHandler(IMoveCommands inputHandler){
        this.inputHandler = inputHandler;
    }

    public Event getStartMultiplayerEvent(){
        return startMultiplayerEvent;
    }

    public Event getStopMultiplayerEvent(){
        return stopMultiplayerEvent;
    }

    public Event getPlayerDeathEvent(){
        return playerDeathEvent;
    }

    public Event getUserLoggedInEvent() { return userLoggedIn; }

    public void submitScore(int score)
    {
        if (isUserSignedIn())
        {
            mLeaderboardClient = Games.getLeaderboardsClient(this,
                    GoogleSignIn.getLastSignedInAccount(this));

            mLeaderboardClient.submitScoreImmediate("CgkI--f1q4ANEAIQBA", score)
                   .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>()
                      {
                          @Override
                          public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                              if (task.isSuccessful()) {
                              } else {
                                  handleException(task.getException(), "");
                              }
                          }
                      });
        }
    }

    public void UpdateHighscore(){

        if(isUserSignedIn())
        {
            mLeaderboardClient = Games.getLeaderboardsClient(this, mSignedInAccount);
            mLeaderboardClient.loadTopScores("CgkI--f1q4ANEAIQBA",
                    LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC, 10, true)
                    .addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardsClient.LeaderboardScores>>()
                    {
                        @Override
                        public void onSuccess(AnnotatedData<LeaderboardsClient.LeaderboardScores> leaderboardScores)
                        {
                            LeaderboardScoreBuffer scores = leaderboardScores.get().getScores();

                            String str = "";
                            for (int i = 0; i < scores.getCount(); i++)
                            {
                                LeaderboardScore score = scores.get(i);
                                String name = score.getScoreHolderDisplayName();
                                String points = score.getDisplayScore();
                                str += name + ":" + points + "!";
                            }

                            if (str != "")
                            {
                                leaderboard = str;
                            }
                        }
                    });
        }
    }


    public String getHighscore()
    {
        UpdateHighscore();
        return this.leaderboard;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        signInSilently();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void startQuickGame()
    {
        // Quick-start a game with 1 randomly selected opponent
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);

        mRoomConfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();

        mRealTimeMultiplayerClient.create(mRoomConfig).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (!task.isSuccessful())
                {
                    handleException(task.getException(), "");
                }
            }
        });
    }

    @Override
    public boolean isGameInProgress(){
        return roomFull;
    }

    @Override
    public void sendAction(String action, Vector2 pos)
    {
        if (mParticipants.size() == 0)
        {
            return;
        }

        for (Participant p : mParticipants)
        {
            if (p.getParticipantId().equals(mMyId))
            {
                continue;
            }

            if (pos != null)
            {
                action += pos.toString();
            }
            sendUnreliableMessage(p , action);
        }
    }

    @Override
    public void synchronize()
    {
        while(!actionQueue.isEmpty())
        {
            String curraction = (String)actionQueue.removeFirst();
            Vector2 position = null;

            if (curraction.contains("("))
            {
                String[] str = curraction.replace("(","/").split("/");

                curraction = str[0];
                String[] pos = str[1].replace(")","").split(",");
                position = new Vector2(Float.parseFloat(pos[0]),Float.parseFloat(pos[1]));
            }

            System.out.println("Executing action...");

            switch (curraction)
            {
                case "L":
                    // Execute move left
                    inputHandler.moveLeft();
                    break;
                case "SL":
                    // Stop move left
                    inputHandler.stopMoveLeft(position);
                    break;
                case "R":
                    // Execute move right
                    inputHandler.moveRight();
                    break;
                case "SR":
                    // Stop move right
                    inputHandler.stopMoveRight(position);
                    break;
                case "J":
                    // Execute jump
                    inputHandler.jump();
                    break;
                case "SJ":
                    // Stop execute jump
                    inputHandler.stopJump();
                    break;
                case "S":
                    // Start shooting
                    inputHandler.startShooting(position);
                    break;
                case "SS":
                    // Stop shooting
                    inputHandler.stopShooting();
                    break;
                case "D":
                    // Player death
                    playerDeathEvent.invoke(null);
                    break;
                default:
                    // Error
                    break;
            }
        }
    }

    public void startSignInIntent()
    {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    public void signInSilently()
    {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>()
                {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task)
                    {
                        if (task.isSuccessful())
                        {
                            onConnected(task.getResult());
                            UpdateHighscore();
                        }
                        else
                        {
                            onDisconnected();
                        }
                    }
                });
    }


    public void signOut()
    {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (!task.isSuccessful())
                        {
                            handleException(task.getException(), "signOut() failed!");
                        }

                        onDisconnected();
                    }
                });
    }

    private void handleException(Exception exception, String details)
    {
        int status = 0;

        if (exception instanceof ApiException)
        {
            ApiException apiException = (ApiException) exception;
            status = apiException.getStatusCode();
        }

        String errorString = null;
        switch (status)
        {
            case GamesCallbackStatusCodes.OK:
                break;
            case GamesClientStatusCodes.SIGN_IN_REQUIRED:
                errorString = "you need to log in for this!";
                break;
            case GamesClientStatusCodes.NETWORK_ERROR:
                errorString = "there was a network error!";
                break;
            default:
                break;
        }

        if (errorString == null)
        {
            return;
        }

        new AlertDialog.Builder(MultiPlayerApplication.this)
                .setTitle("Oops!")
                .setMessage("Looks like "  + errorString)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);

            }
            catch (ApiException apiException)
            {
                handleException(apiException,"");
                onDisconnected();
            }

        }
        else if (requestCode == RC_WAITING_ROOM)
        {
            // We got the result from the "waiting room" UI.
            if (resultCode == Activity.RESULT_OK)
            {
                // Ready to start playing
            }
            else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM)
            {
                // Player indicated that they want to leave the room
                leaveRoom();
            }
            else if (resultCode == Activity.RESULT_CANCELED)
            {
                // Dialog was cancelled (user pressed back key, for instance). In our game,
                // this means leaving the room too. In more elaborate games, this could mean
                // something else (like minimizing the waiting room UI).
                leaveRoom();
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop()
    {
        // If we're in a room, leave it.
        leaveRoom();
        super.onStop();
    }

    // Handle back key to make sure we cleanly leave a game if we are in the middle of one
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e)
    {
        return super.onKeyDown(keyCode, e);
    }

    // Leave the room
    void leaveRoom()
    {
        if (mRoomId != null)
        {
            mRealTimeMultiplayerClient.leave(mRoomConfig, mRoomId)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            mRoomId = null;
                            mRoomConfig = null;
                            stopMultiplayerEvent.invoke(null);
                        }
                    });
        }
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    void showWaitingRoom(Room room)
    {
        // Minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        mRealTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent)
                    {
                        // Show waiting room UI
                        startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                });
    }


    /*
     * CALLBACKS SECTION. This section shows how we implement the several games
     * API callbacks.
     */

    private void onConnected(GoogleSignInAccount googleSignInAccount)
    {
        if (mSignedInAccount != googleSignInAccount)
        {
            mSignedInAccount = googleSignInAccount;

            // Update the clients
            mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);

            // Get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>()
                    {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();

                        }
                    });

            UpdateHighscore();
            userLoggedIn.invoke(null);
        }
    }


    public void onDisconnected()
    {
        mRealTimeMultiplayerClient = null;
    }

    private RoomStatusUpdateCallback mRoomStatusUpdateCallback = new RoomStatusUpdateCallback()
    {
        @Override
        public void onConnectedToRoom(Room room)
        {
            // Get participants and my ID:
            mParticipants = room.getParticipants();
            mMyId = room.getParticipantId(mPlayerId);

            // Save room ID if its not initialized in onRoomCreated()
            // so we can leave cleanly before the game starts
            if (mRoomId == null)
            {
                mRoomId = room.getRoomId();
            }
        }

        @Override
        public void onDisconnectedFromRoom(Room room)
        {
            mRoomId = null;
            mRoomConfig = null;
            showGameError();
        }

        @Override
        public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerInvitedToRoom(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onP2PDisconnected(@NonNull String participant) {
            roomFull = false;
        }

        @Override
        public void onP2PConnected(@NonNull String participant) {
            roomFull = true;
        }

        @Override
        public void onPeerJoined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerLeft(Room room, @NonNull List<String> peersWhoLeft) {
            updateRoom(room);
        }

        @Override
        public void onRoomAutoMatching(Room room) {
            updateRoom(room);
        }

        @Override
        public void onRoomConnecting(Room room) {
            updateRoom(room);
        }

        @Override
        public void onPeersConnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }

        @Override
        public void onPeersDisconnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }
    };

    // Show error message about game being cancelled and return to main screen.
    void showGameError()
    {
        new AlertDialog.Builder(this)
                .setMessage("oops")
                .setNeutralButton(android.R.string.ok, null).create();
    }

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback()
    {
        // Called when room has been created
        @Override
        public void onRoomCreated(int statusCode, Room room) {
            if (statusCode != GamesCallbackStatusCodes.OK) {
                showGameError();
                return;
            }

            // Save room ID so we can leave cleanly before the game starts
            mRoomId = room.getRoomId();

            // Show the waiting room UI
            showWaitingRoom(room);
        }

        // Called when room is fully connected.
        @Override
        public void onRoomConnected(int statusCode, Room room)
        {
            if (statusCode != GamesCallbackStatusCodes.OK)
            {
                showGameError();
                return;
            }

            startMultiplayerEvent.invoke(null);
            updateRoom(room);
        }

        @Override
        public void onJoinedRoom(int statusCode, Room room)
        {
            if (statusCode != GamesCallbackStatusCodes.OK)
            {
                showGameError();
                return;
            }

            showWaitingRoom(room);
        }

        @Override
        public void onLeftRoom(int statusCode, @NonNull String roomId)
        {
            mParticipants = new ArrayList<>();
        }
    };

    void updateRoom(Room room)
    {
        if (room != null)
        {
            mParticipants = room.getParticipants();
        }
    }


    /*
     * COMMUNICATIONS SECTION.
     */


    // Called when we receive a real-time message from the network.
    OnRealTimeMessageReceivedListener mOnRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener()
    {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage)
        {
            byte[] buf = realTimeMessage.getMessageData();
            String sender = realTimeMessage.getSenderParticipantId();
            String msg = new String(buf);

            actionQueue.addLast(msg);
        }
    };

    void sendReliableMessage(Participant p, String msg)
    {
        mMsgBuf = msg.getBytes();
        mRealTimeMultiplayerClient.sendReliableMessage(mMsgBuf,
                mRoomId, p.getParticipantId(), new RealTimeMultiplayerClient.ReliableMessageSentCallback()
                {
                    @Override
                    public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId)
                    {
                        // Nothing needed
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Integer>()
                {
                    @Override
                    public void onSuccess(Integer tokenId)
                    {
                        // Nothing needed
                    }
                });
    }

    void sendUnreliableMessage(Participant p, String msg)
    {
        mMsgBuf = msg.getBytes();
        mRealTimeMultiplayerClient.sendUnreliableMessage(mMsgBuf, mRoomId,
                p.getParticipantId());
    }
}
