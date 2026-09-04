package view.game.map.disasters.tsunami;

import java.awt.*;

public class TsunamiEffectLayer extends WaterEffectLayer {

    private static final int RISE_TICKS = 18;
    private static final int HOLD_TICKS = 40;
    private static final int RECEDE_TICKS = 55;
    private static final int STAGGER_PER_RING = 6;

    private static final Color DEEP = new Color(10, 60, 110, 210);
    private static final Color SHALLOW = new Color(70, 150, 200, 160);
    private static final Color FOAM = new Color(255, 255, 255, 220);

    @Override protected int riseTicks() { return RISE_TICKS; }
    @Override protected int holdTicks() { return HOLD_TICKS; }
    @Override protected int recedeTicks() { return RECEDE_TICKS; }
    @Override protected int staggerPerRing() { return STAGGER_PER_RING; }
    @Override protected Color deepColor() { return DEEP; }
    @Override protected Color shallowColor() { return SHALLOW; }
    @Override protected Color foamColor() { return FOAM; }
    @Override protected double waveAmplitude() { return 7.0; }
    @Override protected double waveSpeed() { return 0.35; }
}