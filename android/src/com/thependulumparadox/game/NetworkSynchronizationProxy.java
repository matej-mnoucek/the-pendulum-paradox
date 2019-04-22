package com.thependulumparadox.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Queue;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.thependulumparadox.control.MoveCommands;
import com.thependulumparadox.multiplayer.ISynchronization;

import java.util.ArrayList;
import java.util.List;

public class NetworkSynchronizationProxy extends AndroidApplication implements ISynchronization {

    //Based on Buttonclicker tutorial from android developers

    /*
     * API INTEGRATION SECTION. This section contains the code that integrates
     * the game with the Google Play game services API.
     */

    final static String TAG = "Pendulum Paradox";

    final static int RC_WAITING_ROOM = 10002;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;

    // Client used to interact with the real time multiplayer system.
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;

    private MoveCommands InputHandler;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;
    boolean roomFull = false;

    // Holds the configuration of the current room.
    RoomConfig mRoomConfig;


    // The participants in the currently active game
    ArrayList<Participant> mParticipants = new ArrayList<>();

    // My participant ID in the currently active game
    String mMyId = null;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[4];

    Queue actionQueue = new Queue();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;





        System.out.println("android launcher starts initialize");
        initialize(new PendulumParadoxGame(this), config);

        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        //startQuickGame();
    }

    public void setInputHandler(MoveCommands inputHandler){
        this.InputHandler = inputHandler;
    }


    // Check the sample to ensure all placeholder ids are are updated with real-world values.
    // This is strictly for the purpose of the samples; you don't need this in a production
    // application.

    @Override
    protected void onResume() {
        super.onResume();

        // Since the state of the signed in user can change when the activity is not active
        // it is recommended to try and sign in silently from when the app resumes.
        signInSilently();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    public void startQuickGame() {
        // quick-start a game with 1 randomly selected opponent
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);

        mRoomConfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        mRealTimeMultiplayerClient.create(mRoomConfig);
    }

    @Override
    public boolean isRoomFull(){
        return roomFull;
    }

    @Override
    public void sendAction(String action) {
        if (mParticipants.size() == 0){
            return;
        }
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId)) {
                continue;
            }
            sendUnreliableMessage(p , action);
        }
    }

    @Override
    public void handleActions() {
        while(!actionQueue.isEmpty()){
            String curraction = (String)actionQueue.removeFirst();
            System.out.println("execute action");
            switch (curraction) {
                case "L":
                    //execute move left
                    InputHandler.moveLeft();
                    break;
                case "SL":
                    //stop move left
                    InputHandler.stopMoveLeft();
                    break;
                case "R":
                    //execute move right
                    InputHandler.moveRight();
                    break;
                case "SR":
                    //stop move right
                    InputHandler.stopMoveRight();
                    break;
                case "J":
                    //execute jump
                    InputHandler.jump();
                    break;
                case "SJ":
                    //stop execute jump
                    InputHandler.stopJump();
                    break;
                case "S":
                    //start shooting
                    InputHandler.startShooting();
                    break;
                case "SS":
                    //stop shooting
                    InputHandler.stopShooting();
                    break;
                default:
                    //error
                    break;

            }
        }

    }

    /**
     * Start a sign in activity.  To properly handle the result, call tryHandleSignInResult from
     * your Activity's onActivityResult function
     */
    public void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    /**
     * Try to sign in without displaying dialogs to the user.
     * <p>
     * If the user has already signed in previously, it will not show dialog.
     */
    public void signInSilently() {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            onConnected(task.getResult());
                        } else {
                            onDisconnected();
                        }
                    }
                });
    }






    public void signOut() {

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                        } else {
                            handleException(task.getException(), "signOut() failed!");
                        }

                        onDisconnected();
                    }
                });
    }

    /**
     * Since a lot of the operations use tasks, we can use a common handler for whenever one fails.
     *
     * @param exception The exception to evaluate.  Will try to display a more descriptive reason for the exception.
     * @param details   Will display alongside the exception if you wish to provide more details for why the exception
     *                  happened
     */
    private void handleException(Exception exception, String details) {
        int status = 0;

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            status = apiException.getStatusCode();
        }

        String errorString = null;
        switch (status) {
            case GamesCallbackStatusCodes.OK:
                break;
            case GamesClientStatusCodes.MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                break;
            case GamesClientStatusCodes.MATCH_ERROR_ALREADY_REMATCHED:
                break;
            case GamesClientStatusCodes.NETWORK_ERROR_OPERATION_FAILED:
                break;
            case GamesClientStatusCodes.INTERNAL_ERROR:
                break;
            case GamesClientStatusCodes.MATCH_ERROR_INACTIVE_MATCH:
                break;
            case GamesClientStatusCodes.MATCH_ERROR_LOCALLY_MODIFIED:
                break;
            default:
                break;
        }

        if (errorString == null) {
            return;
        }


        new AlertDialog.Builder(NetworkSynchronizationProxy.this)
                .setTitle("Error")
                .setMessage("\n" + errorString)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = "nope";
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }

        } else if (requestCode == RC_WAITING_ROOM) {
            // we got the result from the "waiting room" UI.
            if (resultCode == Activity.RESULT_OK) {
                // ready to start playing






            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player indicated that they want to leave the room
                leaveRoom();
            } else if (resultCode == Activity.RESULT_CANCELED) {
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
    public void onStop() {

        // if we're in a room, leave it.
        leaveRoom();

        // stop trying to keep the screen on


        super.onStop();
    }

    // Handle back key to make sure we cleanly leave a game if we are in the middle of one
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {

        return super.onKeyDown(keyCode, e);
    }

    // Leave the room.
    void leaveRoom() {

        if (mRoomId != null) {
            mRealTimeMultiplayerClient.leave(mRoomConfig, mRoomId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRoomId = null;
                            mRoomConfig = null;
                        }
                    });
        }
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        mRealTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        // show waiting room UI
                        startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                })
                .addOnFailureListener(createFailureListener("There was a problem getting the waiting room!"));
    }



    /*
     * CALLBACKS SECTION. This section shows how we implement the several games
     * API callbacks.
     */

    private String mPlayerId;

    // The currently signed in account, used to check the account has changed outside of this activity when resuming.
    GoogleSignInAccount mSignedInAccount = null;

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        if (mSignedInAccount != googleSignInAccount) {

            mSignedInAccount = googleSignInAccount;

            // update the clients
            mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);

            // get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();

                        }
                    })
                    .addOnFailureListener(createFailureListener("There was a problem getting the player id!"));
        }


        GamesClient gamesClient = Games.getGamesClient(NetworkSynchronizationProxy.this, googleSignInAccount);

    }

    private OnFailureListener createFailureListener(final String string) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handleException(e, string);
            }
        };
    }

    public void onDisconnected() {

        mRealTimeMultiplayerClient = null;


    }

    private RoomStatusUpdateCallback mRoomStatusUpdateCallback = new RoomStatusUpdateCallback() {
        @Override
        public void onConnectedToRoom(Room room) {

            //get participants and my ID:
            mParticipants = room.getParticipants();
            mMyId = room.getParticipantId(mPlayerId);

            // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
            if (mRoomId == null) {
                mRoomId = room.getRoomId();
            }

        }

        @Override
        public void onDisconnectedFromRoom(Room room) {
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
    void showGameError() {
        new AlertDialog.Builder(this)
                .setMessage("oops")
                .setNeutralButton(android.R.string.ok, null).create();

    }

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {

        // Called when room has been created
        @Override
        public void onRoomCreated(int statusCode, Room room) {
            if (statusCode != GamesCallbackStatusCodes.OK) {
                showGameError();
                return;
            }

            // save room ID so we can leave cleanly before the game starts.
            mRoomId = room.getRoomId();

            // show the waiting room UI
            showWaitingRoom(room);
        }

        // Called when room is fully connected.
        @Override
        public void onRoomConnected(int statusCode, Room room) {
            if (statusCode != GamesCallbackStatusCodes.OK) {
                showGameError();
                return;
            }
            updateRoom(room);
        }

        @Override
        public void onJoinedRoom(int statusCode, Room room) {
            if (statusCode != GamesCallbackStatusCodes.OK) {
                showGameError();
                return;
            }

            showWaitingRoom(room);
        }
     @Override
        public void onLeftRoom(int statusCode, @NonNull String roomId) {
        }
    };

    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
        if (mParticipants != null) {

        }
    }


    /*
     * COMMUNICATIONS SECTION.
     */


    // Called when we receive a real-time message from the network.
    OnRealTimeMessageReceivedListener mOnRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            byte[] buf = realTimeMessage.getMessageData();
            String sender = realTimeMessage.getSenderParticipantId();

            String msg = new String(buf);

            actionQueue.addLast(msg);

        }
    };





    void sendReliableMessage(Participant p, String msg){

        mMsgBuf = msg.getBytes();
        mRealTimeMultiplayerClient.sendReliableMessage(mMsgBuf,
                mRoomId, p.getParticipantId(), new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                    @Override
                    public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer tokenId) {

                    }
                });
    }



    void sendUnreliableMessage(Participant p, String msg){

        mMsgBuf = msg.getBytes();

        mRealTimeMultiplayerClient.sendUnreliableMessage(mMsgBuf, mRoomId,
                p.getParticipantId());

    }


}
