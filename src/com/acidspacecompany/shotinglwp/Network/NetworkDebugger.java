package com.acidspacecompany.shotinglwp.Network;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.World;

public class NetworkDebugger {

    public NetworkDebugger() {

        final Server s=new Server(1234);
        s.setOnInputEvent(new Runnable() {
            @Override
            public void run() {
                String[] msg=s.getLastInputMessage().split(" ");

                switch (msg[0].toLowerCase()) {

                    case "killall" : {
                        World.killAll();
                        break;
                    }

                    case "resize" : {
                        World.resize(Integer.valueOf(msg[1]), Integer.valueOf(msg[2]));
                        break;
                    }

                    case "select" : {
                        World.selectUnit(Integer.valueOf(msg[1]));
                        break;
                    }

                    case "print" : {
                        World.printUnit(Integer.valueOf(msg[1]));
                        break;
                    }

                    case "bullet" : {
                        World.printBullets();
                        break;
                    }

                    case "pause" : {
                        World.setPhysicIsWorking(false);
                        break;
                    }

                    case "play" : {
                        World.setPhysicIsWorking(true);
                        break;
                    }

                    case "shot" : {
                        World.shot(new Point(Integer.valueOf(msg[1]), Integer.valueOf(msg[2])), Float.valueOf(msg[3]));
                        break;
                    }
                }
            }
        });

    }

}
