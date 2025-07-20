package com.norwood.mcheli.debug._v3;

import com.norwood.mcheli.helper.MCH_Utils;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class V3Debugger {
    public static final V3Debugger.Stater TOGGLE_I = new V3Debugger.Stater(24);
    public static final V3Debugger.Numeric NUM_X = new V3Debugger.Numeric("TX", 203, 205, 0.0625);
    public static final V3Debugger.Numeric NUM_Y = new V3Debugger.Numeric("TY", 200, 208, 0.0625);
    public static final V3Debugger.Numeric ROT_X = new V3Debugger.Numeric("RX", 36, 37, 5.0);
    public static final V3Debugger.Numeric ROT_Y = new V3Debugger.Numeric("RY", 49, 50, 5.0);
    public static final V3Debugger.Numeric ROT_Z = new V3Debugger.Numeric("RZ", 22, 23, 5.0);
    private static boolean tickOnce;

    static void onClient(ClientTickEvent event) {
        if (event.phase == Phase.END) {
            Arrays.fill(V3Debugger.KeyStater.tickChunk, false);
            tickOnce = false;
        }
    }

    public static boolean checkTick() {
        if (!tickOnce) {
            tickOnce = true;
            return true;
        } else {
            return false;
        }
    }

    static void info(Object o) {
        MCH_Utils.logger().info(o);
    }

    static class KeyStater {
        static final boolean[] tickChunk = new boolean[256];
        final int key;
        private boolean down;
        private boolean chunk;

        public KeyStater(int key) {
            this.key = key;
        }

        public boolean press() {
            if (tickChunk[this.key]) {
                return false;
            } else {
                boolean flag = this.keydown();
                boolean flag1 = this.chunk;
                this.chunk = flag;
                this.down = flag && !flag1;
                tickChunk[this.key] = true;
                return this.down;
            }
        }

        public boolean keydown() {
            return Keyboard.isKeyDown(this.key);
        }

        public String keyname() {
            return Keyboard.getKeyName(this.key);
        }
    }

    public static class Numeric {
        final V3Debugger.KeyStater incKey;
        final V3Debugger.KeyStater decKey;
        final double dif;
        final String name;
        double num;

        public Numeric(String name, int decKey, int incKey, double dif) {
            this.name = name;
            this.decKey = new V3Debugger.KeyStater(decKey);
            this.incKey = new V3Debugger.KeyStater(incKey);
            this.dif = dif;
        }

        public double value() {
            if (this.incKey.press()) {
                this.num = this.num + this.dif;
                V3Debugger.info("Num " + this.name + ".value : " + this.num);
            }

            if (this.decKey.press()) {
                this.num = this.num - this.dif;
                V3Debugger.info("Num " + this.name + ".value : " + this.num);
            }

            return this.num;
        }

        public float valueFloat() {
            return (float) this.value();
        }

        public int valueInt() {
            return (int) this.value();
        }
    }

    public static class Stater {
        final V3Debugger.KeyStater key;
        boolean state;

        public Stater(int key) {
            this.key = new V3Debugger.KeyStater(key);
        }

        public boolean state() {
            if (this.key.press()) {
                this.state = !this.state;
                V3Debugger.info("Key " + this.key.keydown() + ".state : " + this.state);
            }

            return this.state;
        }
    }
}
