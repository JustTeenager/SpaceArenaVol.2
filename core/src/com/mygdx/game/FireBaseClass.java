package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.List;


import pl.mk5.gdx.fireapp.GdxFIRApp;
import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.GdxFIRDatabase;
import pl.mk5.gdx.fireapp.annotations.MapConversion;
import pl.mk5.gdx.fireapp.auth.GdxFirebaseUser;
import pl.mk5.gdx.fireapp.distributions.DatabaseDistribution;
import pl.mk5.gdx.fireapp.functional.BiConsumer;
import pl.mk5.gdx.fireapp.functional.Consumer;
import pl.mk5.gdx.fireapp.functional.Function;

public class FireBaseClass {

    private static String uID;
    private static float kills=-1;
    private static float death=-1;

    public static void signIn(final String playerEmail, final char[] playerPassword, final AuthorizationDialog dialog) {
        // Sign in via username/email and password
        FireBaseClass.disableAutoButtons(dialog);
        GdxFIRAuth.instance()
                .signInWithEmailAndPassword(playerEmail, playerPassword)
                .then(new Consumer<GdxFirebaseUser>() {
                    @Override
                    public void accept(GdxFirebaseUser gdxFirebaseUser) {
                        //if (gdxFirebaseUser.getUserInfo()!=
                        uID=gdxFirebaseUser.getUserInfo().getUid();
                        enableAutoButtons(dialog);
                        successLogin();
                    }
                }).fail(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println("ERROR DURING LOGIN");
                enableAutoButtons(dialog);
                try {
                    throw throwable;
                } catch (Throwable e) {
                    e.printStackTrace();
                    dialog.setErrorText("Something with email or pass");
                }
            }
        });
    }


    public static void register(final String playerEmail, final char[] playerPassword, final AuthorizationDialog dialog){
        FireBaseClass.disableAutoButtons(dialog);
            GdxFIRAuth.instance()
                    .createUserWithEmailAndPassword(playerEmail, playerPassword).then(new Consumer<GdxFirebaseUser>() {
                        @Override
                        public void accept(GdxFirebaseUser gdxFirebaseUser) {
                            MainGame.current_player_name="player";
                            uID=gdxFirebaseUser.getUserInfo().getUid();
                            enableAutoButtons(dialog);
                            successRegister();
                            dialog.setErrorText("Now log in!");
                        }
                    })
                    .fail(new BiConsumer<String, Throwable>() {
                        @Override
                        public void accept(String s, Throwable throwable) {
                                //GdxFIRAuth.inst().getCurrentUser().delete().subscribe();
                            System.out.println("REGISTRATION ERROR");
                                enableAutoButtons(dialog);
                            try {
                                throw throwable;
                            } catch (Throwable e) {
                                e.printStackTrace();
                                dialog.setErrorText("Something with email or pass");
                            }
                        }
                    });
    }

    //email:
    //com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -- The email is badly formatted
    //com.google.firebase.auth.FirebaseAuthUserCollisionException --  The email is already in use
    //com.google.firebase.auth.FirebaseAuthWeakPasswordException -- The given password is invalid
    //



    public static void signOut( final AuthorizationDialog dialog){
        FireBaseClass.disableAutoButtons(dialog);
        GdxFIRAuth.instance().signOut()
                .then(new Consumer<Void>() {
                    @Override
                    public void accept(Void o) {
                        enableAutoButtons(dialog);
                        successSignOut();
                    }
                });
    }

    private static void successLogin(){
        MainGame.authorized=true;
        System.out.println("LOGGED");
    }

    private static void successSignOut(){
        MainGame.authorized=false;
        MainGame.current_player_name=null;
        System.out.println("SIGNED OUT");

    }

    private static void successRegister(){
        MainGame.registered=true;
        System.out.println("REGISTERED");
    }


    public static void updatePLayerName(final String nameActual){
        GdxFIRDatabase.instance().inReference(uID+"/Name")
                .transaction(String.class, new Function<String, String>() {
                    @Override
                    public String apply(String name) {
                        return nameActual;
                    }
                }) .fail(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                //GdxFIRAuth.inst().getCurrentUser().delete().subscribe();
                System.out.println("GETTING NAME ERROR");
            }
        });

    }

    public static void updateStatInDataBase(final int addKills,final int addDeath) {
        synchronized (GdxFIRDatabase.class) {
            GdxFIRDatabase.instance().inReference(uID + "/Kills")
                    .transaction(Long.class, new Function<Long, Long>() {
                        @Override
                        public Long apply(Long i) {
                            kills = i + addKills;
                            return (long) kills;
                        }
                    });
            GdxFIRDatabase.instance().inReference(uID + "/Death")
                    .transaction(Long.class, new Function<Long, Long>() {
                        @Override
                        public Long apply(Long i) {
                            death = i + addDeath;
                            return (long) death;
                        }
                    });
            GdxFIRDatabase.instance().inReference(uID + "/KD")
                    .transaction(String.class, new Function<String, String>() {
                        @Override
                        public String apply(String kdLast) {
                            System.out.println(kills / death);
                            System.out.println(kills);
                            System.out.println(death);
                            if (kills != -1 && death != -1) {
                                    float kd=kills/death;
                                    kills = -1;
                                    death = -1;
                                    return String.format("%.2f", kd);
                            }
                            else {
                                System.out.println("FUCK YOU");
                                return String.valueOf(kills);
                            }
                        }
                    }).fail(new BiConsumer<String, Throwable>() {
                @Override
                public void accept(String s, Throwable throwable) {
                    System.out.println("GETTING KD ERROR");
                    try {
                        throw throwable;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("updated kd");
    }

    public static void updateKDInDatabase(){
        GdxFIRDatabase.instance().inReference(uID + "/KD")
                .transaction(String.class, new Function<String, String>() {
                    @Override
                    public String apply(String kd) {
                        System.out.println(kd);
                        System.out.println(kills / death);
                        System.out.println(kills);
                        System.out.println(death);
                        if (kills != -1 && death != -1) {
                            kills = -1;
                            death = -1;
                            return String.format("%.2f", String.valueOf(kills / death));
                        }
                        else {
                            System.out.println("FUCK YOU");
                            return String.valueOf(kills);
                        }
                    }
                }).fail(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println("GETTING KD ERROR");
            }
        });
    }

    public static void addKDInDataBase() {
        GdxFIRAuth.instance().signInWithEmailAndPassword(MainGame.playerLogin,MainGame.playerPassword.toCharArray()).then(
                        GdxFIRDatabase.instance()
                                .inReference(uID+"/Death").setValue(0));
        GdxFIRAuth.instance().signInWithEmailAndPassword(MainGame.playerLogin,MainGame.playerPassword.toCharArray()).then(
                GdxFIRDatabase.instance()
                        .inReference(uID+"/Kills").setValue(0));
        GdxFIRAuth.instance().signInWithEmailAndPassword(MainGame.playerLogin,MainGame.playerPassword.toCharArray()).then(
                GdxFIRDatabase.instance()
                        .inReference(uID+"/Name").setValue("player"));
        GdxFIRAuth.instance().signInWithEmailAndPassword(MainGame.playerLogin,MainGame.playerPassword.toCharArray()).then(
                GdxFIRDatabase.instance()
                        .inReference(uID+"/KD").setValue(String.valueOf(0)));
        System.out.println("added kd");
    }

    private static void disableAutoButtons(AuthorizationDialog dialog){
        dialog.getLogInButton().setTouchable(Touchable.disabled);
        dialog.getRegisterButton().setTouchable(Touchable.disabled);
    }
    private static void enableAutoButtons(AuthorizationDialog dialog){
        dialog.getLogInButton().setTouchable(Touchable.enabled);
        dialog.getRegisterButton().setTouchable(Touchable.enabled);
    }

    public static void getUserName() {
        GdxFIRDatabase.inst()
                .inReference(uID+"/Name")
                .readValue(String.class)
                .then(new Consumer<String>() {
                    @Override
                    public void accept(String string) {
                        System.out.println("USERNAME GOVINA");
                        System.out.println(string);
                        MainGame.current_player_name=string;
                    }
                });
    }

    public static String getuID() {
        return uID;
    }

    public static void readList() {
        GdxFIRDatabase.instance().inReference(uID+"/Name")
                .readValue(List.class)
                .after(GdxFIRAuth.inst().signInWithEmailAndPassword(MainGame.playerLogin,MainGame.playerPassword.toCharArray()))
                .then(new Consumer<List<String>>() {
                    @Override
                    @MapConversion(FireBaseClass.class)
                    public void accept(List<String> list) {
                        System.out.println(list.getItems().items.length);
                        System.out.println("READ LIST");
                    }
                });
    }


}
